package puzzles.slidingpuzzle;

import graphpathfinding.AStarGraph;
import graphpathfinding.WeightedEdge;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of the AStarGraph class for solving sliding puzzles.
 */
public class BoardGraph implements AStarGraph<BoardState> {
    @Override
    public List<WeightedEdge<BoardState>> neighbors(BoardState b) {
        List<BoardState> neighbors = b.neighbors();
        List<WeightedEdge<BoardState>> neighborEdges = new ArrayList<>();
        for (BoardState n : neighbors) {
            neighborEdges.add(new WeightedEdge<>(b, n, 1));
        }
        return neighborEdges;
    }

    @Override
    public double estimatedDistanceToGoal(BoardState v, BoardState goal) {
        int maxVal = v.size() * v.size();

        int totalDistance = 0;
        for (int i = 1; i < maxVal; i += 1) {
            totalDistance += manhattanDistance(v, goal, i);
        }
        return totalDistance;
    }

    private int manhattanDistance(BoardState b1, BoardState b2, int n) {
        int b1row = 0;
        int b1col = 0;
        for (int i = 0; i < b1.size(); i += 1) {
            for (int j = 0; j < b1.size(); j += 1) {
                if (b1.tileAt(i, j) == n) {
                    b1row = i;
                    b1col = j;
                    break;
                }
            }
        }

        int b2row = 0;
        int b2col = 0;
        for (int i = 0; i < b2.size(); i += 1) {
            for (int j = 0; j < b2.size(); j += 1) {
                if (b2.tileAt(i, j) == n) {
                    b2row = i;
                    b2col = j;
                    break;
                }
            }
        }

        return Math.abs(b1row - b2row) + Math.abs(b1col - b2col);
    }
}
