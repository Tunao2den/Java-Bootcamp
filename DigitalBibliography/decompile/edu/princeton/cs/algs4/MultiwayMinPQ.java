/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MultiwayMinPQ<Key>
implements Iterable<Key> {
    private final int d;
    private int n;
    private int order;
    private Key[] keys;
    private final Comparator<Key> comp;

    public MultiwayMinPQ(int d) {
        if (d < 2) {
            throw new IllegalArgumentException("Dimension should be 2 or over");
        }
        this.d = d;
        this.order = 1;
        this.keys = new Comparable[d << 1];
        this.comp = new MyComparator();
    }

    public MultiwayMinPQ(Comparator<Key> comparator, int d) {
        if (d < 2) {
            throw new IllegalArgumentException("Dimension should be 2 or over");
        }
        this.d = d;
        this.order = 1;
        this.keys = new Comparable[d << 1];
        this.comp = comparator;
    }

    public MultiwayMinPQ(Key[] a, int d) {
        if (d < 2) {
            throw new IllegalArgumentException("Dimension should be 2 or over");
        }
        this.d = d;
        this.order = 1;
        this.keys = new Comparable[d << 1];
        this.comp = new MyComparator();
        for (Key key : a) {
            this.insert(key);
        }
    }

    public MultiwayMinPQ(Comparator<Key> comparator, Key[] a, int d) {
        if (d < 2) {
            throw new IllegalArgumentException("Dimension should be 2 or over");
        }
        this.d = d;
        this.order = 1;
        this.keys = new Comparable[d << 1];
        this.comp = comparator;
        for (Key key : a) {
            this.insert(key);
        }
    }

    public boolean isEmpty() {
        return this.n == 0;
    }

    public int size() {
        return this.n;
    }

    public void insert(Key key) {
        this.keys[this.n + this.d] = key;
        this.swim(this.n++);
        if (this.n == this.keys.length - this.d) {
            this.resize(this.getN(this.order + 1) + this.d);
            ++this.order;
        }
    }

    public Key minKey() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        return this.keys[this.d];
    }

    public Key delMin() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Priority queue is empty");
        }
        this.exch(0, --this.n);
        this.sink(0);
        Key min = this.keys[this.n + this.d];
        this.keys[this.n + this.d] = null;
        int number = this.getN(this.order - 2);
        if (this.order > 1 && this.n == number) {
            this.resize(number + (int)Math.pow(this.d, this.order - 1) + this.d);
            --this.order;
        }
        return min;
    }

    private boolean greater(int x, int y) {
        int i = x + this.d;
        int j = y + this.d;
        if (this.keys[i] == null) {
            return false;
        }
        if (this.keys[j] == null) {
            return true;
        }
        return this.comp.compare(this.keys[i], this.keys[j]) > 0;
    }

    private void exch(int x, int y) {
        int i = x + this.d;
        int j = y + this.d;
        Key swap = this.keys[i];
        this.keys[i] = this.keys[j];
        this.keys[j] = swap;
    }

    private int getN(int order) {
        return (1 - (int)Math.pow(this.d, order + 1)) / (1 - this.d);
    }

    private void swim(int i) {
        if (i > 0 && this.greater((i - 1) / this.d, i)) {
            this.exch(i, (i - 1) / this.d);
            this.swim((i - 1) / this.d);
        }
    }

    private void sink(int i) {
        int child = this.d * i + 1;
        if (child >= this.n) {
            return;
        }
        int min = this.minChild(i);
        while (min < this.n && this.greater(i, min)) {
            this.exch(i, min);
            i = min;
            min = this.minChild(i);
        }
    }

    private int minChild(int i) {
        int loBound = this.d * i + 1;
        int hiBound = this.d * i + this.d;
        int min = loBound;
        for (int cur = loBound; cur <= hiBound; ++cur) {
            if (cur >= this.n || !this.greater(min, cur)) continue;
            min = cur;
        }
        return min;
    }

    private void resize(int N) {
        Object[] array = new Comparable[N];
        for (int i = 0; i < Math.min(this.keys.length, array.length); ++i) {
            array[i] = this.keys[i];
            this.keys[i] = null;
        }
        this.keys = array;
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

    private class MyIterator
    implements Iterator<Key> {
        MultiwayMinPQ<Key> data;

        public MyIterator() {
            this.data = new MultiwayMinPQ(MultiwayMinPQ.this.comp, MultiwayMinPQ.this.d);
            this.data.keys = new Comparable[MultiwayMinPQ.this.keys.length];
            this.data.n = MultiwayMinPQ.this.n;
            for (int i = 0; i < MultiwayMinPQ.this.keys.length; ++i) {
                this.data.keys[i] = MultiwayMinPQ.this.keys[i];
            }
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

