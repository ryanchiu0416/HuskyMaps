package arrayutils;

import java.util.Arrays;
import java.util.Comparator;

public interface ArraySearcher<T, U> {
    /**
     * Finds all items that match the target and returns an object representing them.
     * The matching items can be retrieved as-is, or first sorted according to some comparator.
     * @throws IllegalArgumentException if target is null
     */
    AbstractMatchResult<T> findAllMatches(U target);

    @FunctionalInterface
    interface Matcher<T, U> {
        /**
         * Checks whether the matchee matches the target item.
         *
         * In ArraySearcher, the matchee will be an item from the array being searched, and the
         * target item will be the argument to findAllMatches.
         *
         * The return value of this method mimics the values from Comparable/Comparator comparisons:
         *   0 if matchee matches target
         *   negative if matchee has too low of a value to match target
         *   positive if matchee has too high of a value to match target
         */
        int match(T matchee, U target);
    }

    abstract class AbstractMatchResult<T> {
        public abstract int count();
        public abstract T[] unsorted();

        public T[] sortedBy(Comparator<T> comparator) {
            T[] matches = unsorted();
            Arrays.sort(matches, comparator);
            return matches;
        }
    }
}
