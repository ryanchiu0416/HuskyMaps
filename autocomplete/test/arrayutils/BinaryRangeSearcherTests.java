package arrayutils;

public class BinaryRangeSearcherTests extends AbstractArraySearcherTests {
    @Override
    protected <T, U> ArraySearcher<T, U> createArraySearcher(T[] array, ArraySearcher.Matcher<T, U> matcher) {
        return new BinaryRangeSearcher<>(array, matcher);
    }
}
