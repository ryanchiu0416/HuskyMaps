package arrayutils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Make sure to check out the interface for more method details:
 * @see ArraySearcher
 */
public class LinearSearcher<T, U> implements ArraySearcher<T, U> {
    private final T[] array;
    private final Matcher<T, U> matcher;

    /**
     * Creates a LinearSearcher for the given array of items that matches items using the
     * Matcher matchUsing.
     *
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate the array).
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if matchUsing is null
     */
    public static <T, U> LinearSearcher<T, U> forArray(T[] array, Matcher<T, U> matchUsing) {
        if (array == null || matchUsing == null) {
            throw new IllegalArgumentException();
        }
        for (T item : array) {
            if (item == null) {
                throw new IllegalArgumentException();
            }
        }
        return new LinearSearcher<>(array, matchUsing);
    }

    /**
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate the array).
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if matcher is null
     */
    protected LinearSearcher(T[] array, Matcher<T, U> matcher) {
        if (array == null || matcher == null) {
            throw new IllegalArgumentException();
        }
        for (T item : array) {
            if (item == null) {
                throw new IllegalArgumentException();
            }
        }
        this.array = array;
        this.matcher = matcher;
    }

    public MatchResult<T> findAllMatches(U target) {
        if (target == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<T> matches = new ArrayList<>();
        for (T item : this.array) {
            if (this.matcher.match(item, target) == 0) {
                matches.add(item);
            }
        }
        // We need to jump through some hoops to get a new T[] due to the way Java handles generics.
        T[] matchesArray = matches.toArray(Arrays.copyOf(this.array, matches.size()));
        return new MatchResult<>(matchesArray);
    }

    public static class MatchResult<T> extends AbstractMatchResult<T> {
        final T[] array;

        protected MatchResult(T[] array) {
            this.array = array;
        }

        @Override
        public int count() {
            return this.array.length;
        }

        @Override
        public T[] unsorted() {
            return array;
        }
    }
}

