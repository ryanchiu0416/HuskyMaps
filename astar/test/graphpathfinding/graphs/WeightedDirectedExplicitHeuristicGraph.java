package graphpathfinding.graphs;

import graphpathfinding.AStarGraph;
import graphpathfinding.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple AStarGraph with a fixed number of vertices, explicitly-specified heuristic values,
 * and explicitly-specified edges.
 */
public class WeightedDirectedExplicitHeuristicGraph implements AStarGraph<Integer> {
    private List<List<WeightedEdge<Integer>>> adj;
    private final double[] heuristicValues;

    /**
     * Creates a graph using the given heuristic values (one per vertex) with no edges.
     */
    public WeightedDirectedExplicitHeuristicGraph(double... heuristicValues) {
        adj = new ArrayList<>(heuristicValues.length);
        this.heuristicValues = heuristicValues;
        for (int i = 0; i < heuristicValues.length; i += 1) {
            adj.add(new ArrayList<>());
        }
    }

    @Override
    public List<WeightedEdge<Integer>> neighbors(Integer v) {
        return adj.get(v);
    }

    @Override
    public double estimatedDistanceToGoal(Integer v, Integer goal) {
        if (v < 0 || v >= heuristicValues.length) {
            return Double.POSITIVE_INFINITY;
        }
        return heuristicValues[v];
    }

    public void addEdge(int p, int q, double w) {
        WeightedEdge<Integer> e = new WeightedEdge<>(p, q, w);
        adj.get(p).add(e);
    }
}
