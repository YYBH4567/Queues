import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 双端队列模板,鉴于要求各操作constant worst-case，应该写双向列表+首尾哨兵+size
 * 一个Node开销48B,那n个结点的deque需要48(n+2)+4字节也许就可以了，应该还算符合要求。
 *
 * @param <Item>
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     * 这里只能实现双向列表，不然基本完不成要求。
     *
     * @param <Item>
     */
    private static class Node<Item> {
        private Item item;
        /**
         * 指向前一个Node对象
         */
        private Node<Item> prev;
        /**
         * 指向后一个Node对象
         */
        private Node<Item> next;

        /**
         * 在结点之后新增一个值为item的结点
         *
         * @param item 结点的数值
         */
        void insertAsNext(Item item) {
            Node<Item> node = new Node<>(item, this, this.next);
            if (this.next != null) {
                this.next.prev = node;
            }
            this.next = node;
        }

        void insertAsPrev(Item item) {
            Node<Item> node = new Node<>(item, this.prev, this);
            if (this.prev != null) {
                this.prev.next = node;
            }
            this.prev = node;
        }

        Node(Item item, Node<Item> prev, Node<Item> next) {
            setItem(item);
            setPrev(prev);
            setNext(next);
        }

        Node() {
            this(null, null, null);
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public void setPrev(Node<Item> prev) {
            this.prev = prev;
        }

        public void setNext(Node<Item> next) {
            this.next = next;
        }
    }


    /**
     * 表示deque中现有结点数
     */
    private int n;

    public Deque() {
        n = 0;
        head = new Node<>();
        tail = new Node<>();
        head.next = tail;
        tail.prev = head;
    }

    private final Node<Item> head;

    private final Node<Item> tail;

    public int size() {
        return n;
    }

    private void checkNullItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("null argument insertion!");
        }
    }

    public void addFirst(Item item) {
        checkNullItem(item);
        ++n;
        head.insertAsNext(item);
    }

    private Item remove(Node<Item> pos) {
        if (isEmpty()) {
            throw new NoSuchElementException("empty deque already!");
        }
        if (pos == null) {
            throw new IllegalArgumentException("position cannot be null");
        }
        if (pos.next != null) {
            pos.next.prev = pos.prev;
        }
        if (pos.prev != null) {
            pos.prev.next = pos.next;
        }
        --n;
        return pos.item;
    }

    public void addLast(Item item) {
        checkNullItem(item);
        ++n;
        tail.insertAsPrev(item);
    }

    public Item removeFirst() {
        return remove(head.next);
    }

    public Item removeLast() {
        return remove(tail.prev);
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public static void main(String[] args) {
        Deque<Integer> test = new Deque<>();
        for (int num = 1; num <= 10; ++num) {
            if ((num & 1) == 1) {
                test.addFirst(num);
            } else {
                test.addLast(num);
            }
        }
        for (var each : test) {
            StdOut.printf("%d ", each);
        }
        System.out.println();
        while (!test.isEmpty()) {
            StdOut.println(test.removeFirst());
            StdOut.println(test.removeLast());
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current = head.next;

        @Override
        public boolean hasNext() {
            return current != null && current != tail;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("already at the end");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


}
