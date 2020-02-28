package puzzles.slidingpuzzle;

import edu.princeton.cs.algs4.In;
import puzzles.ShortestPathUtil;

import java.io.File;
import java.time.Duration;

/**
 * Showcases how the AStarPathFinder can be used for solving sliding puzzles.
 */
public class SlidingPuzzleDemo {

    public static void main(String[] args) {
        BoardState start = BoardState.readBoard(new In(new File("astar/puzzles/BasicPuzzle1.txt")));
        System.out.println(start);
        int N = start.size();
        BoardState goal = BoardState.solved(N);

        BoardGraph spg = new BoardGraph();

        ShortestPathUtil.printShortestPath(spg, start, goal, Duration.ofSeconds(20), "\n");
    }
}
