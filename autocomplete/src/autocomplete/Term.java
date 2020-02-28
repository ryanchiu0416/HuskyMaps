package autocomplete;

import java.util.Comparator;

public interface Term {
    /** Returns this term's query. */
    String query();

    /** Returns this term's weight. */
    long weight();

    /**
     * Compares to another term in lexicographic order by query.
     * (Obeys semantics for {@link Comparator#compare}.)
     * @return a negative integer, zero, or a positive integer as this.query
     *         is less than, equal to, or greater than that.query.
     * @throws NullPointerException if the specified object is null
     */
    int queryOrder(Term that);

    /**
     * Compares to another term in descending order by weight.
     * (Obeys semantics for {@link Comparator#compare}.)
     * @return a negative integer, zero, or a positive integer as this.weight
     *         is greater than, equal to, or less than that.weight.
     * @throws NullPointerException if the specified object is null
     */
    int reverseWeightOrder(Term that);

    /**
     * Compares this term's query to the given prefix.
     *
     * Mimics the return value semantics of {@link Comparable#compareTo}
     * and {@link Comparator#compare}:
     * <ul>
     *   <li>0 if query starts with prefix</li>
     *   <li>negative if query comes before prefix lexicographically</li>
     *   <li>positive if query comes after prefix lexicographically</li>
     * </ul>
     *
     * (Does not exactly match their definitions, since it's possible that
     * {@code sgn(a.matchesPrefix(b)) != -sgn(b.matchesPrefix(a))}.)
     */
    int matchesPrefix(String prefix);
}
