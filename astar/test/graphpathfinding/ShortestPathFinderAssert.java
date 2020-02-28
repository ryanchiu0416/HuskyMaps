package graphpathfinding;

import org.assertj.core.api.AbstractDoubleAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.ListAssert;
import puzzles.slidingpuzzle.BoardGraph;
import puzzles.wordladder.WordGraph;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.within;

/**
 * Asserts for ShortestPathFinders and ShortestPathResults.
 */
public class ShortestPathFinderAssert<VERTEX>
    extends AbstractObjectAssert<ShortestPathFinderAssert<VERTEX>, ShortestPathFinder<VERTEX>> {

    public ShortestPathFinderAssert(ShortestPathFinder<VERTEX> actual) {
        super(actual, ShortestPathFinderAssert.class);
    }

    public ShortestPathResultAssert<VERTEX> shortestPath(VERTEX start, VERTEX end, Duration timeout) {
        return new ShortestPathResultAssert<>(actual.findShortestPath(start, end, timeout), actual.graph(), start, end);
    }

    public static class ShortestPathResultAssert<VERTEX>
        extends AbstractObjectAssert<ShortestPathResultAssert<VERTEX>, ShortestPathResult<VERTEX>> {

        public static final double EPSILON = .0001;
        private final AStarGraph<VERTEX> graph;
        private final VERTEX start;
        private final VERTEX end;

        public ShortestPathResultAssert(ShortestPathResult<VERTEX> actual,
                                        AStarGraph<VERTEX> graph,
                                        VERTEX start,
                                        VERTEX end) {
            super(actual, ShortestPathResultAssert.class);
            this.graph = graph;
            this.start = start;
            this.end = end;
        }

        public AbstractObjectAssert<?, ShortestPathResult.SolverOutcome> extractOutcome() {
            return extracting(ShortestPathResult::outcome)
                .as(describe("outcome"));
        }

        public ListAssert<Object> extractSolution() {
            return extracting(ShortestPathResult::solution, InstanceOfAssertFactories.list(Object.class))
                .as(describe("solution"));
        }

        public AbstractIntegerAssert<?> extractStatesExplored() {
            return extracting(ShortestPathResult::numStatesExplored, InstanceOfAssertFactories.INTEGER)
                .as(describe("num states explored"));
        }

        public AbstractDoubleAssert<?> extractWeight() {
            return extracting(ShortestPathResult::solutionWeight, InstanceOfAssertFactories.DOUBLE)
                .as(describe("weight"));
        }

        public ShortestPathResultAssert<VERTEX> isUnsolvable() {
            extractOutcome().isEqualTo(ShortestPathResult.SolverOutcome.UNSOLVABLE);
            return this;
        }

        /**
         * This assert probably isn't very useful, but we may as well include it.
         */
        public ShortestPathResultAssert<VERTEX> isTimedOut() {
            extractOutcome().isEqualTo(ShortestPathResult.SolverOutcome.TIMEOUT);
            return this;
        }

        @SafeVarargs
        public final ShortestPathResultAssert<VERTEX> hasSolution(VERTEX... vertices) {
            extractOutcome().isEqualTo(ShortestPathResult.SolverOutcome.SOLVED);
            // Use Object here since we don't have the exactly type of VERTEX
            extractSolution().containsExactly(vertices);
            return this;
        }

        public ShortestPathResultAssert<VERTEX> hasEquivalentSolutionTo(ShortestPathResult<VERTEX> expected) {
            extractOutcome().isEqualTo(expected.outcome());
            if (expected.isSolved()) {
                hasWeightCloseTo(expected.solutionWeight());
                pathIsValid();
            }
            return this;
        }

        public ShortestPathResultAssert<VERTEX> hasWeightCloseTo(double expected) {
            extractWeight().isCloseTo(expected, within(EPSILON));
            return this;
        }

        public ShortestPathResultAssert<VERTEX> pathIsValid() {
            if (!actual.isSolved()) {
                // skip if not solved; validity check only matters when solution exists
                return this;
            }
            // check start and end match
            extractSolution().first().as(describe("first vertex")).isEqualTo(this.start);
            extractSolution().last().as(describe("last vertex")).isEqualTo(this.end);
            // check transitions are valid according to graph
            checkHasValidTransitions();
            return this;
        }

        private void checkHasValidTransitions() {
            List<VERTEX> solution = actual.solution();

            for (int i = 0; i < solution.size() - 1; i++) {
                VERTEX curr = solution.get(i);
                VERTEX next = solution.get(i + 1);
                boolean passed = false;

                if (graph instanceof WordGraph || graph instanceof BoardGraph) {
                    // rely on properties of heuristics for these two graphs
                    if (graph.estimatedDistanceToGoal(curr, next) == 1) {
                        passed = true;
                    }
                } else {
                    Iterable<WeightedEdge<VERTEX>> neighbors = graph.neighbors(curr);

                    for (WeightedEdge<VERTEX> edge : neighbors) {
                        if (edge.to().equals(next)) {
                            passed = true;
                            break;
                        }
                    }
                }

                if (!passed) {
                    as(describe("solution")).
                        failWithMessage("Invalid transition found in solution. No edge exists in graph "
                            + describeFromTo(curr, next));
                }
            }
        }

        private String describe(String s) {
            return s + " of shortest path " + describeFromTo(this.start, this.end);
        }

        private String describeFromTo(VERTEX from, VERTEX to) {
            String fromString = from.toString().stripTrailing();
            String toString = to.toString().stripTrailing();
            if (fromString.contains("\n") || toString.contains("\n")) {
                return String.format("from:%n%s%n%nto:%s", fromString, toString);
            }
            return String.format("from <%s> to <%s>", fromString, toString);
        }
    }
}
