package huskymaps.graph;

import graphpathfinding.AStarGraph;
import graphpathfinding.WeightedEdge;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static huskymaps.utils.Spatial.greatCircleDistance;

public class StreetMapGraph implements AStarGraph<Node> {
    protected Map<Node, Set<WeightedEdge<Node>>> neighbors = new HashMap<>();
    protected List<Node> nodes = new ArrayList<>();

    /** Creates a new StreetMapGraph from the data in the specified resources. */
    public static StreetMapGraph fromResources(String osmGzipResourceName, String placesResourceName) {
        StreetMapGraph graph = new StreetMapGraph();
        OSMGraphLoader.populateGraph(graph,
            StreetMapGraph.class.getResourceAsStream(osmGzipResourceName),
            StreetMapGraph.class.getResourceAsStream(placesResourceName)
        );
        return graph;
    }

    /** Creates a new StreetMapGraph from the data in the specified file and resource. */
    public static StreetMapGraph fromFileAndResource(File osmGzipFile, String placesResourceName) {
        StreetMapGraph graph = new StreetMapGraph();
        OSMGraphLoader.populateGraph(graph, osmGzipFile, StreetMapGraph.class.getResourceAsStream(placesResourceName));
        return graph;
    }

    /** Creates a new StreetMapGraph from the data in the specified input streams. */
    public static StreetMapGraph fromStreams(InputStream osmGzip, InputStream places) {
        StreetMapGraph graph = new StreetMapGraph();
        OSMGraphLoader.populateGraph(graph, osmGzip, places);
        return graph;
    }

    /** Returns a list of outgoing edges for V. Assumes V exists in this graph. */
    @Override
    public Set<WeightedEdge<Node>> neighbors(Node v) {
        Set<WeightedEdge<Node>> edges = neighbors.get(v);
        if (edges == null) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(edges);
        }
    }

    @Override
    public double estimatedDistanceToGoal(Node v, Node goal) {
        return greatCircleDistance(Coordinate.fromNode(v), Coordinate.fromNode(goal));
    }

    /** Returns an unmodifiable list of all nodes in the graph. */
    public List<Node> allNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /** Adds an edge to this graph, using distance as the weight. */
    protected void addWeightedEdge(Node from, Node to, String name) {
        double weight = greatCircleDistance(Coordinate.fromNode(from), Coordinate.fromNode(to));
        neighbors.computeIfAbsent(from, k -> new HashSet<>())
            .add(new WeightedEdge<>(from, to, weight, name));
    }

    /** Adds an edge to this graph. */
    protected void addWeightedEdge(Node from, Node to, double weight, String name) {
        neighbors.computeIfAbsent(from, k -> new HashSet<>())
            .add(new WeightedEdge<>(from, to, weight, name));
    }

    /** Adds a node to this graph. */
    protected void addNode(Node node) {
        this.nodes.add(node);
    }

    protected Node.Builder nodeBuilder() {
        return new Node.Builder();
    }
}
