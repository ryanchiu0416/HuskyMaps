package deques;

/**
 * Even if it looks like there are no tests here, we actually provided a couple
 * of unit tests for you in the AbstractDequeTests class, which can be run for
 * both ArrayDeque and LinkedDeque. This is a good example of how ADT allows
 * for multiple implementations!
 *
 * Try clicking the green Run button below.
 */
public class ArrayDequeTests extends AbstractDequeTests {
    @Override
    protected <T> Deque<T> createDeque() {
        return new ArrayDeque<>();
    }

    @Override
    protected <T> void checkInvariants(Deque<T> deque) {
        // do nothing
    }

    // You can write additional tests here if you only want them to run for ArrayDequeTests and not LinkedDequeTests
}
