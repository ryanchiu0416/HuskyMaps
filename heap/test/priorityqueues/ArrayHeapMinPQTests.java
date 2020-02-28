package priorityqueues;

import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Some provided tests for ArrayHeapMinPQ.
 *
 * If you wish, you can extend this class and override createMinPQ and assertThat to run on
 * NaiveMinPQ instead (although you'd need to make a dummy NaiveMinPQAssert class that with an
 * invariant check that does nothing).
 */
public class ArrayHeapMinPQTests extends BaseTest {

    protected <T extends Comparable<T>> ExtrinsicMinPQ<T> createMinPQ() {
        return new ArrayHeapMinPQ<>();
    }

    protected <T extends Comparable<T>> AbstractHeapMinPQAssert<T> assertThat(ExtrinsicMinPQ<T> pq) {
        return new ArrayHeapMinPQAssert<>((ArrayHeapMinPQ<T>) pq);
    }

    @Nested
    @DisplayName("New Empty")
    class NewEmpty {
        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            return createMinPQ();
        }

        @Test
        void size_returns0() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(0);
            assertThat(pq).isValid();
        }

        @Test
        void contains_returnsFalse() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            boolean output = pq.contains(412);
            assertThat(output).isFalse();
            assertThat(pq).isValid();
        }

        @Test
        void peekMin_throwsNoSuchElement() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThatThrownBy(pq::peekMin).isInstanceOf(NoSuchElementException.class);
            assertThat(pq).isValid();
        }

        @Test
        void removeMin_throwsNoSuchElement() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThatThrownBy(pq::removeMin).isInstanceOf(NoSuchElementException.class);
            assertThat(pq).isValid();
        }

        @Test
        void changePriority_throwsNoSuchElement() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThatThrownBy(() -> pq.changePriority(1, 7)).isInstanceOf(NoSuchElementException.class);
            assertThat(pq).isValid();
        }

        @Test
        void addNull_throwsIllegalArgument() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThatThrownBy(() -> pq.add(null, 15)).isInstanceOf(IllegalArgumentException.class);
            assertThat(pq).isValid();
        }
    }

    @Nested
    @DisplayName("Empty After Adding/Removing 3")
    class EmptyAfterAddRemove extends NewEmpty {
        @Override
        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            ExtrinsicMinPQ<Integer> pq = createMinPQ();
            pq.add(1, 1);
            pq.add(2, 2);
            pq.add(3, 3);
            pq.removeMin();
            pq.removeMin();
            pq.removeMin();
            return pq;
        }
    }

    @Nested
    @DisplayName("Add 3 Increasing Priority")
    class Add3Increasing {
        int min = 1;

        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            ExtrinsicMinPQ<Integer> pq = createMinPQ();
            pq.add(1, 1);
            pq.add(2, 2);
            pq.add(3, 3);
            return pq;
        }

        @Test
        void isValid() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThat(pq).isValid();
        }

        @Test
        void size_returns3() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(3);
            assertThat(pq).isValid();
        }

        @Test
        void containsWithContainedItem_returnsTrue() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            boolean output = pq.contains(1);
            assertThat(output).isTrue();
            assertThat(pq).isValid();
        }

        @Test
        void containsWithNotContainedItem_returnsFalse() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            boolean output = pq.contains(412);
            assertThat(output).isFalse();
            assertThat(pq).isValid();
        }

        @Test
        void peekMin_returnsCorrectItem() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            int output = pq.peekMin();
            assertThat(output).isEqualTo(this.min);
            assertThat(pq).isValid();
        }

        @Test
        void removeMin_returnsCorrectItem() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            int output = pq.removeMin();
            assertThat(output).isEqualTo(this.min);
            assertThat(pq).isValid();
        }

        @Test
        void addNull_throwsIllegalArgument() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThatThrownBy(() -> pq.add(null, 15)).isInstanceOf(IllegalArgumentException.class);
            assertThat(pq).isValid();
        }

        @Test
        void addDuplicate_throwsIllegalArgument() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThatThrownBy(() -> pq.add(1, 15)).isInstanceOf(IllegalArgumentException.class);
            assertThat(pq).isValid();
        }
    }

    @Nested
    @DisplayName("Add 3 Decreasing Priority")
    class Add3Decreasing extends Add3Increasing {
        Add3Decreasing() {
            min = 3;
        }

        @Override
        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            ExtrinsicMinPQ<Integer> pq = createMinPQ();
            pq.add(1, 3);
            pq.add(2, 2);
            pq.add(3, 1);
            return pq;
        }
    }

    @Nested
    @DisplayName("Add 3 Arbitrary Priority")
    class Add3Arbitrary extends Add3Increasing {
        Add3Arbitrary() {
            min = 2;
        }

        @Override
        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            ExtrinsicMinPQ<Integer> pq = createMinPQ();
            pq.add(1, 3);
            pq.add(2, 1);
            pq.add(3, 2);
            return pq;
        }
    }

    @Nested
    @DisplayName("Add 3 Same Priority")
    class Add3Same {
        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            ExtrinsicMinPQ<Integer> pq = createMinPQ();
            pq.add(1, 7);
            pq.add(2, 7);
            pq.add(3, 7);
            return pq;
        }

        @Test
        void isValid() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThat(pq).isValid();
        }

        @Test
        void size_returns3() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(3);
            assertThat(pq).isValid();
        }

        @Test
        void containsWithContainedItem_returnsTrue() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            boolean output = pq.contains(1);
            assertThat(output).isTrue();
            assertThat(pq).isValid();
        }

        @Test
        void removeMinRepeatedly_returnsAllItems() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            List<Integer> output = removeAll(pq);
            assertThat(output).containsExactlyInAnyOrder(1, 2, 3);
            assertThat(pq).isValid();
        }
    }

    @Nested
    @DisplayName("Add 10 Increasing Priority")
    class Add10Increasing {
        Integer[] correctOrdering = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            ExtrinsicMinPQ<Integer> pq = createMinPQ();
            pq.add(1, 1);
            pq.add(2, 2);
            pq.add(3, 3);
            pq.add(4, 4);
            pq.add(5, 5);
            pq.add(6, 6);
            pq.add(7, 7);
            pq.add(8, 8);
            pq.add(9, 9);
            pq.add(10, 10);
            return pq;
        }

        @Test
        void isValid() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            assertThat(pq).isValid();
        }

        @Test
        void size_returns10() {
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            int output = pq.size();
            assertThat(output).isEqualTo(10);
            assertThat(pq).isValid();
        }

        @Test
        void removeMinRepeatedly_returnsItemsInCorrectOrder() {
            long start = System.currentTimeMillis();
            ExtrinsicMinPQ<Integer> pq = setUpMinPQ();
            List<Integer> output = removeAll(pq);
            System.out.println(output);
            assertThat(output).containsExactly(correctOrdering);
            assertThat(pq).isValid();
            long end = System.currentTimeMillis();
            System.out.println("Total time elapsed: " + (end - start) / 1000.0 +  " seconds.");
        }


    }

    @Nested
    @DisplayName("Add 10 Arbitrary Priority")
    class Add10Arbitrary extends Add10Increasing {
        Add10Arbitrary() {
            this.correctOrdering = new Integer[]{5, 8, 1, 4, 7, 9, 3, 6, 2, 10};
        }

        @Override
        ExtrinsicMinPQ<Integer> setUpMinPQ() {
            ExtrinsicMinPQ<Integer> pq = createMinPQ();
            pq.add(1, 3);
            pq.add(2, 9);
            pq.add(3, 7);
            pq.add(4, 4);
            pq.add(5, 1);
            pq.add(6, 8);
            pq.add(7, 5);
            pq.add(8, 2);
            pq.add(9, 6);
            pq.add(10, 10);
            return pq;
        }


    }

    protected <T extends Comparable<T>> List<T> removeAll(ExtrinsicMinPQ<T> pq) {
        return IntStream.range(0, pq.size()).mapToObj(i -> pq.removeMin()).collect(Collectors.toList());
    }
}
