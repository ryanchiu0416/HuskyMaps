package huskymaps.routing;

import graphpathfinding.AStarGraph;
import graphpathfinding.ShortestPathFinder;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import pointsets.Point;
import pointsets.PointSet;

import java.util.List;

public abstract class Router {
    /** Creates a NodePoint from a given Node, for use in a PointSet */
    protected abstract NodePoint createNodePoint(Node node);
    /*
    The following methods allow us to swap out the data structure implementations used in the Router.
    Make sure to use them instead of instantiating your own data structures if you're not sure
    whether your data structures from previous assignments are correct.
    */
    protected abstract <T extends Point> PointSet<T> createPointSet(List<T> points);
    protected abstract <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> g);

    /**
     * Returns the node closest to the given longitude and latitude coordinate.
     *
     * @param c
     * @return The node in the graph closest to the target.
     */
    protected abstract Node closest(Coordinate c);

    /**
     * Returns a List of nodes representing the shortest path from the node
     * closest to a start location to the node closest to the destination location.
     * @param start
     * @param end
     * @return A list of nodes in the order visited on the shortest path.
     */
    public abstract List<Node> shortestPath(Coordinate start, Coordinate end);

    /**
     * Creates the list of directions corresponding to a route on the graph.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigationDirections for the route.
     */
    public abstract List<NavigationDirection> routeDirections(List<Node> route);
}
