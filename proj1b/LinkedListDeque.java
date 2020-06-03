public class LinkedListDeque<T> implements Deque<T> {
    private int size;
    private Node<T> sentinel;

    private static class Node<T> {
        T item;
        Node<T> prev;
        Node<T> next;

        Node(T item, Node<T> prev, Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedListDeque() {
        sentinel = new Node<>(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node<T> x = new Node<>(item, sentinel, sentinel.next);
        sentinel.next.prev = x;
        sentinel.next = x;
        size++;
    }

    @Override
    public void addLast(T item) {
        Node<T> x = new Node<>(item, sentinel.prev, sentinel);
        sentinel.prev.next = x;
        sentinel.prev = x;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node<T> x = sentinel;
        while (x.next != sentinel) {
            System.out.print(x.next.item + " ");
            x = x.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node<T> x = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return x.item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node<T> x = sentinel.prev;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return x.item;
    }

    @Override
    public T get(int index) {
        Node<T> x = sentinel;
        while (x.next != sentinel) {
            if (index == 0) {
                return x.next.item;
            }
            x = x.next;
            index--;
        }
        return null;
    }

    public T getRecursive(int index) {
        return getRecursive(index, sentinel);
    }

    private T getRecursive(int index, Node<T> x) {
        if (x.next == sentinel) {
            return null;
        }
        if (index == 0) {
            return x.next.item;
        }
        return getRecursive(index - 1, x.next);
    }

}
