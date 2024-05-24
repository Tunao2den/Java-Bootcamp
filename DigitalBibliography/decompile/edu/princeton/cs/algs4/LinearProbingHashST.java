/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class LinearProbingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;
    private int n;
    private int m;
    private Key[] keys;
    private Value[] vals;

    public LinearProbingHashST() {
        this(4);
    }

    public LinearProbingHashST(int capacity) {
        this.m = capacity;
        this.n = 0;
        this.keys = new Object[this.m];
        this.vals = new Object[this.m];
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

    private int hashTextbook(Key key) {
        return (key.hashCode() & Integer.MAX_VALUE) % this.m;
    }

    private int hash(Key key) {
        int h = key.hashCode();
        h ^= h >>> 20 ^ h >>> 12 ^ h >>> 7 ^ h >>> 4;
        return h & this.m - 1;
    }

    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key, Value>(capacity);
        for (int i = 0; i < this.m; ++i) {
            if (this.keys[i] == null) continue;
            temp.put(this.keys[i], this.vals[i]);
        }
        this.keys = temp.keys;
        this.vals = temp.vals;
        this.m = temp.m;
    }

    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        if (val == null) {
            this.delete(key);
            return;
        }
        if (this.n >= this.m / 2) {
            this.resize(2 * this.m);
        }
        int i = this.hash(key);
        while (this.keys[i] != null) {
            if (this.keys[i].equals(key)) {
                this.vals[i] = val;
                return;
            }
            i = (i + 1) % this.m;
        }
        this.keys[i] = key;
        this.vals[i] = val;
        ++this.n;
    }

    public Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        int i = this.hash(key);
        while (this.keys[i] != null) {
            if (this.keys[i].equals(key)) {
                return this.vals[i];
            }
            i = (i + 1) % this.m;
        }
        return null;
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        if (!this.contains(key)) {
            return;
        }
        int i = this.hash(key);
        while (!key.equals(this.keys[i])) {
            i = (i + 1) % this.m;
        }
        this.keys[i] = null;
        this.vals[i] = null;
        i = (i + 1) % this.m;
        while (this.keys[i] != null) {
            Key keyToRehash = this.keys[i];
            Value valToRehash = this.vals[i];
            this.keys[i] = null;
            this.vals[i] = null;
            --this.n;
            this.put(keyToRehash, valToRehash);
            i = (i + 1) % this.m;
        }
        --this.n;
        if (this.n > 0 && this.n <= this.m / 8) {
            this.resize(this.m / 2);
        }
        assert (this.check());
    }

    public Iterable<Key> keys() {
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < this.m; ++i) {
            if (this.keys[i] == null) continue;
            queue.enqueue(this.keys[i]);
        }
        return queue;
    }

    private boolean check() {
        if (this.m < 2 * this.n) {
            System.err.println("Hash table size m = " + this.m + "; array size n = " + this.n);
            return false;
        }
        for (int i = 0; i < this.m; ++i) {
            if (this.keys[i] == null || this.get(this.keys[i]) == this.vals[i]) continue;
            System.err.println("get[" + this.keys[i] + "] = " + this.get(this.keys[i]) + "; vals[i] = " + this.vals[i]);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<String, Integer>();
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

