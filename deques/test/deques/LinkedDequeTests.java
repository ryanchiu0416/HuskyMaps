package deques;

public class LinkedDequeTests extends AbstractDequeTests {
    public static <T> LinkedDequeAssert<T> assertThat(LinkedDeque<T> deque) {
        return new LinkedDequeAssert<>(deque);
    }

    @Override
    protected <T> Deque<T> createDeque() {
        return new LinkedDeque<>();
    }

    @Override
    protected <T> void checkInvariants(Deque<T> deque) {
        // cast so we can use the LinkedDeque-specific version of assertThat defined above
        assertThat((LinkedDeque<T>) deque).isValid();
    }

    // You can write additional tests here if you only want them to run for LinkedDequeTests and not ArrayDequeTests
}
