package graphpathfinding.graphs;

import graphpathfinding.AStarGraph;
import graphpathfinding.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/** A very simple AStarGraph with a fixed number of vertices and explicitly-specified edges. */
public class WeightedDirectedGraph implements AStarGraph<Integer> {
    private List<List<WeightedEdge<Integer>>> adj;

    /**
     * Creates a graph with the given number of vertices, and with no edges.
     */
    public WeightedDirectedGraph(int numVertices) {
        adj = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i += 1) {
            adj.add(new ArrayList<>());
        }
    }

    @Override
    public List<WeightedEdge<Integer>> neighbors(Integer v) {
        return adj.get(v);
    }

    /**
     * Very crude heuristic that just returns the weight of the smallest edge out of vertex s.
     */
    @Override
    public double estimatedDistanceToGoal(Integer v, Integer goal) {
        if (v.equals(goal)) {
            return 0;
        }
        return neighbors(v).stream()
            .mapToDouble(WeightedEdge::weight)
            .min()
            .orElse(Double.POSITIVE_INFINITY);
    }

    public void addEdge(int p, int q, double w) {
        WeightedEdge<Integer> e = new WeightedEdge<>(p, q, w);
        adj.get(p).add(e);
    }
}
