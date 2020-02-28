package deques;

public class LinkedDeque<T> extends AbstractDeque<T> {
    private int size;
    // IMPORTANT: Do not rename these fields or change their visibility.
    // We access these during grading to test your code.
    Node<T> front;
    Node<T> back; // may be the same as front, if you're using circular sentinel nodes

    public LinkedDeque() {
        size = 0;
        front = new Node<>(null);
        back = new Node<>(null);
        front.next = back;
        back.prev = front;
    }

    static class Node<T> {
        // IMPORTANT: Do not rename these fields or change their visibility.
        // We access these during grading to test your code.
        T value;
        Node<T> next;
        Node<T> prev;

        Node(T value) {
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        Node(T value, Node<T> prev, Node<T> next) {
            this(value);
            this.prev = prev;
            this.next = next;
        }
    }

    public void addFirst(T item) {
        front.next = new Node<>(item, front, front.next);
        front.next.next.prev = front.next;
        size += 1;
    }

    public void addLast(T item) {
        back.prev = new Node<>(item, back.prev, back);
        back.prev.prev.next = back.prev;
        size += 1;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = front.next.value;
        front.next = front.next.next;
        front.next.prev = front;
        size -= 1;
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = back.prev.value;
        back.prev = back.prev.prev;
        back.prev.next = back;
        size -= 1;
        return item;
    }

    public T get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<T> curr = front.next;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.value;
    }

    public int size() {
        return size;
    }
}
