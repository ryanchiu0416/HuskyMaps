package huskymaps.searching;

import autocomplete.Autocomplete;
import autocomplete.DefaultTerm;
import autocomplete.Term;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @see Searcher
 */
public class DefaultSearcher extends Searcher {
    private Autocomplete autocompleteObject;
    private List<Node> allNodes;

    public DefaultSearcher(StreetMapGraph graph) {
        this.allNodes = graph.allNodes();
        Set<Node> allNodesNoDuplicate = new HashSet<>(this.allNodes);

        List<Term> tmp = new ArrayList<>();
        for (Node n : allNodesNoDuplicate) {
            if (n.name() != null) {
                tmp.add(createTerm(n.name(), n.importance()));
            }
        }
        Term[] terms = tmp.toArray(Term[]::new);
        this.autocompleteObject = createAutocomplete(terms);
    }

    @Override
    protected Term createTerm(String name, int weight) {
        return new DefaultTerm(name, weight);
    }

    @Override
    protected Autocomplete createAutocomplete(Term[] termsArray) {
        return new Autocomplete(termsArray);
    }

    @Override
    public List<String> getLocationsByPrefix(String prefix) { // no duplicate
        Term[] resultTerms = autocompleteObject.findMatchesForPrefix(prefix);
        List<String> locations = new ArrayList<>();
        for (Term rTerm : resultTerms) {
            locations.add(rTerm.query());
        }
        return locations;
    }

    @Override
    public List<Node> getLocations(String locationName) { // allow duplicates
        List<Node> matchLocations = new ArrayList<>();
        for (Node n : this.allNodes) {
            if (n.name() != null && n.name().equals(locationName)) {
                matchLocations.add(n);
            }
        }
        return matchLocations;
    }
}
