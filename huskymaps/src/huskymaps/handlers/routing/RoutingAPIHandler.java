package huskymaps.handlers.routing;

import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.handlers.APIRouteHandler;
import huskymaps.routing.NavigationDirection;
import huskymaps.routing.Router;
import spark.Request;
import spark.Response;

import java.util.List;

/**
 * Handles requests from the web browser for routes between locations. The
 * route will be returned as image data, as well as (optionally) driving directions.
 */
public class RoutingAPIHandler extends APIRouteHandler<RouteRequest, RouteResult> {

    private Router router;

    public RoutingAPIHandler(Router router) {
        this.router = router;
    }

    @Override
    protected RouteRequest parseRequest(Request request) {
        return RouteRequest.from(request);
    }

    /**
     * Takes a user query in the form of a pair of (lat/lon) values, and finds
     * street directions between the given points.
     * @param request RouteRequest
     * @param response Ignored.
     * @return RouteResult
     */
    @Override
    protected RouteResult processRequest(RouteRequest request, Response response) {
        Coordinate start = new Coordinate(request.startLat, request.startLon);
        Coordinate end = new Coordinate(request.endLat, request.endLon);
        List<Node> routeNodes = router.shortestPath(start, end);
        Coordinate[] routeCoords = routeNodes.stream().map(Coordinate::fromNode).toArray(Coordinate[]::new);
        String directionsText = getDirectionsText(routeNodes);
        return new RouteResult(routeCoords, directionsText);
    }

    /**
     * Takes the given route and converts it into an HTML-friendly String.
     * @param routeNodes
     */
    private String getDirectionsText(List<Node> routeNodes) {
        List<NavigationDirection> directions = router.routeDirections(routeNodes);
        if (directions == null || directions.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int step = 1;
        for (NavigationDirection d: directions) {
            sb.append(String.format("%d. %s <br>", step, d));
            step += 1;
        }
        return sb.toString();
    }
}
