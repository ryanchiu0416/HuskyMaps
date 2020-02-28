package graphpathfinding;

import java.util.Collection;

/** Represents a graph of vertices. */
public interface AStarGraph<VERTEX> {
    /** Returns the list of outgoing edges from the given vertex. */
    Collection<WeightedEdge<VERTEX>> neighbors(VERTEX v);

    /**
     *  Returns an estimated distance from vertex v to the goal vertex according to
     *  the A* heuristic function for this graph.
     */
    double estimatedDistanceToGoal(VERTEX v, VERTEX goal);
}
