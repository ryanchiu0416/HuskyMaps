package graphpathfinding;

import java.time.Duration;
import java.util.List;

/**
 * A result object returned by {@link ShortestPathFinder#findShortestPath(VERTEX, VERTEX, Duration)}.
 */
public abstract class ShortestPathResult<VERTEX> {
    private final int numStatesExplored;
    private final Duration explorationTime;

    protected ShortestPathResult(int numStatesExplored, Duration explorationTime) {
        this.numStatesExplored = numStatesExplored;
        this.explorationTime = explorationTime;
    }

    /** Returns one of SOLVED, TIMEOUT, or UNSOLVABLE. */
    protected abstract SolverOutcome outcome();

    public boolean isSolved() {
        return outcome() == SolverOutcome.SOLVED;
    }

    public boolean isUnsolvable() {
        return outcome() == SolverOutcome.UNSOLVABLE;
    }

    public boolean isTimedOut() {
        return outcome() == SolverOutcome.TIMEOUT;
    }

    /**
     * A list of vertices corresponding to the solution, from start to end.
     * Returns an empty list if problem was unsolvable or solving timed out.
     */
    public abstract List<VERTEX> solution();

    /**
     * The total weight of the solution, taking into account edge weights.
     * Returns Double.POSITIVE_INFINITY if problem was unsolvable or solving timed out.
     */
    public abstract double solutionWeight();

    /** The total number of states explored while solving. */
    public int numStatesExplored() {
        return this.numStatesExplored;
    }

    /** The total time spent in seconds by the constructor to run A* search. */
    public Duration explorationTime() {
        return this.explorationTime;
    }

    /** This possible results of a shortest path problem. */
    public enum SolverOutcome {
        /** Path successfully found. */
        SOLVED,
        /** Path not found due to exceeding allowed computation time. */
        TIMEOUT,
        /** No path exists from start to end. */
        UNSOLVABLE
    }

    /**
     * A result representing a successfully-computed shortest path.
     */
    public static class Solved<VERTEX> extends ShortestPathResult<VERTEX> {
        private final List<VERTEX> solution;
        private final double solutionWeight;

        Solved(List<VERTEX> solution, double solutionWeight, int numStatesExplored, Duration startTime) {
            super(numStatesExplored, startTime);
            this.solution = solution;
            this.solutionWeight = solutionWeight;
        }

        @Override
        protected SolverOutcome outcome() {
            return SolverOutcome.SOLVED;
        }

        @Override
        public List<VERTEX> solution() {
            return this.solution;
        }

        @Override
        public double solutionWeight() {
            return this.solutionWeight;
        }
    }

    /**
     * A base class for results representing failed computations of shortest paths.
     */
    public abstract static class Failed<VERTEX> extends ShortestPathResult<VERTEX> {
        private final SolverOutcome outcome;

        Failed(SolverOutcome outcome, int numStatesExplored, Duration explorationTime) {
            super(numStatesExplored, explorationTime);
            this.outcome = outcome;
        }

        @Override
        protected SolverOutcome outcome() {
            return this.outcome;
        }

        @Override
        public List<VERTEX> solution() {
            return List.of();
        }

        @Override
        public double solutionWeight() {
            return Double.POSITIVE_INFINITY;
        }
    }

    /**
     * A result representing a shortest path computation that failed to terminate within the
     * allotted timeout duration.
     */
    public static class Timeout<VERTEX> extends Failed<VERTEX> {
        Timeout(int numStatesExplored, Duration explorationTime) {
            super(SolverOutcome.TIMEOUT, numStatesExplored, explorationTime);
        }
    }

    /**
     * A result representing a shortest path computation that failed to due to lack of any paths
     * between the start and end vertices.
     */
    public static class Unsolvable<VERTEX> extends Failed<VERTEX> {
        Unsolvable(int numStatesExplored, Duration explorationTime) {
            super(SolverOutcome.UNSOLVABLE, numStatesExplored, explorationTime);
        }
    }
}
