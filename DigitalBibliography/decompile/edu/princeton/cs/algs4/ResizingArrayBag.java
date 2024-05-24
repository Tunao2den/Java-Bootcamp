/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizingArrayBag<Item>
implements Iterable<Item> {
    private static final int INIT_CAPACITY = 8;
    private Item[] a = new Object[8];
    private int n = 0;

    public boolean isEmpty() {
        return this.n == 0;
    }

    public int size() {
        return this.n;
    }

    private void resize(int capacity) {
        assert (capacity >= this.n);
        Object[] copy = new Object[capacity];
        for (int i = 0; i < this.n; ++i) {
            copy[i] = this.a[i];
        }
        this.a = copy;
    }

    public void add(Item item) {
        if (this.n == this.a.length) {
            this.resize(2 * this.a.length);
        }
        this.a[this.n++] = item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    public static void main(String[] args) {
        ResizingArrayBag<String> bag = new ResizingArrayBag<String>();
        bag.add("Hello");
        bag.add("World");
        bag.add("how");
        bag.add("are");
        bag.add("you");
        for (String s : bag) {
            StdOut.println(s);
        }
    }

    private class ArrayIterator
    implements Iterator<Item> {
        private int i = 0;

        private ArrayIterator() {
        }

        @Override
        public boolean hasNext() {
            return this.i < ResizingArrayBag.this.n;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return ResizingArrayBag.this.a[this.i++];
        }
    }
}

