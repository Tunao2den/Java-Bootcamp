/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedQueue<Item>
implements Iterable<Item> {
    private int n = 0;
    private Node first = null;
    private Node last = null;

    public LinkedQueue() {
        assert (this.check());
    }

    public boolean isEmpty() {
        return this.first == null;
    }

    public int size() {
        return this.n;
    }

    public Item peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        return this.first.item;
    }

    public void enqueue(Item item) {
        Node oldlast = this.last;
        this.last = new Node();
        this.last.item = item;
        this.last.next = null;
        if (this.isEmpty()) {
            this.first = this.last;
        } else {
            oldlast.next = this.last;
        }
        ++this.n;
        assert (this.check());
    }

    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue underflow");
        }
        Object item = this.first.item;
        this.first = this.first.next;
        --this.n;
        if (this.isEmpty()) {
            this.last = null;
        }
        assert (this.check());
        return item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item + " ");
        }
        return s.toString();
    }

    private boolean check() {
        if (this.n < 0) {
            return false;
        }
        if (this.n == 0) {
            if (this.first != null) {
                return false;
            }
            if (this.last != null) {
                return false;
            }
        } else if (this.n == 1) {
            if (this.first == null || this.last == null) {
                return false;
            }
            if (this.first != this.last) {
                return false;
            }
            if (this.first.next != null) {
                return false;
            }
        } else {
            int numberOfNodes;
            if (this.first == null || this.last == null) {
                return false;
            }
            if (this.first == this.last) {
                return false;
            }
            if (this.first.next == null) {
                return false;
            }
            if (this.last.next != null) {
                return false;
            }
            Node x = this.first;
            for (numberOfNodes = 0; x != null && numberOfNodes <= this.n; ++numberOfNodes) {
                x = x.next;
            }
            if (numberOfNodes != this.n) {
                return false;
            }
            Node lastNode = this.first;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
            if (this.last != lastNode) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator();
    }

    public static void main(String[] args) {
        LinkedQueue<String> queue = new LinkedQueue<String>();
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

    private class Node {
        private Item item;
        private Node next;

        private Node() {
        }
    }

    private class LinkedIterator
    implements Iterator<Item> {
        private Node current;

        private LinkedIterator() {
            this.current = LinkedQueue.this.first;
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

