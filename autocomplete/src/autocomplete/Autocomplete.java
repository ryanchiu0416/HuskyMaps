package autocomplete;

import arrayutils.ArraySearcher;
import arrayutils.BinaryRangeSearcher;
import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

import static arrayutils.ArraySearcher.Matcher;

/**
 * A class that loads Terms from a file and uses ArraySearcher to find autocomplete suggestions
 * from those Terms.
 *
 * Includes a console interface to display the suggestions to a user.
 */
public class Autocomplete {
    protected static final String FILENAME = "autocomplete/data/cities.txt";

    private final ArraySearcher<Term, String> searcher;

    /**
     * Creates a new Autocomplete instance for the given array of Terms.
     *
     * Since {@link Comparator} and {@link Matcher} are defined as {@link FunctionalInterface}s,
     * we can simply pass in the methods from {@link Term} as arguments and let Java automagically
     * convert them into the proper objects.
     */
    public Autocomplete(Term[] terms) {
        this(BinaryRangeSearcher.forUnsortedArray(terms, Term::queryOrder, Term::matchesPrefix));
    }

    protected Autocomplete(ArraySearcher<Term, String> searcher) {
        this.searcher = searcher;
    }

    public Term[] findMatchesForPrefix(String prefix) {
        return this.searcher.findAllMatches(prefix).sortedBy(Term::reverseWeightOrder);
    }

    static Autocomplete loadFromFile(String filename) {
        return loadFromFile(filename, DefaultTerm::new, Autocomplete::new);
    }

    static Autocomplete loadFromFile(String filename,
                                     BiFunction<String, Long, Term> termCreator,
                                     Function<Term[], Autocomplete> autocompleteCreator) {
        Term[] terms = null;
        try {
            In in = new In(filename);
            String line0 = in.readLine();
            if (line0 == null) {
                System.err.println("Could not read line 0 of " + filename);
                System.exit(1);
            }
            int n = Integer.parseInt(line0);
            terms = new Term[n];
            for (int i = 0; i < n; i++) {
                String line = in.readLine();
                if (line == null) {
                    System.err.println("Could not read line " + (i+1) + " of " + filename);
                    System.exit(1);
                }
                int tab = line.indexOf('\t');
                if (tab == -1) {
                    System.err.println("No tab character in line " + (i+1) + " of " + filename);
                    System.exit(1);
                }
                long weight = Long.parseLong(line.substring(0, tab).trim());
                String query = line.substring(tab + 1);
                terms[i] = termCreator.apply(query, weight);
            }
        }
        catch (Exception e) {
            System.err.println("Could not read or parse input file " + filename);
            e.printStackTrace();
            System.exit(1);
        }
        return autocompleteCreator.apply(terms);
    }

    public static void main(String[] args) {
        Autocomplete auto = Autocomplete.loadFromFile(FILENAME);
        runConsoleLoop(auto);
    }

    protected static void runConsoleLoop(Autocomplete auto) {
        Scanner consoleInput = new Scanner(System.in);

        System.out.println("Loaded data from " + FILENAME);

        while (true) {
            System.out.print("Enter a prefix to search for (or \"exit\" to quit): ");
            String input = consoleInput.nextLine();
            if (input.equals("exit")) {
                System.out.println("  Quitting.");
                System.exit(0);
            }
            Term[] matches = auto.findMatchesForPrefix(input);
            Arrays.stream(matches).forEach(m -> System.out.printf("  - %8s: %s%n", m.weight(), m.query()));
        }
    }
}
