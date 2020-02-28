package priorityqueues;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

/**
 * Use this PQ implementation if you don't want to use your ArrayHeapMinPQ.
 * This implementation is, in theory, slower than ArrayHeapMinPQ.
 */
public class DoubleMapMinPQ<T> implements ExtrinsicMinPQ<T> {
    private TreeMap<Double, Set<T>> priorityToItem = new TreeMap<>();
    private HashMap<T, Double> itemToPriority = new HashMap<>();

    public DoubleMapMinPQ() {}

    private static <K> K getItem(Set<K> s) {
        Iterator<K> i = s.iterator();
        return i.next();
    }

    @Override
    public void add(T item, double priority) {
        if (itemToPriority.containsKey(item)) {
            throw new IllegalArgumentException("Already contains " + item);
        }
        if (!priorityToItem.containsKey(priority)) {
            priorityToItem.put(priority, new HashSet<T>());
        }
        Set<T> itemsWithPriority = priorityToItem.get(priority);
        itemsWithPriority.add(item);
        itemToPriority.put(item, priority);
    }

    @Override
    public boolean contains(T item) {
        return itemToPriority.containsKey(item);
    }

    @Override
    public T peekMin() {
        if (itemToPriority.size() == 0) {
            throw new NoSuchElementException("PQ is empty.");
        }
        Set<T> itemsWithLowestPriority = priorityToItem.get(priorityToItem.firstKey());
        return getItem(itemsWithLowestPriority);
    }

    @Override
    public T removeMin() {
        if (itemToPriority.size() == 0) {
            throw new NoSuchElementException("PQ is empty.");
        }

        double lowestPriority = priorityToItem.firstKey();

        Set<T> itemsWithLowestPriority = priorityToItem.get(lowestPriority);
        T item = getItem(itemsWithLowestPriority);

        itemsWithLowestPriority.remove(item);
        if (itemsWithLowestPriority.size() == 0) {
            priorityToItem.remove(lowestPriority);
        }
        itemToPriority.remove(item);
        return item;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new IllegalArgumentException(item + " not in PQ.");
        }

        double oldP = itemToPriority.get(item);
        Set<T> itemsWithOldPriority = priorityToItem.get(oldP);
        itemsWithOldPriority.remove(item);

        if (itemsWithOldPriority.size() == 0) {
            priorityToItem.remove(oldP);
        }

        itemToPriority.remove(item);
        add(item, priority);
    }

    @Override
    public int size() {
        return itemToPriority.size();
    }
}
