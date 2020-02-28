package puzzles.slidingpuzzle;

import edu.princeton.cs.algs4.In;
import puzzles.ShortestPathUtil;

import java.io.File;
import java.time.Duration;

/**
 * Showcases how the AStarPathFinder can be used for solving sliding puzzles.
 * Runs several puzzles in a row.
 */
public class SlidingPuzzleDemoMultiple {
    private static String[] basicPuzzles = {
        "BasicPuzzle1.txt",
        "BasicPuzzle2.txt",
        "BasicPuzzle3.txt",
        "BasicPuzzle4.txt",
        "BasicPuzzle5.txt",
    };

    private static String[] hardPuzzles = {
        "HardPuzzle1.txt",
        "HardPuzzle2.txt",
        "HardPuzzle3.txt",
    };

    private static String[] elitePuzzles = {
        "ElitePuzzle1.txt",
        "ElitePuzzle2.txt",
        "ElitePuzzle3.txt",
    };

    public static void main(String[] args) {
        String[] puzzleFiles = basicPuzzles;

        System.out.println(puzzleFiles.length + " puzzle files being run.");
        for (String puzzleFile : puzzleFiles) {
            System.out.println();
            System.out.println(puzzleFile + ":");

            BoardState start = BoardState.readBoard(new In(new File("astar/puzzles/", puzzleFile)));
            int N = start.size();
            BoardState goal = BoardState.solved(N);

            BoardGraph spg = new BoardGraph();

            ShortestPathUtil.printShortestPath(spg, start, goal, Duration.ofSeconds(30), "");
        }
    }
}
