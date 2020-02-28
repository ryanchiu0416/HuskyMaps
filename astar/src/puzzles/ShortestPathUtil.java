package puzzles;

import graphpathfinding.AStarGraph;
import graphpathfinding.AStarPathFinder;
import graphpathfinding.LazySolver;
import graphpathfinding.ShortestPathFinder;
import graphpathfinding.ShortestPathResult;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
/**
 * Some helper methods for running a shortest path query and printing results.
 */
public class ShortestPathUtil {
    /**
     * Runs a shortest path query and prints a summary of the results.
     *
     * Attempts to use {@link graphpathfinding.AStarPathFinder} if possible; defaults to
     * {@link graphpathfinding.LazySolver} otherwise.
     */
    public static <VERTEX> void printShortestPath(AStarGraph<VERTEX> wdg, VERTEX start, VERTEX goal,
                                                  Duration timeout, String delimiter) {
        ShortestPathFinder<VERTEX> solver;
        try {
            solver = new AStarPathFinder<>(wdg);
        } catch (UnsupportedOperationException e) {
            System.out.println("AStarPathFinder doesn't seem to be implemented yet; using LazySolver instead.");
            solver = new LazySolver<>(wdg);
        }
        summarizeSolution(solver.findShortestPath(start, goal, timeout), delimiter);
    }

    /**
     * Summarizes the result of the search made by this solver without actually
     * printing the solution itself (if any).
     */
    public static <VERTEX> void summarizeOutcome(ShortestPathResult<VERTEX> solver) {
        summarizeSolution(solver, "", false);
    }

    /**
     * Summarizes the result of the search made by this solver and also prints
     * each vertex of the solution, connected by the given delimiter, e.g.
     * delimiter = "," would return all states separated by commas.
     */
    public static <VERTEX> void summarizeSolution(ShortestPathResult<VERTEX> solver,
                                                  String delimiter) {
        summarizeSolution(solver, delimiter, true);
    }

    private static <VERTEX> String solutionString(ShortestPathResult<VERTEX> solver,
                                                  String delimiter) {
        List<String> solutionVertices = new ArrayList<>();
        for (VERTEX v : solver.solution()) {
            solutionVertices.add(v.toString());
        }
        return String.join(delimiter, solutionVertices);
    }

    private static <VERTEX> void summarizeSolution(ShortestPathResult<VERTEX> solver,
                                                   String delimiter, boolean printSolution) {

        System.out.println("Total states explored in " + formatDuration(solver.explorationTime())
                            + ": " + solver.numStatesExplored());

        if (solver.isSolved()) {
            List<VERTEX> solution = solver.solution();
            System.out.println("Search was successful.");
            System.out.println("Solution was of length " + solution.size()
                               + ", and had total weight " + solver.solutionWeight() + ":");
            if (printSolution) {
                System.out.println(solutionString(solver, delimiter));
            }
        } else if (solver.isTimedOut()) {
            System.out.println("Search timed out, considered " + solver.numStatesExplored()
                                + " vertices before timing out.");
        } else { // (solver.isUnsolvable())
            System.out.println("Search determined that the goal is unreachable from source.");
        }
    }

    private static String formatDuration(Duration duration) {
        return duration.getSeconds() + "." + duration.toMillisPart() + "s";
    }
}
