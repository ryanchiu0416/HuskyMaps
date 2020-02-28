package arrayutils;

public class LinearSearcherTests extends AbstractArraySearcherTests {
    @Override
    protected <T, U> ArraySearcher<T, U> createArraySearcher(T[] array, ArraySearcher.Matcher<T, U> matcher) {
        return new LinearSearcher<>(array, matcher);
    }
}
