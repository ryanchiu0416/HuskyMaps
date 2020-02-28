package deques;

import edu.washington.cse373.BaseTest;
import org.assertj.core.api.FactoryBasedNavigableIterableAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertFactory;
import org.junit.jupiter.api.Test;

// Don't worry about the crazy things happening in the generic types of this class.
public class LinkedDequeAssert<T> extends FactoryBasedNavigableIterableAssert<
        LinkedDequeAssert<T>, LinkedDeque<? extends T>, T, ObjectAssert<T>> {
    protected final LinkedDeque<T> actual;

    public LinkedDequeAssert(LinkedDeque<T> actual) {
        super(actual, LinkedDequeAssert.class, new ObjectAssertFactory<>());
        this.actual = actual;
    }

    public LinkedDequeAssert<T> isValid() {
        String message = getErrorMessageIfInvalid(actual.front, actual.back);
        if (message != null) {
            failWithMessage(message);
        }
        return this;
    }

    /**
     * Returns null if the given front and back nodes form a valid linked deque. Otherwise, returns
     * a string describing the error.
     */
    static String getErrorMessageIfInvalid(LinkedDeque.Node<?> front, LinkedDeque.Node<?> back) {
        // TO DO replace this with your code
        // currently always passes; return any string to make the assertion fail.
        return null;
    }

    /**
     * Some tests for getErrorMessageIfInvalid.
     */
    static class TestCheckLinkedDequeIsValid extends BaseTest {
        /**
         * It's not strictly necessary to wrap this method like this, but doing this allows us
         * to swap out the implementation of getErrorMessageIfInvalid being tested, so that e.g.
         * we can reuse this class to test our own solution.
         */
        String getErrorMessageIfInvalid(LinkedDeque.Node<?> front, LinkedDeque.Node<?> back) {
            return LinkedDequeAssert.getErrorMessageIfInvalid(front, back);
        }


        LinkedDeque.Node<Void> node() {
            return new LinkedDeque.Node<>(null);
        }

        LinkedDeque.Node<Void> node(LinkedDeque.Node<Void> prev, LinkedDeque.Node<Void> next) {
            LinkedDeque.Node<Void> node = new LinkedDeque.Node<>(null);
            node.next = next;
            node.prev = prev;
            return node;
        }

        @Test
        void testNulls() {
            String message = getErrorMessageIfInvalid(null, null);
            assertThat(message).isNull();
        }

        @Test
        void testMismatchedNulls() {
            LinkedDeque.Node<Void> node = node();
            String message = getErrorMessageIfInvalid(node, null);
            assertThat(message).isNotNull();
        }

        @Test
        void testOneMismatchedNode() {
            LinkedDeque.Node<Void> node = node();
            node.next = node;
            String message = getErrorMessageIfInvalid(node, node);
            assertThat(message).isNotNull();
        }

        @Test
        void testTwoNodes() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> back = node(front, null);
            front.next = back;
            String message = getErrorMessageIfInvalid(front, back);
            assertThat(message).isNull();
        }

        @Test
        void testTwoCircularNodes() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> node = node(front, front);
            front.next = node;
            front.prev = node;
            String message = getErrorMessageIfInvalid(front, front);
            assertThat(message).isNull();
        }

        @Test
        void testTwoUnlinkedNodes() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> back = node();
            String message = getErrorMessageIfInvalid(front, back);
            assertThat(message).isNotNull();
        }

        @Test
        void testThreeNodes() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> back = node();
            LinkedDeque.Node<Void> middle = node(front, back);
            front.next = middle;
            back.prev = middle;
            String message = getErrorMessageIfInvalid(front, back);
            assertThat(message).isNull();
        }

        @Test
        void testThreeCircularNodes() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> node1 = node(front, null);
            LinkedDeque.Node<Void> node2 = node(node1, front);
            front.prev = node2;
            front.next = node1;
            node1.next = node2;
            String message = getErrorMessageIfInvalid(front, front);
            assertThat(message).isNull();
        }

        @Test
        void testThreeNodesWithNullPointer() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> back = node();
            LinkedDeque.Node<Void> middle = node(front, back);
            back.prev = middle;
            String message = getErrorMessageIfInvalid(front, back);
            assertThat(message).isNotNull();
        }

        @Test
        void testThreeNodesWithSkippedPointer() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> back = node();
            LinkedDeque.Node<Void> middle = node(front, back);
            front.next = middle;
            back.prev = front;
            String message = getErrorMessageIfInvalid(front, back);
            assertThat(message).isNotNull();
        }

        @Test
        void testWeirdCycle() {
            LinkedDeque.Node<Void> front = node();
            LinkedDeque.Node<Void> middle1 = node(front, null);
            LinkedDeque.Node<Void> middle2 = node(middle1, null);
            LinkedDeque.Node<Void> back = node(middle2, null);
            front.next = middle1;
            middle1.next = middle2;
            middle2.next = front;
            String message = getErrorMessageIfInvalid(front, back);
            assertThat(message).isNotNull();
        }
    }
}
