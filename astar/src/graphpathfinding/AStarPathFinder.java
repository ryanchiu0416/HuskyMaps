package graphpathfinding;

import priorityqueues.ArrayHeapMinPQ;
import priorityqueues.ExtrinsicMinPQ;
import timing.Timer;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @see ShortestPathFinder for more method documentation
 */
public class AStarPathFinder<VERTEX> extends ShortestPathFinder<VERTEX> {
    private AStarGraph<VERTEX> graph;
    private Map<VERTEX, Double> distances;
    private Map<VERTEX, VERTEX> previousVertex;
    private int totalNumExplored;

    /**
     * Creates a new AStarPathFinder that works on the provided graph.
     */
    public AStarPathFinder(AStarGraph<VERTEX> graph) {
        this.graph = graph;
        this.distances = new HashMap<>();
        this.previousVertex = new HashMap<>();
        this.totalNumExplored = 0;
    }

    @Override
    public ShortestPathResult<VERTEX> findShortestPath(VERTEX start, VERTEX end, Duration timeout) {
        Timer timer = new Timer(timeout);

        boolean isFound = aStarSearch(start, end, timer);
        if (isFound) {
            List<VERTEX> solution = new LinkedList<>();
            VERTEX curr = end;
            while (!curr.equals(start)) {
                solution.add(0, curr);
                curr = previousVertex.get(curr);
            }
            solution.add(0, curr);
            return new ShortestPathResult.Solved<>(solution, distances.get(end),
                                                   totalNumExplored, timer.elapsedDuration());
        } else if (timer.isTimeUp()) {
            return new ShortestPathResult.Timeout<>(totalNumExplored, timer.elapsedDuration());
        } else {
            return new ShortestPathResult.Unsolvable<>(totalNumExplored, timer.elapsedDuration());
        }
    }

    private boolean aStarSearch(VERTEX start, VERTEX goal, Timer timer) {
        ExtrinsicMinPQ<VERTEX> pq = new ArrayHeapMinPQ<>();
        pq.add(start, 0.0 + graph.estimatedDistanceToGoal(start, goal));
        distances.put(start, 0.0);

        while (!pq.isEmpty()) {
            VERTEX curr = pq.removeMin();
            totalNumExplored++;
            for (WeightedEdge<VERTEX> edge: graph.neighbors(curr)) {
                VERTEX node = edge.to();
                if (timer.isTimeUp()) {
                    return false;
                }

                if (distances.containsKey(node) && distances.get(node) < distances.get(curr) + edge.weight()) {
                    continue;
                } else {
                    distances.put(node, distances.get(curr) + edge.weight());
                    previousVertex.put(node, curr);
                    if (pq.contains(node)) {
                        pq.changePriority(node, distances.get(node) + graph.estimatedDistanceToGoal(node, goal));
                    } else {
                        pq.add(node, distances.get(node) + graph.estimatedDistanceToGoal(node, goal));
                    }
                }
            }
            if (curr.equals(goal)) {
                return true; // isFound = true;
            }
        }
        return false;
    }


    @Override
    protected AStarGraph<VERTEX> graph() {
        return this.graph;
    }
}
