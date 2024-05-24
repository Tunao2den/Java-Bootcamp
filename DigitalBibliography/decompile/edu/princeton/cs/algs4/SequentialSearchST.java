/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SequentialSearchST<Key, Value> {
    private int n;
    private Node first;

    public int size() {
        return this.n;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public boolean contains(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return this.get(key) != null;
    }

    public Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Node x = this.first;
        while (x != null) {
            if (key.equals(x.key)) {
                return x.val;
            }
            x = x.next;
        }
        return null;
    }

    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        if (val == null) {
            this.delete(key);
            return;
        }
        Node x = this.first;
        while (x != null) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
            x = x.next;
        }
        this.first = new Node(key, val, this.first);
        ++this.n;
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        this.first = this.delete(this.first, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) {
            return null;
        }
        if (key.equals(x.key)) {
            --this.n;
            return x.next;
        }
        x.next = this.delete(x.next, key);
        return x;
    }

    public Iterable<Key> keys() {
        Queue queue = new Queue();
        Node x = this.first;
        while (x != null) {
            queue.enqueue(x.key);
            x = x.next;
        }
        return queue;
    }

    public static void main(String[] args) {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<String, Integer>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            st.put(key, i);
            ++i;
        }
        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }
    }

    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }
}

