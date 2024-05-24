/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FibonacciMinPQ<Key>
implements Iterable<Key> {
    private Node head;
    private Node min;
    private int size;
    private final Comparator<Key> comp;
    private HashMap<Integer, Node> table = new HashMap();

    public FibonacciMinPQ(Comparator<Key> C) {
        this.comp = C;
    }

    public FibonacciMinPQ() {
        this.comp = new MyComparator();
    }

    public FibonacciMinPQ(Key[] a) {
        this.comp = new MyComparator();
        for (Key k : a) {
            this.insert(k);
        }
    }

    public FibonacciMinPQ(Comparator<Key> C, Key[] a) {
        this.comp = C;
        for (Key k : a) {
            this.insert(k);
        }
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void insert(Key key) {
        Node x = new Node();
        x.key = key;
        ++this.size;
        this.head = this.insert(x, this.head);
        this.min = this.min == null ? this.head : (this.greater(this.min.key, key) ? this.head : this.min);
    }

    public Key minKey() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return this.min.key;
    }

    public Key delMin() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        this.head = this.cut(this.min, this.head);
        Node x = this.min.child;
        Object key = this.min.key;
        this.min.key = null;
        if (x != null) {
            this.head = this.meld(this.head, x);
            this.min.child = null;
        }
        --this.size;
        if (!this.isEmpty()) {
            this.consolidate();
        } else {
            this.min = null;
        }
        return key;
    }

    public FibonacciMinPQ<Key> union(FibonacciMinPQ<Key> that) {
        this.head = this.meld(this.head, that.head);
        this.min = this.greater(this.min.key, that.min.key) ? that.min : this.min;
        this.size += that.size;
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
        root2.child = this.insert(root1, root2.child);
        ++root2.order;
    }

    private void consolidate() {
        this.table.clear();
        Node x = this.head;
        int maxOrder = 0;
        this.min = this.head;
        Node y = null;
        Node z = null;
        do {
            y = x;
            x = x.next;
            z = this.table.get(y.order);
            while (z != null) {
                this.table.remove(y.order);
                if (this.greater(y.key, z.key)) {
                    this.link(y, z);
                    y = z;
                } else {
                    this.link(z, y);
                }
                z = this.table.get(y.order);
            }
            this.table.put(y.order, y);
            if (y.order <= maxOrder) continue;
            maxOrder = y.order;
        } while (x != this.head);
        this.head = null;
        for (Node n : this.table.values()) {
            if (n == null) continue;
            this.min = this.greater(this.min.key, n.key) ? n : this.min;
            this.head = this.insert(n, this.head);
        }
    }

    private Node insert(Node x, Node head) {
        if (head == null) {
            x.prev = x;
            x.next = x;
        } else {
            head.prev.next = x;
            x.next = head;
            x.prev = head.prev;
            head.prev = x;
        }
        return x;
    }

    private Node cut(Node x, Node head) {
        if (x.next == x) {
            x.next = null;
            x.prev = null;
            return null;
        }
        x.next.prev = x.prev;
        x.prev.next = x.next;
        Node res = x.next;
        x.next = null;
        x.prev = null;
        if (head == x) {
            return res;
        }
        return head;
    }

    private Node meld(Node x, Node y) {
        if (x == null) {
            return y;
        }
        if (y == null) {
            return x;
        }
        x.prev.next = y.next;
        y.next.prev = x.prev;
        x.prev = y;
        y.next = x;
        return x;
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
        Node prev;
        Node next;
        Node child;

        private Node() {
        }
    }

    private class MyIterator
    implements Iterator<Key> {
        private FibonacciMinPQ<Key> copy;

        public MyIterator() {
            this.copy = new FibonacciMinPQ(FibonacciMinPQ.this.comp);
            this.insertAll(FibonacciMinPQ.this.head);
        }

        private void insertAll(Node head) {
            if (head == null) {
                return;
            }
            Node x = head;
            do {
                this.copy.insert(x.key);
                this.insertAll(x.child);
            } while ((x = x.next) != head);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return !this.copy.isEmpty();
        }

        @Override
        public Key next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.copy.delMin();
        }
    }
}

