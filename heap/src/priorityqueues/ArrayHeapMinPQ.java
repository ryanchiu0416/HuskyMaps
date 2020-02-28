package priorityqueues;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    // currently changed for HW6: original was <T extends Comparable<T>>

    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    static final int START_INDEX = 1;
    List<PriorityNode<T>> items;
    private Map<T, Integer> dict;

    public ArrayHeapMinPQ() {
        items = new ArrayList<>();
        dict = new HashMap<>();
        items.add(null);
    }

    // Here's a method stub that may be useful. Feel free to change or remove it, if you wish.
    // You'll probably want to add more helper methods like this one to make your code easier to read.
    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        PriorityNode<T> temp = items.get(a);
        items.set(a, items.get(b));
        items.set(b, temp);
        dict.put(items.get(a).getItem(), a);
        dict.put(items.get(b).getItem(), b);
    }

    // Percolate up if needed (check invariant violation - priority comparison)
    // used in add()
    private void percolateUp(int curr) {
        while (curr > 1 && items.get(curr).getPriority() < items.get(curr / 2).getPriority()) {
            swap(curr, curr / 2);
            curr /= 2;
        }
    }

    private void percolateDown(int curr) {
        // int curr = START_INDEX;
        for (int i = 2 * curr; i <= size(); i *= 2) {
            // find minimum among children
            if (i + 1 <= size() && items.get(i).getPriority() > items.get(i + 1).getPriority()) {
                i++;
            }
            // check if swap is needed
            if (items.get(curr).getPriority() > items.get(i).getPriority()) {
                swap(curr, i);
                curr = i;
            } else {
                break; // to exit loop (or just break)
            }
        }
    }

    /**
     * Adds an item with the given priority value.
     * Runs in O(log N) time (except when resizing).
     * @throws IllegalArgumentException if item is null or is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if (item == null || contains(item)) {
            throw new IllegalArgumentException();
        }
        // 1. add to the end of list
        items.add(new PriorityNode<>(item, priority));
        int index = size();
        dict.put(items.get(index).getItem(), index);
        // 2. percolate up (if needed - check invariant violation)
        percolateUp(index);
    }

    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        // use a BST to store T item - O(Log N)
        // a map might work too..?
        return dict.containsKey(item);
    }

    /**
     * Returns the item with the least-valued priority.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items.get(START_INDEX).getItem();
    }

    /**
     * Removes and returns the item with the least-valued priority.
     * Runs in O(log N) time (except when resizing).
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        // 1. remove and swap in last element in the list
        T result = items.get(START_INDEX).getItem();
        dict.remove(result);
        if (size() > START_INDEX) {
            items.set(START_INDEX, items.get(size()));
            dict.put(items.get(START_INDEX).getItem(), START_INDEX);
        }
        items.remove(size());
        // 2. percolate down
        percolateDown(START_INDEX);

        // 3. return removed item
        return result;
    }

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        // change the priority of the specified item at the index
        int index = dict.get(item);
        items.get(index).setPriority(priority);

        // need to determine whether to percolate up or down and percolate
        percolateUp(index);
        percolateDown(index);
    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    // it is also the index of the last element in the list.
    @Override
    public int size() {
        return items.size() - 1;
    }
}
