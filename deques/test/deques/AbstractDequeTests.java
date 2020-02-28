package deques;

import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

/**
 * Tests written in this class will be run by both ArrayDequeTests and LinkedDequeTests
 */
public abstract class AbstractDequeTests extends BaseTest {
    protected abstract <T> Deque<T> createDeque();
    protected abstract <T> void checkInvariants(Deque<T> deque);

    @Test
    void sizeOf_empty() {
        Deque<String> deque = createDeque();
        assertThat(deque).isEmpty();
    }

    @Test
    void getFrom_empty() {
        Deque<String> deque = createDeque();
        String output = deque.get(0);
        assertThat(output).isNull();
        checkInvariants(deque);
    }

    /**
     * These tests are not symmetrical.
     * Tip: the grader will versions of these tests that add/remove from the other sides as well.
     */
    @Nested
    class Asymmetrical {
        @Test
        void addTo_empty() {
            Deque<String> deque = createDeque();
            deque.addFirst("s");
            checkInvariants(deque);
        }

        @Test
        void sizeOf_singleElement() {
            Deque<String> deque = createDeque();
            deque.addFirst("s");
            assertThat(deque).hasSize(1);
            checkInvariants(deque);
        }

        @Test
        void getFrom_singleElement() {
            Deque<String> deque = createDeque();
            deque.addFirst("s");
            String output = deque.get(0);
            assertThat(output).isEqualTo("s");
            checkInvariants(deque);
        }

        @Test
        void removeSameSideFrom_single$element() {
            Deque<String> deque = createDeque();
            deque.addFirst("s");
            String output = deque.removeFirst();
            assertThat(output).isEqualTo("s");
            checkInvariants(deque);
        }

        @Test
        void removeOppositeSideFrom_single$element() {
            Deque<String> deque = createDeque();
            deque.addFirst("s");
            String output = deque.removeLast();
            assertThat(output).isEqualTo("s");
            checkInvariants(deque);
        }

        @Test
        void removeFrom_empty() {
        Deque<String> deque = createDeque();
            String output = deque.removeFirst();
            assertThat(output).isNull();
            checkInvariants(deque);
        }

        @Test
        void getAfter_addToOppositeEnds() {
            Deque<String> deque = createDeque();
            deque.addFirst("a");
            deque.addLast("b");
            assertThat(deque).containsExactly("a", "b");
            checkInvariants(deque);
        }

        @Test
        void sizeAfter_addToOppositeEnds() {
            Deque<String> deque = createDeque();
            deque.addFirst("a");
            deque.addLast("b");
            assertThat(deque).hasSize(2);
            checkInvariants(deque);
        }

        @Test
        void removeAfter_addToOppositeEnds() {
            Deque<String> deque = createDeque();
            deque.addFirst("a");
            deque.addLast("b");
            String output = deque.removeFirst();
            assertThat(output).isEqualTo("a");
            checkInvariants(deque);
        }

        @Test
        void sizeAfter_removeAfter_addToOppositeEnds() {
            Deque<String> deque = createDeque();
            deque.addFirst("a");
            deque.addLast("b");
            deque.removeFirst();
            assertThat(deque).hasSize(1);
            checkInvariants(deque);
        }

        @Test
        void removeAllAfter_addToOppositeEnds() {
            Deque<String> deque = createDeque();
            deque.addFirst("a");
            deque.addLast("b");
            deque.removeFirst();
            String output = deque.removeLast();
            assertThat(output).isEqualTo("b");
            checkInvariants(deque);
        }

        private Integer[] convert(IntStream stream) {
            return stream.boxed().toArray(Integer[]::new);
        }

        @Test
        void getAfter_addManyToSameSide() {
            Deque<Integer> deque = createDeque();
            IntStream.range(0, 20).forEach(deque::addLast);
            assertThat(deque).containsExactly(convert(IntStream.range(0, 20)));
            checkInvariants(deque);
        }
    }

    @Test
    void getNegativeFrom_empty() {
        Deque<String> deque = createDeque();
        String output = deque.get(-1);
        assertThat(output).isNull();
        checkInvariants(deque);
    }

    @Test
    void getPositiveFrom_empty() {
        Deque<String> deque = createDeque();
        String output = deque.get(1);
        assertThat(output).isNull();
        checkInvariants(deque);
    }

    @Test
    void usingMultipleDequesSimultaneously() {
        Deque<Integer> d1 = createDeque();
        Deque<Integer> d2 = createDeque();
        d1.addFirst(1);
        d2.addFirst(2);
        d1.addFirst(3);

        assertThat(d1).hasSize(2);
        assertThat(d2).hasSize(1);
    }

    /** Checks whether you can create Deques of different parameterized types. */
    @Nested
    class DifferentTypes {
        @Test
        @DisplayName("String")
        void testString() {
            Deque<String> student1 = createDeque();
            student1.addFirst("string");
            String s = student1.removeFirst();
            assertThat(s).isEqualTo("string");
        }
        @Test
        @DisplayName("Double")
        void testDouble() {
            Deque<Double> student2 = createDeque();
            student2.addFirst(3.1415);
            double d = student2.removeFirst();
            assertThat(d).isEqualTo(3.1415);
        }

        @Test
        @DisplayName("Boolean")
        void testBoolean() {
            Deque<Boolean> student3 = createDeque();
            student3.addFirst(true);
            boolean b = student3.removeFirst();
            assertThat(b).isTrue();
        }
    }

    @Test
    void myTest() {
        Deque<String> d = new LinkedDeque<>();
        d.addFirst("b");
        d.addFirst("a");
        System.out.println(d.removeFirst());
        System.out.println(d.removeFirst());

    }


    @Test
    void confusingTest() {
        Deque<Integer> deque = createDeque();
        deque.addFirst(0);
        assertThat((int) deque.get(0)).isEqualTo(0);

        deque.addLast(1);
        assertThat((int) deque.get(1)).isEqualTo(1);

        deque.addFirst(-1);
        deque.addLast(2);
        assertThat((int) deque.get(3)).isEqualTo(2);

        deque.addLast(3);
        deque.addLast(4);

        // Test that removing and adding back is okay
        assertThat((int) deque.removeFirst()).isEqualTo(-1);
        deque.addFirst(-1);
        assertThat((int) deque.get(0)).isEqualTo(-1);

        deque.addLast(5);
        deque.addFirst(-2);
        deque.addFirst(-3);

        // Test a tricky sequence of removes
        assertThat((int) deque.removeFirst()).isEqualTo(-3);
        assertThat((int) deque.removeLast()).isEqualTo(5);
        assertThat((int) deque.removeLast()).isEqualTo(4);
        assertThat((int) deque.removeLast()).isEqualTo(3);
        assertThat((int) deque.removeLast()).isEqualTo(2);

        int actual = deque.removeLast();
        assertThat(actual).isEqualTo(1);
    }
}
