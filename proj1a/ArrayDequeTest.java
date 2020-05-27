public class ArrayDequeTest {
    public static void main(String[] args) {
        ArrayDeque<Integer> d = new ArrayDeque<>();
        for (int i = 0; i < 25; i++) {
            if (i % 2 == 0) d.addFirst(i);
            else d.addLast(i);
        }
        d.printDeque();
    }
}