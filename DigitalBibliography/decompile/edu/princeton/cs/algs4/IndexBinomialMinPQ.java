/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexBinomialMinPQ<Key>
implements Iterable<Integer> {
    private Node<Key> head;
    private Node<Key>[] nodes;
    private int n;
    private final Comparator<Key> comparator;

    public IndexBinomialMinPQ(int N) {
        if (N < 0) {
            throw new IllegalArgumentException("Cannot create a priority queue of negative size");
        }
        this.comparator = new MyComparator();
        this.nodes = new Node[N];
        this.n = N;
    }

    public IndexBinomialMinPQ(int N, Comparator<Key> comparator) {
        if (N < 0) {
            throw new IllegalArgumentException("Cannot create a priority queue of negative size");
        }
        this.comparator = comparator;
        this.nodes = new Node[N];
        this.n = N;
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public boolean contains(int i) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        return this.nodes[i] != null;
    }

    public int size() {
        int result = 0;
        Node<Key> node = this.head;
        while (node != null) {
            if (node.order > 30) {
                throw new ArithmeticException("The number of elements cannot be evaluated, but the priority queue is still valid.");
            }
            int tmp = 1 << node.order;
            result |= tmp;
            node = node.sibling;
        }
        return result;
    }

    public void insert(int i, Key key) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        if (this.contains(i)) {
            throw new IllegalArgumentException("Specified index is already in the queue");
        }
        Node x = new Node();
        x.key = key;
        x.index = i;
        x.order = 0;
        this.nodes[i] = x;
        IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
        H.head = x;
        this.head = this.union(H).head;
    }

    public int minIndex() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        Node<Key> min = this.head;
        Node<Key> current = this.head;
        while (current.sibling != null) {
            min = this.greater(min.key, current.sibling.key) ? current.sibling : min;
            current = current.sibling;
        }
        return min.index;
    }

    public Key minKey() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        Node<Key> min = this.head;
        Node<Key> current = this.head;
        while (current.sibling != null) {
            min = this.greater(min.key, current.sibling.key) ? current.sibling : min;
            current = current.sibling;
        }
        return min.key;
    }

    public int delMin() {
        Node<Key> x;
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        Node<Key> min = this.eraseMin();
        Node<Key> node = x = min.child == null ? min : min.child;
        if (min.child != null) {
            min.child = null;
            Node<Key> prevx = null;
            Node nextx = x.sibling;
            while (nextx != null) {
                x.parent = null;
                x.sibling = prevx;
                prevx = x;
                x = nextx;
                nextx = nextx.sibling;
            }
            x.parent = null;
            x.sibling = prevx;
            IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
            H.head = x;
            this.head = this.union(H).head;
        }
        return min.index;
    }

    public Key keyOf(int i) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        if (!this.contains(i)) {
            throw new IllegalArgumentException("Specified index is not in the queue");
        }
        return this.nodes[i].key;
    }

    public void changeKey(int i, Key key) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        if (!this.contains(i)) {
            throw new IllegalArgumentException("Specified index is not in the queue");
        }
        if (this.greater(this.nodes[i].key, key)) {
            this.decreaseKey(i, key);
        } else {
            this.increaseKey(i, key);
        }
    }

    public void decreaseKey(int i, Key key) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("Specified index is not in the queue");
        }
        if (this.greater(key, this.nodes[i].key)) {
            throw new IllegalArgumentException("Calling with this argument would not decrease the key");
        }
        Node<Key> x = this.nodes[i];
        x.key = key;
        this.swim(i);
    }

    public void increaseKey(int i, Key key) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("Specified index is not in the queue");
        }
        if (this.greater(this.nodes[i].key, key)) {
            throw new IllegalArgumentException("Calling with this argument would not increase the key");
        }
        this.delete(i);
        this.insert(i, key);
    }

    public void delete(int i) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        if (!this.contains(i)) {
            throw new NoSuchElementException("Specified index is not in the queue");
        }
        this.toTheRoot(i);
        Node<Key> x = this.erase(i);
        if (x.child != null) {
            Node<Key> y = x;
            x = x.child;
            y.child = null;
            Node<Key> prevx = null;
            Node nextx = x.sibling;
            while (nextx != null) {
                x.parent = null;
                x.sibling = prevx;
                prevx = x;
                x = nextx;
                nextx = nextx.sibling;
            }
            x.parent = null;
            x.sibling = prevx;
            IndexBinomialMinPQ<Key> H = new IndexBinomialMinPQ<Key>();
            H.head = x;
            this.head = this.union(H).head;
        }
    }

    private boolean greater(Key n, Key m) {
        if (n == null) {
            return false;
        }
        if (m == null) {
            return true;
        }
        return this.comparator.compare(n, m) > 0;
    }

    private void exchange(Node<Key> x, Node<Key> y) {
        Object tempKey = x.key;
        x.key = y.key;
        y.key = tempKey;
        int tempInt = x.index;
        x.index = y.index;
        y.index = tempInt;
        this.nodes[x.index] = x;
        this.nodes[y.index] = y;
    }

    private void link(Node<Key> root1, Node<Key> root2) {
        root1.sibling = root2.child;
        root1.parent = root2;
        root2.child = root1;
        ++root2.order;
    }

    private void swim(int i) {
        Node<Key> x = this.nodes[i];
        Node parent = x.parent;
        if (parent != null && this.greater(parent.key, x.key)) {
            this.exchange(x, parent);
            this.swim(i);
        }
    }

    private void toTheRoot(int i) {
        Node<Key> x = this.nodes[i];
        Node parent = x.parent;
        if (parent != null) {
            this.exchange(x, parent);
            this.toTheRoot(i);
        }
    }

    private Node<Key> erase(int i) {
        Node<Key> reference = this.nodes[i];
        Node<Key> x = this.head;
        Node<Key> previous = null;
        while (x != reference) {
            previous = x;
            x = x.sibling;
        }
        previous.sibling = x.sibling;
        if (x == this.head) {
            this.head = this.head.sibling;
        }
        this.nodes[i] = null;
        return x;
    }

    private Node<Key> eraseMin() {
        Node<Key> min = this.head;
        Node<Key> previous = null;
        Node<Key> current = this.head;
        while (current.sibling != null) {
            if (this.greater(min.key, current.sibling.key)) {
                previous = current;
                min = current.sibling;
            }
            current = current.sibling;
        }
        previous.sibling = min.sibling;
        if (min == this.head) {
            this.head = min.sibling;
        }
        this.nodes[min.index] = null;
        return min;
    }

    private Node<Key> merge(Node<Key> h, Node<Key> x, Node<Key> y) {
        if (x == null && y == null) {
            return h;
        }
        h.sibling = x == null ? this.merge(y, null, y.sibling) : (y == null ? this.merge(x, x.sibling, null) : (x.order < y.order ? this.merge(x, x.sibling, y) : this.merge(y, x, y.sibling)));
        return h;
    }

    private IndexBinomialMinPQ<Key> union(IndexBinomialMinPQ<Key> heap) {
        Node<Key> x = this.head = this.merge((IndexBinomialMinPQ)this.new Node<Key>(), this.head, heap.head).sibling;
        Node<Key> prevx = null;
        Node nextx = x.sibling;
        while (nextx != null) {
            if (x.order < nextx.order || nextx.sibling != null && nextx.sibling.order == x.order) {
                prevx = x;
                x = nextx;
            } else if (this.greater(nextx.key, x.key)) {
                x.sibling = nextx.sibling;
                this.link(nextx, x);
            } else {
                if (prevx == null) {
                    this.head = nextx;
                } else {
                    prevx.sibling = nextx;
                }
                this.link(x, nextx);
                x = nextx;
            }
            nextx = x.sibling;
        }
        return this;
    }

    private IndexBinomialMinPQ() {
        this.comparator = null;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new MyIterator();
    }

    private class MyComparator
    implements Comparator<Key> {
        private MyComparator() {
        }

        @Override
        public int compare(Key key1, Key key2) {
            return ((Comparable)key1).compareTo(key2);
        }
    }

    private class Node<Key> {
        Key key;
        int order;
        int index;
        Node<Key> parent;
        Node<Key> child;
        Node<Key> sibling;

        private Node() {
        }
    }

    private class MyIterator
    implements Iterator<Integer> {
        IndexBinomialMinPQ<Key> data;

        public MyIterator() {
            this.data = new IndexBinomialMinPQ(IndexBinomialMinPQ.this.n, IndexBinomialMinPQ.this.comparator);
            this.data.head = this.clone(IndexBinomialMinPQ.this.head, null);
        }

        private Node<Key> clone(Node<Key> x, Node<Key> parent) {
            if (x == null) {
                return null;
            }
            Node node = new Node();
            node.index = x.index;
            node.key = x.key;
            this.data.nodes[node.index] = node;
            node.parent = parent;
            node.sibling = this.clone(x.sibling, parent);
            node.child = this.clone(x.child, node);
            return node;
        }

        @Override
        public boolean hasNext() {
            return !this.data.isEmpty();
        }

        @Override
        public Integer next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.data.delMin();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

