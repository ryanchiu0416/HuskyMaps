package graphpathfinding;

import edu.washington.cse373.BaseTest;
import graphpathfinding.graphs.CustomVertexGraph;
import graphpathfinding.graphs.InfiniteBidirectionalGraph;
import graphpathfinding.graphs.InfiniteUnidirectionalGraph;
import graphpathfinding.graphs.WeightedDirectedExplicitHeuristicGraph;
import graphpathfinding.graphs.WeightedDirectedGraph;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.stream.IntStream;

public class PathFinderTests extends BaseTest {
    private static final Duration ONE_SECOND = Duration.ofSeconds(1);

    protected <VERTEX> ShortestPathFinder<VERTEX> createShortestPathFinder(AStarGraph<VERTEX> graph) {
        return new AStarPathFinder<>(graph);
    }

    protected <VERTEX> ShortestPathFinderAssert<VERTEX> assertThat(ShortestPathFinder<VERTEX> actual) {
        return new ShortestPathFinderAssert<>(actual);
    }

    @Test
    void findOn_specExample_returnsCorrectPath() {
        WeightedDirectedGraph graph = new WeightedDirectedGraph(6);

        graph.addEdge(0, 1, 50);
        graph.addEdge(0, 2, 20);

        graph.addEdge(1, 4, 20);

        graph.addEdge(2, 3, 10);

        graph.addEdge(3, 4, 70);

        graph.addEdge(4, 3, 10);
        graph.addEdge(4, 5, 100);

        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);

        int start = 0;
        int end = 5;
        assertThat(pathFinder).shortestPath(start, end, ONE_SECOND)
            .hasSolution(0, 1, 4, 5)
            .hasWeightCloseTo(170);
    }

    @Test
    void findOn_lectureExample_returnsCorrectPath() {
        WeightedDirectedGraph graph = new WeightedDirectedGraph(7);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 1);

        graph.addEdge(1, 2, 5);
        graph.addEdge(1, 3, 11);
        graph.addEdge(1, 4, 3);

        graph.addEdge(2, 5, 15);

        graph.addEdge(3, 4, 2);

        graph.addEdge(4, 2, 1);
        graph.addEdge(4, 5, 4);
        graph.addEdge(4, 6, 5);

        graph.addEdge(6, 3, 1);
        graph.addEdge(6, 5, 1);

        ShortestPathFinder<Integer> finder = new AStarPathFinder<>(graph);

        int start = 0;
        int end = 6;
        System.out.println(finder.findShortestPath(start, end, ONE_SECOND).solution());
        assertThat(finder).shortestPath(start, end, ONE_SECOND)
            .hasSolution(0, 1, 4, 6)
            .hasWeightCloseTo(10);
    }

    @Test
    void findOn_graphWithSingleEdge_returnsCorrectPath() {
        WeightedDirectedGraph graph = new WeightedDirectedGraph(2);
        graph.addEdge(0, 1, Math.PI);
        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);

        assertThat(pathFinder).shortestPath(0, 1, ONE_SECOND)
            .hasSolution(0, 1)
            .hasWeightCloseTo(Math.PI);
    }

    @Test
    void findWithSameStartAndEndVertex_returnsCorrectPath() {
        WeightedDirectedGraph graph = new WeightedDirectedGraph(2);
        graph.addEdge(0, 1, 500);
        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);

        assertThat(pathFinder).shortestPath(0, 0, ONE_SECOND)
            .hasSolution(0)
            .hasWeightCloseTo(0);
    }

    @Test
    void findOn_infiniteGraph_returnsCorrectPath() {
        InfiniteBidirectionalGraph graph = new InfiniteBidirectionalGraph();
        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);

        assertThat(pathFinder).shortestPath(0, 5, ONE_SECOND)
            .hasSolution(0, 1, 2, 3, 4, 5)
            .hasWeightCloseTo(5);
    }

    @Test
    void findNonexistentPathOn_infiniteGraph_returnsTimedOut() {
        AStarGraph<Integer> graph = new InfiniteUnidirectionalGraph();
        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);
        assertThat(pathFinder).shortestPath(0, -1, Duration.ofMillis(100))
            .isTimedOut();
    }

    @Test
    void findNonexistentPathOn_smallGraph_returnsUnsolvable() {
        WeightedDirectedGraph graph = new WeightedDirectedGraph(2);
        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);
        assertThat(pathFinder).shortestPath(0, 1, ONE_SECOND)
            .isUnsolvable();
    }

    @Test
    void findOn_graphWithMultiplePathsBetweenStartAndEnd_returnsCorrectPath_1() {
        WeightedDirectedGraph graph = new WeightedDirectedGraph(5);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 4, 1);
        graph.addEdge(1, 3, 6);
        graph.addEdge(2, 3, 5);

        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);
        assertThat(pathFinder).shortestPath(0, 3, ONE_SECOND)
            .hasSolution(0, 2, 3)
            .hasWeightCloseTo(10);
    }

    @Test
    void findOn_graphWithMultiplePathsBetweenStartAndEnd_returnsCorrectPath_2() {
        WeightedDirectedExplicitHeuristicGraph graph = new WeightedDirectedExplicitHeuristicGraph(0, 0, 0, 0, 0);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 3);
        graph.addEdge(0, 3, 5);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 4, 10);
        graph.addEdge(3, 2, 1);

        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);
        assertThat(pathFinder).shortestPath(0, 4, ONE_SECOND)
            .hasSolution(0, 2, 4)
            .hasWeightCloseTo(13);
    }

    @Test
    void findOn_graphWhereLeastCostPathHasMoreEdgesThanOtherPaths_returnsCorrectPath() {
        WeightedDirectedGraph graph = new WeightedDirectedGraph(7);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 5, 1);
        graph.addEdge(0, 4, 2);
        graph.addEdge(4, 5, 3);
        graph.addEdge(4, 6, 1);

        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);
        ShortestPathResult<Integer> res = pathFinder.findShortestPath(0, 5, ONE_SECOND);
        System.out.println(res.solution());
        System.out.println(res.solutionWeight());

        assertThat(pathFinder).shortestPath(0, 5, ONE_SECOND)
            .hasSolution(0, 1, 2, 3, 5)
            .hasWeightCloseTo(4);
    }

    @Test
    void findOn_graphRequiringRelaxation_returnsCorrectPath() {
        int vertices = 20;
        int mid = vertices / 2;
        int end = vertices - 1;

        WeightedDirectedExplicitHeuristicGraph graph = new WeightedDirectedExplicitHeuristicGraph(new double[vertices]);
        graph.addEdge(0, 1, 1);
        for (int i = 2; i < end; i++) {
            graph.addEdge(0, i, i*2);
        }
        graph.addEdge(1, mid, 1);
        graph.addEdge(mid, end, 1);
        graph.addEdge(2, end, 1);

        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);
        ShortestPathResult<Integer> res = pathFinder.findShortestPath(0, end, ONE_SECOND);
        System.out.println(res.solution());
        System.out.println(res.solutionWeight());


        assertThat(pathFinder).shortestPath(0, end, ONE_SECOND)
            .hasSolution(0, 1, mid, end)
            .hasWeightCloseTo(3);
    }

    /**
     * We don't usually check the number of explored states, since that's very likely to change
     * based on how the ties in shortest paths are broken and when exactly vertices are considered
     * to be explored, but in this simple graph, we can use explored states to sanity check whether
     * the A* heuristic is being used properly. (If not, the number of explored states would be
     * about double what we're expecting.)
     */
    @Test
    void findOn_infiniteGraph_exploresReasonableNumberOfStates() {
        InfiniteBidirectionalGraph graph = new InfiniteBidirectionalGraph();
        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);

        assertThat(pathFinder).shortestPath(0, 10, ONE_SECOND)
            .hasSolution(IntStream.rangeClosed(0, 10).boxed().toArray(Integer[]::new))
            .extractStatesExplored().isLessThan(13).isGreaterThan(7);
        // allow being off by up to 2, since we don't specify when exactly to consider a state explored
    }

    /**
     * Make sure the solution's weight is 0. If it isn't, it's likely that the heuristic values
     * are affecting the solution weight somehow.
     */
    @Test
    void findOn_graphWithOnlyZeroWeightEdges_returnsCorrectPath() {
        WeightedDirectedExplicitHeuristicGraph graph = new WeightedDirectedExplicitHeuristicGraph(1, 1, 1, 0);
        graph.addEdge(0, 1, 0);
        graph.addEdge(1, 2, 0);
        graph.addEdge(2, 3, 0);

        ShortestPathFinder<Integer> pathFinder = createShortestPathFinder(graph);
        assertThat(pathFinder).shortestPath(0, 3, ONE_SECOND)
            .hasSolution(0, 1, 2, 3)
            .hasWeightCloseTo(0);
    }

    /**
     * This test is likely to fail for graphs that use == instead of the equals method.
     */
    @Test
    void findOn_graphWithCustomVertexObjects_returnsCorrectPath() {
        CustomVertexGraph graph = new CustomVertexGraph(3);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);

        ShortestPathFinder<CustomVertexGraph.CustomVertex> pathFinder = createShortestPathFinder(graph);
        CustomVertexGraph.CustomVertex v0 = new CustomVertexGraph.CustomVertex(0);
        CustomVertexGraph.CustomVertex v1 = new CustomVertexGraph.CustomVertex(1);
        CustomVertexGraph.CustomVertex v2 = new CustomVertexGraph.CustomVertex(2);
        assertThat(pathFinder).shortestPath(v0, v2, ONE_SECOND)
            .hasSolution(v0, v1, v2);
    }
}
