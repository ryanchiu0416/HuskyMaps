package priorityqueues;

import edu.washington.cse373.BaseTest;
import org.assertj.core.api.AbstractObjectAssert;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * An assert class that for heaps that provides an assert method for invariant checking.
 *
 * The check method is currently unimplemented; feel free to implement it to run invariant checks
 * on your own machine.
 */
public abstract class AbstractHeapMinPQAssert<T>
        extends AbstractObjectAssert<AbstractHeapMinPQAssert<T>, ExtrinsicMinPQ<T>> {

    public AbstractHeapMinPQAssert(ExtrinsicMinPQ<T> actual, Class<?> selfType) {
        super(actual, selfType);
    }

    protected abstract int extractStartIndex(ExtrinsicMinPQ<T> actual);
    protected abstract List<PriorityNode<T>> extractHeap(ExtrinsicMinPQ<T> actual);

    public AbstractHeapMinPQAssert<T> isValid() {
        String message = getErrorMessageIfInvalid();
        if (message != null) {
            this.as("invariant check")
                .failWithMessage(message);
        }
        return this;
    }

    protected String getErrorMessageIfInvalid() {
        List<PriorityNode<T>> heap = extractHeap(this.actual);
        int startIndex = extractStartIndex(this.actual);
        return getErrorMessageIfInvalid(heap, startIndex, startIndex + this.actual.size());
    }

    static <T> String getErrorMessageIfInvalid(List<PriorityNode<T>> heap, int startIndex, int endIndex) {
        if (heap.size() < endIndex) {
            return String.format("Heap's internal ArrayList size (%d) is too small for heap's contents? " +
                    "(Expected at least %d.)",
                heap.size(), endIndex);
        }
        // TODO (optional): implement invariant checks
        return null;
    }

    /**
     * Some basic tests for the heap invariant checks.
     */
    static class Tests extends BaseTest {
        <T> String check(List<PriorityNode<T>> heap, int startIndex, int endIndex) {
            return getErrorMessageIfInvalid(heap, startIndex, endIndex);
        }

        @Test
        void test1(FormattingTestReporter out) {
            String message = check(priorityNodes(-1, -1, -1), 0, 3);
            assertThat(message).isNotNull();
            out.publish(message);
        }

        @Test
        void test2(FormattingTestReporter out) {
            String message = check(priorityNodes(3, 2, 1), 0, 3);
            assertThat(message).isNotNull();
            out.publish(message);
        }

        @Test
        void test3(FormattingTestReporter out) {
            String message = check(priorityNodes(1, 2, -1), 0, 3);
            assertThat(message).isNotNull();
            out.publish(message);
        }

        @Test
        void test4(FormattingTestReporter out) {
            String message = check(priorityNodes(0, 1, 2, 3), 1, 4);
            assertThat(message).isNull();
        }

        @Test
        void test5(FormattingTestReporter out) {
            String message = check(priorityNodes(-1,
                1,
                2, 3,
                4, 5, 6, 1), 1, 8);
            assertThat(message).isNotNull();
            out.publish(message);
        }

        @Test
        void test6(FormattingTestReporter out) {
            String message = check(priorityNodes(-1, 1, 1, 1, 1, 1), 1, 6);
            assertThat(message).isNull();
        }

        private ArrayList<PriorityNode<Integer>> priorityNodes(double... priorities) {
            ArrayList<PriorityNode<Integer>> out = new ArrayList<>();
            for (double priority : priorities) {
                if (priority < 0) {
                    out.add(null);
                } else {
                    out.add(new PriorityNode<>(0, priority));
                }
            }
            return out;
        }
    }
}
