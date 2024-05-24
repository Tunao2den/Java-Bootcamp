/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResizingArrayQueue<Item>
implements Iterable<Item> {
    private static final int INIT_CAPACITY = 8;
    private Item[] q = new Object[8];
    private int n = 0;
    private int first = 0;
    private int last = 0;

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
            copy[i] = this.q[(this.first + i) % this.q.length];
        }
        this.q = copy;
        this.first = 0;
        this.last = this.n;
    }

    public void enqueue(Item item) {
        if (this.n == this.q.length) {
            this.resize(2 * this.q.length);
        }
        this.q[this.last++] = item;
        if (this.last == this.q.length) {
            this.last = 0;
        }
        ++this.n;
    }

    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        Item item = this.q[this.first];
        this.q[this.first] = null;
        --this.n;
        ++this.first;
        if (this.first == this.q.length) {
            this.first = 0;
        }
        if (this.n > 0 && this.n == this.q.length / 4) {
            this.resize(this.q.length / 2);
        }
        return item;
    }

    public Item peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        return this.q[this.first];
    }

    @Override
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    public static void main(String[] args) {
        ResizingArrayQueue<String> queue = new ResizingArrayQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                queue.enqueue(item);
                continue;
            }
            if (queue.isEmpty()) continue;
            StdOut.print((String)queue.dequeue() + " ");
        }
        StdOut.println("(" + queue.size() + " left on queue)");
    }

    private class ArrayIterator
    implements Iterator<Item> {
        private int i = 0;

        private ArrayIterator() {
        }

        @Override
        public boolean hasNext() {
            return this.i < ResizingArrayQueue.this.n;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Object item = ResizingArrayQueue.this.q[(this.i + ResizingArrayQueue.this.first) % ResizingArrayQueue.this.q.length];
            ++this.i;
            return item;
        }
    }
}

