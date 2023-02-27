import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 要求除
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;

    private int capacity;

    private Item[] items;

    /**
     * 一开始先预留一个容量，随后扩容一次就翻倍
     */
    public RandomizedQueue() {
        size = 0;
        capacity = 1;
        items = (Item[]) new Object[capacity];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("empty item enqueue");
        }
        items[size++] = item;
        if (size == capacity) {
            resize(capacity << 1);
        }
    }

    private void resize(int newCapacity) {
        capacity = newCapacity;
        Item[] newItems = (Item[]) new Object[capacity];
        if (size >= 0) System.arraycopy(items, 0, newItems, 0, size);
        items = newItems;
    }

    private int randomInt() {
        return StdRandom.uniformInt(size);
    }

    public Item dequeue() {
        checkEmpty();
        int lastIndex = size - 1;
        int index = randomInt();
        Item item = items[index];
        items[index] = items[lastIndex];
        items[lastIndex] = null;
        --size;
        if (size <= (capacity >>> 2)) {
            resize(capacity >>> 1);
        }
        return item;
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("queue already empty");
        }
    }

    public Item sample() {
        checkEmpty();
        return items[randomInt()];
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final RandomizedQueue<Item> copy;

        public RandomizedQueueIterator() {
            copy = new RandomizedQueue<>();
            for (Item item : items) {
                if (item != null)
                    copy.enqueue(item);
            }
        }

        @Override
        public boolean hasNext() {
            return copy.size() > 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("iteration already done");
            }
            return copy.dequeue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> test = new RandomizedQueue<>();
        for (int num = 1; num <= 25; ++num) {
            test.enqueue(num);
        }
        for (var num : test) {
            StdOut.printf("%d ", num);
        }
        StdOut.println();
        while (!test.isEmpty()) {
            StdOut.printf("%d ", test.dequeue());
        }
    }
}
