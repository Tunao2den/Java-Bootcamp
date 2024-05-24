/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SequentialSearchST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SeparateChainingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;
    private int n;
    private int m;
    private SequentialSearchST<Key, Value>[] st;

    public SeparateChainingHashST() {
        this(4);
    }

    public SeparateChainingHashST(int m) {
        this.m = m;
        this.st = new SequentialSearchST[m];
        for (int i = 0; i < m; ++i) {
            this.st[i] = new SequentialSearchST();
        }
    }

    private void resize(int chains) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < this.m; ++i) {
            for (Key key : this.st[i].keys()) {
                temp.put(key, this.st[i].get(key));
            }
        }
        this.m = temp.m;
        this.n = temp.n;
        this.st = temp.st;
    }

    private int hashTextbook(Key key) {
        return (key.hashCode() & Integer.MAX_VALUE) % this.m;
    }

    private int hash(Key key) {
        int h = key.hashCode();
        h ^= h >>> 20 ^ h >>> 12 ^ h >>> 7 ^ h >>> 4;
        return h & this.m - 1;
    }

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
        int i = this.hash(key);
        return this.st[i].get(key);
    }

    public void put(Key key, Value val) {
        int i;
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        if (val == null) {
            this.delete(key);
            return;
        }
        if (this.n >= 10 * this.m) {
            this.resize(2 * this.m);
        }
        if (!this.st[i = this.hash(key)].contains(key)) {
            ++this.n;
        }
        this.st[i].put(key, val);
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        int i = this.hash(key);
        if (this.st[i].contains(key)) {
            --this.n;
        }
        this.st[i].delete(key);
        if (this.m > 4 && this.n <= 2 * this.m) {
            this.resize(this.m / 2);
        }
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < this.m; ++i) {
            for (Key key : this.st[i].keys()) {
                queue.enqueue(key);
            }
        }
        return queue;
    }

    public static void main(String[] args) {
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
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
}

