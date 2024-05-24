/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class BinomialMinPQ<Key>
implements Iterable<Key> {
    private Node head;
    private final Comparator<Key> comp;

    public BinomialMinPQ() {
        this.comp = new MyComparator();
    }

    public BinomialMinPQ(Comparator<Key> C) {
        this.comp = C;
    }

    public BinomialMinPQ(Key[] a) {
        this.comp = new MyComparator();
        for (Key k : a) {
            this.insert(k);
        }
    }

    public BinomialMinPQ(Comparator<Key> C, Key[] a) {
        this.comp = C;
        for (Key k : a) {
            this.insert(k);
        }
    }

    public boolean isEmpty() {
        return this.head == null;
    }

    public int size() {
        int result = 0;
        Node node = this.head;
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

    public void insert(Key key) {
        Node x = new Node();
        x.key = key;
        x.order = 0;
        BinomialMinPQ<Key> H = new BinomialMinPQ<Key>();
        H.head = x;
        this.head = this.union(H).head;
    }

    public Key minKey() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        Node min = this.head;
        Node current = this.head;
        while (current.sibling != null) {
            min = this.greater(min.key, current.sibling.key) ? current : min;
            current = current.sibling;
        }
        return min.key;
    }

    public Key delMin() {
        Node x;
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        Node min = this.eraseMin();
        Node node = x = min.child == null ? min : min.child;
        if (min.child != null) {
            min.child = null;
            Node prevx = null;
            Node nextx = x.sibling;
            while (nextx != null) {
                x.sibling = prevx;
                prevx = x;
                x = nextx;
                nextx = nextx.sibling;
            }
            x.sibling = prevx;
            BinomialMinPQ<Key> H = new BinomialMinPQ<Key>();
            H.head = x;
            this.head = this.union(H).head;
        }
        return min.key;
    }

    public BinomialMinPQ<Key> union(BinomialMinPQ<Key> heap) {
        if (heap == null) {
            throw new IllegalArgumentException("Cannot merge a Binomial Heap with null");
        }
        Node x = this.head = this.merge((Node)(BinomialMinPQ)this.new Node(), (Node)this.head, (Node)heap.head).sibling;
        Node prevx = null;
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

    private boolean greater(Key n, Key m) {
        if (n == null) {
            return false;
        }
        if (m == null) {
            return true;
        }
        return this.comp.compare(n, m) > 0;
    }

    private void link(Node root1, Node root2) {
        root1.sibling = root2.child;
        root2.child = root1;
        ++root2.order;
    }

    private Node eraseMin() {
        Node min = this.head;
        Node previous = null;
        Node current = this.head;
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
        return min;
    }

    private Node merge(Node h, Node x, Node y) {
        if (x == null && y == null) {
            return h;
        }
        h.sibling = x == null ? this.merge(y, null, y.sibling) : (y == null ? this.merge(x, x.sibling, null) : (x.order < y.order ? this.merge(x, x.sibling, y) : this.merge(y, x, y.sibling)));
        return h;
    }

    @Override
    public Iterator<Key> iterator() {
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

    private class Node {
        Key key;
        int order;
        Node child;
        Node sibling;

        private Node() {
        }
    }

    private class MyIterator
    implements Iterator<Key> {
        BinomialMinPQ<Key> data;

        public MyIterator() {
            this.data = new BinomialMinPQ(BinomialMinPQ.this.comp);
            this.data.head = this.clone(BinomialMinPQ.this.head, null);
        }

        private Node clone(Node x, Node parent) {
            if (x == null) {
                return null;
            }
            Node node = new Node();
            node.key = x.key;
            node.sibling = this.clone(x.sibling, parent);
            node.child = this.clone(x.child, node);
            return node;
        }

        @Override
        public boolean hasNext() {
            return !this.data.isEmpty();
        }

        @Override
        public Key next() {
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

