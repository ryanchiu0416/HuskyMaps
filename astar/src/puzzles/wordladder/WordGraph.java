package puzzles.wordladder;

import edu.princeton.cs.algs4.In;
import graphpathfinding.AStarGraph;
import graphpathfinding.WeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A class that represents a graph of a set of words.
 * Word p has an edge to word q iff the edit distance between p and q is 1.
 * For example, there is an edge from "horse" to "hose", and "hose" to "horse".
 * There is no edge from "dog" to "deg", because deg isn't a word (according to words10000.txt).
 */
public class WordGraph implements AStarGraph<String> {
    private Set<String> words;

    /**
     * Creates a new WordGraph with the words from the specified {@link In}.
     * @param in
     */
    public static WordGraph readWords(In in) {
        Set<String> words = new HashSet<>();

        while (!in.isEmpty()) {
            String w = in.readString();
            words.add(w);
        }

        return new WordGraph(words);
    }

    /** Creates a new WordGraph with the given words. */
    public WordGraph(Set<String> words) {
        this.words = words;
    }

    /**
     * Computes the edit distance between a and b.
     * @source https://rosettacode.org/wiki/Levenshtein_distance
     */
    private static int editDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]),
                        a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    @Override
    public List<WeightedEdge<String>> neighbors(String v) {
        List<WeightedEdge<String>> neighbors = new ArrayList<>();
        for (String w : words) {
            if (editDistance(v, w) == 1) {
                neighbors.add(new WeightedEdge<>(v, w, 1));
            }
        }
        return neighbors;
    }

    @Override
    public double estimatedDistanceToGoal(String v, String goal) {
        return editDistance(v, goal);
    }

    public Set<String> getWords() {
        return Collections.unmodifiableSet(this.words);
    }
}
