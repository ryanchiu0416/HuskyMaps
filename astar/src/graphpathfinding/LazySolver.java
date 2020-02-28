package graphpathfinding;

import timing.Timer;

import java.time.Duration;
import java.util.List;

/**
 * Very basic, syntactically-correct but semantically-incorrect shortest paths finder. It tries the
 * first edge it sees, and if that edge doesn't work it gives up and (incorrectly) says the task is
 * UNSOLVABLE.
 */
public class LazySolver<VERTEX> extends ShortestPathFinder<VERTEX> {
    private final AStarGraph<VERTEX> graph;

    public LazySolver(AStarGraph<VERTEX> graph) {
        this.graph = graph;
    }

    @Override
    public ShortestPathResult<VERTEX> findShortestPath(VERTEX start, VERTEX end, Duration timeout) {
        Timer timer = new Timer(timeout);

        for (WeightedEdge<VERTEX> e : graph.neighbors(start)) {
            if (e.to().equals(end)) {
                List<VERTEX> solution = List.of(start, end);
                double solutionWeight = e.weight();
                return new ShortestPathResult.Solved<>(solution, solutionWeight, 1, timer.elapsedDuration());
            }
        }
        if (timer.isTimeUp()) {
            return new ShortestPathResult.Timeout<>(1, timer.elapsedDuration());
        }
        return new ShortestPathResult.Unsolvable<>(1, timer.elapsedDuration());
    }

    @Override
    protected AStarGraph<VERTEX> graph() {
        return this.graph;
    }
}
