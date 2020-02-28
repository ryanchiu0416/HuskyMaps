package graphpathfinding.graphs;

import graphpathfinding.AStarGraph;
import graphpathfinding.WeightedEdge;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an infinite graph of integers, like the one shown below:
 *  ... <-> 0 <-> 1 <-> 2 <-> 3 <-> ...
 *  Every vertex i has two neighbors: the vertices i-1 and i+1.
 */
public class InfiniteBidirectionalGraph implements AStarGraph<Integer> {
    @Override
    public Collection<WeightedEdge<Integer>> neighbors(Integer v) {
        Set<WeightedEdge<Integer>> neighbors = new HashSet<>();
        neighbors.add(new WeightedEdge<>(v, v - 1, 1));
        neighbors.add(new WeightedEdge<>(v, v + 1, 1));
        return neighbors;
    }

    @Override
    public double estimatedDistanceToGoal(Integer v, Integer goal) {
        return Math.abs(v - goal);
    }
}
