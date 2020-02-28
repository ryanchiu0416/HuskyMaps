package huskymaps.graph;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *
 *  The idea here is that some external library is going to walk through the XML file,
 *  and call the startElement and endElement methods when it enters and exits every
 *  element in the file--similar to the Visitor pattern.
 */
class OSMGraphLoader extends DefaultHandler {

    /** Only allow for non-service roads; this prevents going on pedestrian streets. */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = Set.of(
            "motorway", "trunk", "primary", "secondary", "tertiary", "unclassified", "residential",
            "living_street", "motorway_link", "trunk_link", "primary_link", "secondary_link",
            "tertiary_link"
    );
    private String activeState;
    private boolean validWay;
    private String wayName;
    private final StreetMapGraph g;
    private Node.Builder nodeBuilder;
    private Queue<Long> nodePath;
    private Map<String, Integer> places;
    private Map<Long, Node> nodes = new HashMap<>();

    private OSMGraphLoader(StreetMapGraph g, Map<String, Integer> places) {
        this.places = places;
        this.activeState = "";
        this.validWay = false;
        this.wayName = "";
        this.g = g;
        this.nodeBuilder = g.nodeBuilder();
        this.nodePath = new ArrayDeque<>();
    }

    /**
     * Return the cleaned, normalized version of the string.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String normalize(String s) {
        return s.strip()
                .replace('“', '"')
                .replace('”', '"')
                .replace('‘', '\'')
                .replace('’', '\'');
    }

    public static void populateGraph(StreetMapGraph g, File osmGzipFile, File placesFile) {
        try {
            populateGraph(g, new FileInputStream(osmGzipFile), new FileInputStream(placesFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void populateGraph(StreetMapGraph g, File osmGzipFile, InputStream placesStream) {
        try {
            populateGraph(g, new FileInputStream(osmGzipFile), placesStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /** Initializes a graph from an OSM file. Assumes files are correctly formatted. */
    public static void populateGraph(StreetMapGraph g, InputStream osmGzipStream, InputStream placesStream) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            Map<String, Integer> places;
            try {
                Reader fileReader = new InputStreamReader(placesStream);
                places = new Gson().fromJson(fileReader, new TypeToken<HashMap<String, Integer>>() {}.getType());
            } catch (NullPointerException e) {
                e.printStackTrace();
                places = Map.of();
            }
            OSMGraphLoader handler = new OSMGraphLoader(g, places);
            saxParser.parse(new GZIPInputStream(osmGzipStream), handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called at the beginning of an element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("node")) {
            /* We encountered a new <node...> tag. */
            activeState = "node";
            nodeBuilder.setId(Long.parseLong(attributes.getValue("id")))
                    .setLat(Double.parseDouble(attributes.getValue("lat")))
                    .setLon(Double.parseDouble(attributes.getValue("lon")));
        } else if (qName.equals("way")) {
            /* We encountered a new <way...> tag. */
            activeState = "way";
        } else if (activeState.equals("way") && qName.equals("nd")) {
            /* While looking at a way, we found a <nd...> tag. */
            nodePath.add(Long.parseLong(attributes.getValue("ref")));
        } else if (activeState.equals("way") && qName.equals("tag")) {
            /* While looking at a way, we found a <tag...> tag. */
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            if (k.equals("highway")) {
                validWay = ALLOWED_HIGHWAY_TYPES.contains(v);
            } else if (k.equals("name")) {
                wayName = v;
            }
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k").equals("name")) {
            String name = normalize(attributes.getValue("v"));
            nodeBuilder.setName(name).setImportance(places.getOrDefault(name, 0));
        }
    }

    /**
     * Receive notification of the end of an element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("way")) {
            /* We are done looking at a way. (We finished looking at the nodes, speeds, etc...)*/
            if (validWay && !nodePath.isEmpty()) {
                Node from = this.nodes.get(nodePath.remove());
                assert from != null;
                while (!nodePath.isEmpty()) {
                    Node to = this.nodes.get(nodePath.remove());
                    assert to != null;
                    g.addWeightedEdge(from, to, wayName);
                    g.addWeightedEdge(to, from, wayName);
                    from = to;
                }
            }
            clearStates();
        } else if (qName.equals("node")) {
            Node node = nodeBuilder.createNode();
            this.nodes.put(node.id(), node);
            g.addNode(node);
            clearStates();
        }
    }

    private void clearStates() {
        activeState = "";
        validWay = false;
        nodePath.clear();
        nodeBuilder = g.nodeBuilder();
        wayName = "";
    }
}
