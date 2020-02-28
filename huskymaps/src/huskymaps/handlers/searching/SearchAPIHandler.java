package huskymaps.handlers.searching;

import huskymaps.handlers.APIRouteHandler;
import huskymaps.searching.Searcher;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * Handles search requests from the browser.
 */
public class SearchAPIHandler extends APIRouteHandler<SearchRequest, List<?>> {

    private Searcher searcher;

    public SearchAPIHandler(Searcher searcher) {
        this.searcher = searcher;
    }

    @Override
    protected SearchRequest parseRequest(Request request) {
        return new SearchRequest(request.queryParams("term"), request.queryParams("full") != null);
    }

    /**
     * Processes user search requests.
     *
     * @param request parameters
     * @param response ignored
     * @return a list of strings matching the searched prefix if request.full is false; otherwise,
     * a list of all locations with names exactly matching the search term.
     */
    @Override
    protected List<?> processRequest(SearchRequest request, Response response) {
        if (request.full) {
            return searcher.getLocations(request.term);
        } else {
            return searcher.getLocationsByPrefix(request.term);
        }
    }
}
