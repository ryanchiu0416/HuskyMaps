package huskymaps.searching;

import autocomplete.Autocomplete;
import autocomplete.Term;
import huskymaps.graph.Node;

import java.util.List;

public abstract class Searcher {
    /*
    The following methods allow us to swap out the data structure implementations used in the Searcher.
    Make sure to use them instead of instantiating your own data structures if you're not sure
    whether your data structures from previous assignments are correct.
    */
    protected abstract Term createTerm(String name, int weight);
    protected abstract Autocomplete createAutocomplete(Term[] termsArray);

    /**
     * Collects all the names of locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for.
     * @return A <code>List</code> of full names of locations matching the <code>prefix</code>.
     */
    public abstract List<String> getLocationsByPrefix(String prefix);

    /**
     * Collects all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose name matches the <code>locationName</code>.
     */
    public abstract List<Node> getLocations(String locationName);
}
