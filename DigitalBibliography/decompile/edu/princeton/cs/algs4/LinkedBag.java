/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedBag<Item>
implements Iterable<Item> {
    private Node first = null;
    private int n = 0;

    public boolean isEmpty() {
        return this.first == null;
    }

    public int size() {
        return this.n;
    }

    public void add(Item item) {
        Node oldfirst = this.first;
        this.first = new Node();
        this.first.item = item;
        this.first.next = oldfirst;
        ++this.n;
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator();
    }

    public static void main(String[] args) {
        LinkedBag<String> bag = new LinkedBag<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            bag.add(item);
        }
        StdOut.println("size of bag = " + bag.size());
        for (String s : bag) {
            StdOut.println(s);
        }
    }

    private class Node {
        private Item item;
        private Node next;

        private Node() {
        }
    }

    private class LinkedIterator
    implements Iterator<Item> {
        private Node current;

        public LinkedIterator() {
            this.current = LinkedBag.this.first;
        }

        @Override
        public boolean hasNext() {
            return this.current != null;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Object item = this.current.item;
            this.current = this.current.next;
            return item;
        }
    }
}

