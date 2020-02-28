package puzzles.wordladder;

import edu.princeton.cs.algs4.In;
import puzzles.ShortestPathUtil;

import java.io.File;
import java.time.Duration;

/** Showcases how the AStarPathFinder can be used for solving word ladders. */
public class WordPuzzleDemo {
    public static void main(String[] args) {
        String start = "horse";
        String goal = "nurse";

        WordGraph wg = WordGraph.readWords(new In(new File("astar/puzzles/words10000.txt")));

        ShortestPathUtil.printShortestPath(wg, start, goal, Duration.ofSeconds(10), "->");
    }
}
