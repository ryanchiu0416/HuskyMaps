package huskymaps;

import huskymaps.graph.StreetMapGraph;
import huskymaps.handlers.APIRouteHandler;
import huskymaps.handlers.RedirectAPIHandler;
import huskymaps.handlers.rastering.RasterAPIHandler;
import huskymaps.handlers.routing.RoutingAPIHandler;
import huskymaps.handlers.searching.SearchAPIHandler;
import huskymaps.rastering.DefaultRasterer;
import huskymaps.routing.DefaultRouter;
import huskymaps.searching.DefaultSearcher;

import java.util.Map;

import static huskymaps.utils.Constants.OSM_GZ_RESOURCE_NAME;
import static huskymaps.utils.Constants.PLACES_RESOURCE_NAME;
import static huskymaps.utils.Constants.PORT;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class MapServer {
    protected final Map<String, APIRouteHandler<?, ?>> handlers;

    public MapServer(Map<String, APIRouteHandler<?, ?>> handlers) {
        this.handlers = handlers;
    }

    /** Entry point for the MapServer. Everything starts here. */
    public static void main(String[] args) {
        StreetMapGraph graph = StreetMapGraph.fromResources(OSM_GZ_RESOURCE_NAME, PLACES_RESOURCE_NAME);
        new MapServer(Map.of(
            "/raster", new RasterAPIHandler(new DefaultRasterer()),
            "/route", new RoutingAPIHandler(new DefaultRouter(graph)),
            "/search", new SearchAPIHandler(new DefaultSearcher(graph)),
            "/", new RedirectAPIHandler()
        )).start();
    }

    public void start() {
        port(getPort());

        staticFileLocation("/page");
        /* Allow for all origin requests (since this is not an authenticated server, we do not
         * care about CSRF).  */
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
        });

        for (Map.Entry<String, APIRouteHandler<?, ?>> apiRoute : handlers.entrySet()) {
            get(apiRoute.getKey(), apiRoute.getValue());
        }
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.parseInt(port);
        }
        return PORT;
    }
}
