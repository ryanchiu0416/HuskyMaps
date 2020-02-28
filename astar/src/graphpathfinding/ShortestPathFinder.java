package graphpathfinding;

import java.time.Duration;

/**
 * Interface for shortest path solvers.
 */
public abstract class ShortestPathFinder<VERTEX> {
    /**
     * Computes a shortest path from start to end in some graph (probably set in the constructor,
     * or another method), and returns an object with information about that path and some other
     * details about the computation.
     *
     * Should only be called once. Subsequent calls have undefined behavior.
     *
     * If a path is not found after timeout duration has passed, stops execution and returns a
     * failed TIMEOUT result instead.
     * If no path between start and end is in the graph, returns an UNSOLVABLE result.
     */
    public abstract ShortestPathResult<VERTEX> findShortestPath(VERTEX start, VERTEX end, Duration timeout);

    /**
     * Returns the graph that this shortest path finder runs on.
     * Intended to be used for testing feedback only.
     *
     * (A more-general version of this class that supports multiple different algorithms might
     * return a more-general Graph type than just AStarGraph, but for simplicity, we'll leave this
     * type over-specified.)
     */
    protected abstract AStarGraph<VERTEX> graph();
}
