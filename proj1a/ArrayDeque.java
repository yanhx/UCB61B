public class ArrayDeque<T> {
    private int size, head, tail;
    private T[] items;
    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[10];
        head = 0;
        tail = 0;
    }
    public ArrayDeque(ArrayDeque<T> other) {

    }

    private void resize(int len) {
        T[] newItems = (T[]) new Object[len];
        int i = (head + 1) % items.length;
        int j = 0;
        for (; i != tail; j++) {
            newItems[j] = items[i];
            i = (i + 1) % items.length;
        }
        items = newItems;
        head = items.length-1;
        tail = j;
    }
    public void addFirst(T item){
        items[head] = item;
        head = (head - 1 + items.length) % items.length;
        if (size == 0)
            tail = (tail + 1) % items.length;
        size++;
        if(head == tail)
            resize(items.length * 2);
    }

    public void addLast(T item) {
        items[tail] = item;
        tail = (tail + 1) % items.length;
        if (size == 0)
            head = (head - 1 + items.length) % items.length;
        size++;
        if (head == tail)
            resize(items.length * 2);
    }

    public boolean isEmpty() {
        return size==0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for(int i = (head + 1) % items.length;i != tail;i = (i + 1) % items.length)
            System.out.print(items[i] + " ");
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty())
            return null;
        T ret = items[head];
        items[head] = null;
        head = (head + 1) % items.length;
        size--;
        if (size == 0)
            tail = (tail - 1 + items.length) % items.length;
        if (size < items.length / 4)
            resize(items.length / 2);
        return ret;
    }

    public T removeLast() {
        if (isEmpty())
            return null;
        T ret = items[tail];
        items[tail] = null;
        tail = (tail - 1 + items.length) % items.length;
        size--;
        if (size == 0)
            head = (head + 1) % items.length;
        if (size < items.length / 4)
            resize(items.length / 2);
        return ret;
    }

    public T get(int index) {
        if (index >= size)
            return null;
        return items[(head + index + 1) % items.length];
    }


}
