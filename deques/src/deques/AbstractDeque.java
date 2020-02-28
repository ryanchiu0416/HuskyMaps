package deques;

import java.util.AbstractQueue;
import java.util.Iterator;

/**
 * Provides default implementations of all Queue methods using Deque methods.
 */
public abstract class AbstractDeque<T> extends AbstractQueue<T> implements Deque<T> {
    @Override
    public boolean offer(T t) {
        addFirst(t);
        return true;
    }

    @Override
    public T poll() {
        return removeFirst();
    }

    @Override
    public T peek() {
        return get(0);
    }

    /**
     * A basic iterator that uses the get method.
     * May not be very efficient, depending on the implementation.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < AbstractDeque.this.size();
            }

            @Override
            public T next() {
                return AbstractDeque.this.get(i++);
            }
        };
    }
}
