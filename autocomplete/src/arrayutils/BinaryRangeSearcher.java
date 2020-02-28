package arrayutils;

import java.util.Arrays;
import java.util.Comparator;
/**
 * Make sure to check out the interface for more method details:
 * @see ArraySearcher
 */
public class BinaryRangeSearcher<T, U> implements ArraySearcher<T, U> {
    private final T[] array;
    private final Matcher<T, U> matcher;
    /**
     * Creates a BinaryRangeSearcher for the given array of items that matches items using the
     * Matcher matchUsing.
     *
     * First sorts the array in place using the Comparator sortUsing. (Assumes that the given array
     * will not be used externally afterwards.)
     *
     * Requires that sortUsing sorts the array such that for any possible reference item U,
     * calling matchUsing.match(T, U) on each T in the sorted array will result in all negative
     * values first, then all 0 values, then all positive.
     *
     * For example:
     * sortUsing lexicographic string sort: [  aaa,  abc,   ba,  bzb, cdef ]
     * matchUsing T is prefixed by U
     * matchUsing.match for prefix "b":     [   -1,   -1,    0,    0,    1 ]
     *
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if sortUsing or matchUsing is null
     */
    public static <T, U> BinaryRangeSearcher<T, U> forUnsortedArray(T[] array,
                                                                    Comparator<T> sortUsing,
                                                                    Matcher<T, U> matchUsing) {
        /*
        Tip: To reduce redundancy, you can let the BinaryRangeSearcher constructor throw some of
        the exceptions mentioned in this method's documentation. The caller doesn't care which
        method exactly causes the exception, as long as it's something that happens while
        executing this method.
        */
        Arrays.sort(array, sortUsing);
        return new BinaryRangeSearcher<>(array, matchUsing);
    }

    /**
     * Requires that array is sorted such that for any possible reference item U,
     * calling matchUsing.match(T, U) on each T in the sorted array will result in all negative
     * values first, then all 0 values, then all positive.
     *
     * Assumes that the given array will not be used externally afterwards (and thus may directly
     * store and mutate the array).
     * @throws IllegalArgumentException if array is null or contains null
     * @throws IllegalArgumentException if matcher is null
     */
    protected BinaryRangeSearcher(T[] array, Matcher<T, U> matcher) {
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
        int headMatch = this.matcher.match(this.array[0], target);
        int tailMatch = this.matcher.match(this.array[this.array.length - 1], target);
        int startInclusive;
        int endInclusive;
        if (headMatch == 0 && tailMatch == 0) { //all are matches
            startInclusive = 0;
            endInclusive = this.array.length - 1;
        } else if (headMatch == tailMatch) { // no match possible
            startInclusive = -1;
            endInclusive = -1;
        } else if (headMatch == -1 && tailMatch == 0) {
            startInclusive = searchStartIndex(target, 0, this.array.length - 1);
            endInclusive = this.array.length - 1;
        } else if (headMatch == 0 && tailMatch == 1) {
            startInclusive = 0;
            endInclusive = searchEndIndex(target, 0, this.array.length - 1);
        } else { // headMatch == -1 && tailMatch == 1 (might not contain match == 0)
            startInclusive = searchStartIndex(target, 0, this.array.length - 1);
            endInclusive = searchEndIndex(target, 0, this.array.length - 1);
        }
        if (startInclusive == -1 && endInclusive == -1) {
            return new BinaryRangeSearcher.MatchResult<>(this.array);
        } else {
            return new BinaryRangeSearcher.MatchResult<>(this.array, startInclusive, endInclusive + 1);
        }
    }
    // head     tail
    //   -1       -1   no match
    //   0         0   all match
    //   1         1   no match
    //   -1        0   some match
    //   -1        1   some match
    //   0         1   some match

    private int searchStartIndex(U str, int start, int end) {
        if (start > end) {
            return -1;
        }
        int mid = (start + end) / 2;
        if (this.matcher.match(this.array[mid], str) > 0) {
            return searchStartIndex(str, start, mid - 1);
        } else if (this.matcher.match(this.array[mid], str) < 0) {
            return searchStartIndex(str, mid + 1, end);
        } else if (mid == 0 || this.matcher.match(this.array[mid - 1], str) < 0) {
            return mid; // front start inclusive
        } else {
            return searchStartIndex(str, start, mid - 1);
        }
    }

    private int searchEndIndex(U str, int start, int end) {
        if (start > end) {
            return -1;
        }
        int mid = (start + end) / 2;
        if (this.matcher.match(this.array[mid], str) > 0) {
            return searchEndIndex(str, start, mid - 1);
        } else if (this.matcher.match(this.array[mid], str) < 0) {
            return searchEndIndex(str, mid + 1, end);
        } else if (mid == this.array.length - 1 || this.matcher.match(this.array[mid + 1], str) > 0) {
            return mid; // back end inclusive
        } else {
            return searchEndIndex(str, mid + 1, end);
        }
    }



    public static class MatchResult<T> extends AbstractMatchResult<T> {
        final T[] array;
        final int start;
        final int end;

        /**
         * Use this constructor if there are no matching results.
         * (This lets us use Arrays.copyOfRange to make a new T[], which can be difficult to
         * acquire otherwise due to the way Java handles generics.)
         */
        protected MatchResult(T[] array) {
            this(array, 0, 0);
        }

        protected MatchResult(T[] array, int startInclusive, int endExclusive) {
            this.array = array;
            this.start = startInclusive;
            this.end = endExclusive;
        }

        @Override
        public int count() {
            return this.end - this.start;
        }

        @Override
        public T[] unsorted() {
            return Arrays.copyOfRange(this.array, this.start, this.end);
        }
    }
}
