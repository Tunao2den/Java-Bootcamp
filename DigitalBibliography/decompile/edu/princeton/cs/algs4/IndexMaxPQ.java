/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IndexMaxPQ<Key extends Comparable<Key>>
implements Iterable<Integer> {
    private int maxN;
    private int n;
    private int[] pq;
    private int[] qp;
    private Key[] keys;

    public IndexMaxPQ(int maxN) {
        if (maxN < 0) {
            throw new IllegalArgumentException();
        }
        this.maxN = maxN;
        this.n = 0;
        this.keys = new Comparable[maxN + 1];
        this.pq = new int[maxN + 1];
        this.qp = new int[maxN + 1];
        for (int i = 0; i <= maxN; ++i) {
            this.qp[i] = -1;
        }
    }

    public boolean isEmpty() {
        return this.n == 0;
    }

    public boolean contains(int i) {
        this.validateIndex(i);
        return this.qp[i] != -1;
    }

    public int size() {
        return this.n;
    }

    public void insert(int i, Key key) {
        this.validateIndex(i);
        if (this.contains(i)) {
            throw new IllegalArgumentException("index is already in the priority queue");
        }
        this.qp[i] = ++this.n;
        this.pq[this.n] = i;
        this.keys[i] = key;
        this.swim(this.n);
    }

    public int maxIndex() {
        if (this.n == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        return this.pq[1];
    }

    public Key maxKey() {
        if (this.n == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        return this.keys[this.pq[1]];
    }

    public int delMax() {
        if (this.n == 0) {
            throw new NoSuchElementException("Priority queue underflow");
        }
        int max = this.pq[1];
        this.exch(1, this.n--);
        this.sink(1);
        assert (this.pq[this.n + 1] == max);
        this.qp[max] = -1;
        this.keys[max] = null;
        this.pq[this.n + 1] = -1;
        return max;
    }

    public Key keyOf(int i) {
        this.validateIndex(i);
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        return this.keys[i];
    }

    public void changeKey(int i, Key key) {
        this.validateIndex(i);
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        this.keys[i] = key;
        this.swim(this.qp[i]);
        this.sink(this.qp[i]);
    }

    @Deprecated
    public void change(int i, Key key) {
        this.validateIndex(i);
        this.changeKey(i, key);
    }

    public void increaseKey(int i, Key key) {
        this.validateIndex(i);
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        if (this.keys[i].compareTo(key) == 0) {
            throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
        }
        if (this.keys[i].compareTo(key) > 0) {
            throw new IllegalArgumentException("Calling increaseKey() with a key that is strictly less than the key in the priority queue");
        }
        this.keys[i] = key;
        this.swim(this.qp[i]);
    }

    public void decreaseKey(int i, Key key) {
        this.validateIndex(i);
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        if (this.keys[i].compareTo(key) == 0) {
            throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
        }
        if (this.keys[i].compareTo(key) < 0) {
            throw new IllegalArgumentException("Calling decreaseKey() with a key that is strictly greater than the key in the priority queue");
        }
        this.keys[i] = key;
        this.sink(this.qp[i]);
    }

    public void delete(int i) {
        this.validateIndex(i);
        if (!this.contains(i)) {
            throw new NoSuchElementException("index is not in the priority queue");
        }
        int index = this.qp[i];
        this.exch(index, this.n--);
        this.swim(index);
        this.sink(index);
        this.keys[i] = null;
        this.qp[i] = -1;
    }

    private void validateIndex(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("index is negative: " + i);
        }
        if (i >= this.maxN) {
            throw new IllegalArgumentException("index >= capacity: " + i);
        }
    }

    private boolean less(int i, int j) {
        return this.keys[this.pq[i]].compareTo(this.keys[this.pq[j]]) < 0;
    }

    private void exch(int i, int j) {
        int swap = this.pq[i];
        this.pq[i] = this.pq[j];
        this.pq[j] = swap;
        this.qp[this.pq[i]] = i;
        this.qp[this.pq[j]] = j;
    }

    private void swim(int k) {
        while (k > 1 && this.less(k / 2, k)) {
            this.exch(k, k / 2);
            k /= 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= this.n) {
            int j = 2 * k;
            if (j < this.n && this.less(j, j + 1)) {
                ++j;
            }
            if (!this.less(k, j)) break;
            this.exch(k, j);
            k = j;
        }
    }

    @Override
    public Iterator<Integer> iterator() {
        return new HeapIterator();
    }

    public static void main(String[] args) {
        int i3;
        String[] strings = new String[]{"it", "was", "the", "best", "of", "times", "it", "was", "the", "worst"};
        IndexMaxPQ<Object> pq = new IndexMaxPQ<Object>(strings.length);
        for (int i2 = 0; i2 < strings.length; ++i2) {
            pq.insert(i2, strings[i2]);
        }
        for (int i3 : pq) {
            StdOut.println(i3 + " " + strings[i3]);
        }
        StdOut.println();
        for (int i4 = 0; i4 < strings.length; ++i4) {
            if (StdRandom.bernoulli(0.5)) {
                pq.increaseKey(i4, strings[i4] + strings[i4]);
                continue;
            }
            pq.decreaseKey(i4, strings[i4].substring(0, 1));
        }
        while (!pq.isEmpty()) {
            String key = (String)pq.maxKey();
            i3 = pq.delMax();
            StdOut.println(i3 + " " + key);
        }
        StdOut.println();
        for (int i5 = 0; i5 < strings.length; ++i5) {
            pq.insert(i5, strings[i5]);
        }
        int[] perm = new int[strings.length];
        for (i3 = 0; i3 < strings.length; ++i3) {
            perm[i3] = i3;
        }
        StdRandom.shuffle(perm);
        for (i3 = 0; i3 < perm.length; ++i3) {
            String key = (String)pq.keyOf(perm[i3]);
            pq.delete(perm[i3]);
            StdOut.println(perm[i3] + " " + key);
        }
    }

    private class HeapIterator
    implements Iterator<Integer> {
        private IndexMaxPQ<Key> copy;

        public HeapIterator() {
            this.copy = new IndexMaxPQ(IndexMaxPQ.this.pq.length - 1);
            for (int i = 1; i <= IndexMaxPQ.this.n; ++i) {
                this.copy.insert(IndexMaxPQ.this.pq[i], IndexMaxPQ.this.keys[IndexMaxPQ.this.pq[i]]);
            }
        }

        @Override
        public boolean hasNext() {
            return !this.copy.isEmpty();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Integer next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.copy.delMax();
        }
    }
}

