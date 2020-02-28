package huskymaps.routing;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.ShortestPathResult;
import graphpathfinding.WeightedEdge;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import pointsets.KDTreePointSet;
import pointsets.Point;
import pointsets.PointSet;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static huskymaps.utils.Spatial.projectToPoint;

/**
 * @see Router
 */
public class DefaultRouter extends Router {
    private StreetMapGraph graph;
    private List<NodePoint> allPointsFromGraph;

    public DefaultRouter(StreetMapGraph graph) {
        this.graph = graph;
        this.allPointsFromGraph = new ArrayList<>();
        for (Node n : this.graph.allNodes()) {
            if (!this.graph.neighbors(n).isEmpty()) {
                allPointsFromGraph.add(createNodePoint(n));
            }
        }
    }

    @Override
    protected <T extends Point> PointSet<T> createPointSet(List<T> points) {
        return KDTreePointSet.createAfterShuffling(points);
    }

    @Override
    protected <VERTEX> ShortestPathFinder<VERTEX> createPathFinder(AStarGraph<VERTEX> g) {
        return new AStarPathFinder<>(g);
    }

    @Override
    protected NodePoint createNodePoint(Node node) {
        return projectToPoint(Coordinate.fromNode(node), (x, y) -> new NodePoint(x, y, node));
    }

    @Override
    protected Node closest(Coordinate c) {
        // Project to x and y coordinates instead of using raw lat and lon for finding closest points:
        PointSet<NodePoint> kdNodePoints = createPointSet(allPointsFromGraph);
        Point p = projectToPoint(c, Point::new);
        NodePoint closestNode = kdNodePoints.nearest(p);
        return closestNode.node();
    }

    @Override
    public List<Node> shortestPath(Coordinate start, Coordinate end) {
        Node src = closest(start);
        Node dest = closest(end);

        ShortestPathResult<Node> result = createPathFinder(this.graph).
                                          findShortestPath(src, dest, Duration.ofSeconds(90));
        return result.solution();
    }

    @Override
    public List<NavigationDirection> routeDirections(List<Node> route) {
        List<Double> directionAngle = new ArrayList<>();
        for (int i = 0; i < route.size() - 1; i++) {
            Node start = route.get(i);
            Node end = route.get(i + 1);
            directionAngle.add(getAngle(start, end));
        }

        List<NavigationDirection> results = new ArrayList<>();
        NavigationDirection beginNav = new NavigationDirection();
        if (route.size() >= 2) {
            WeightedEdge<Node> edge = getEdge(route.get(0), route.get(1));
            beginNav.direction = NavigationDirection.START;
            beginNav.way = edge.name();
            beginNav.distance = edge.weight();
            int currDirection = NavigationDirection.STRAIGHT;
            String currWayName = beginNav.way;
            double currDistance = beginNav.distance;

            for (int i = 0; i < directionAngle.size() - 1; i++) {
                int dir = NavigationDirection.getDirection(directionAngle.get(i), directionAngle.get(i + 1));
                edge = getEdge(route.get(i), route.get(i + 1));
                if (currWayName.equals(edge.name()) && (dir == NavigationDirection.SLIGHT_LEFT
                    || dir == NavigationDirection.SLIGHT_RIGHT || dir == NavigationDirection.STRAIGHT)) {
                    currDistance += edge.weight();
                } else {
                    NavigationDirection currNav = new NavigationDirection();
                    currNav.way = currWayName;
                    currNav.distance = currDistance;
                    if (results.isEmpty()) {
                        currNav.direction = NavigationDirection.START;
                    } else {
                        currNav.direction = currDirection;
                    }
                    results.add(currNav);

                    currDirection = dir;
                    currDistance = edge.weight();
                    currWayName = edge.name();
                }
            }

            NavigationDirection finalNav = new NavigationDirection();
            finalNav.way = currWayName;
            finalNav.distance = currDistance;
            finalNav.direction = currDirection;
            results.add(finalNav);
        }
        return results;
    }

    private WeightedEdge<Node> getEdge(Node n1, Node n2) {
        WeightedEdge<Node> matchEdge = null;
        for (WeightedEdge<Node> edge : this.graph.neighbors(n1)) {
            if (n2 == edge.to()) {
                matchEdge = edge;
            }
        }
        return matchEdge;
    }

    private double getAngle(Node start, Node end) {
        double degree = Math.toDegrees(Math.atan2(end.lat() - start.lat(), end.lon() - start.lon())) - 90.0;
        if (degree < 0) {
            degree += 360.0;
        }
        return degree;
    }
}
