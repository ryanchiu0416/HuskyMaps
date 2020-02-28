package graphpathfinding.graphs;

import graphpathfinding.AStarGraph;
import graphpathfinding.WeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** A graph with custom vertex objects. */
public class CustomVertexGraph implements AStarGraph<CustomVertexGraph.CustomVertex> {
    private List<List<WeightedEdge<CustomVertex>>> adj;

    /**
     * Creates a graph with the given number of vertices, and with no edges.
     */
    public CustomVertexGraph(int numVertices) {
        adj = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i += 1) {
            adj.add(new ArrayList<>());
        }
    }

    @Override
    public List<WeightedEdge<CustomVertex>> neighbors(CustomVertex v) {
        return adj.get(v.id);
    }

    /**
     * Very crude heuristic that just returns the weight of the smallest edge out of vertex s.
     */
    @Override
    public double estimatedDistanceToGoal(CustomVertex v, CustomVertex goal) {
        if (v.equals(goal)) {
            return 0;
        }
        return neighbors(v).stream()
            .mapToDouble(WeightedEdge::weight)
            .min()
            .orElse(Double.POSITIVE_INFINITY);
    }

    public void addEdge(int p, int q, double w) {
        WeightedEdge<CustomVertex> e = new WeightedEdge<>(new CustomVertex(p), new CustomVertex(q), w);
        adj.get(p).add(e);
    }

    /**
     * The custom vertex type used in CustomVertexGraph.
     * Basically just an int wrapper.
     */
    public static class CustomVertex {
        private final int id;

        public CustomVertex(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CustomVertex that = (CustomVertex) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "CustomVertex{" +
                "id=" + id +
                '}';
        }
    }
}
