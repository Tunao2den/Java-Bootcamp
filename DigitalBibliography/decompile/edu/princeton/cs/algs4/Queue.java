/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<Item>
implements Iterable<Item> {
    private Node<Item> first = null;
    private Node<Item> last = null;
    private int n = 0;

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
        Node<Item> oldlast = this.last;
        this.last = new Node();
        this.last.item = item;
        this.last.next = null;
        if (this.isEmpty()) {
            this.first = this.last;
        } else {
            oldlast.next = this.last;
        }
        ++this.n;
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
        return item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator(this.first);
    }

    public static void main(String[] args) {
        Queue<String> queue = new Queue<String>();
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

    private static class Node<Item> {
        private Item item;
        private Node<Item> next;

        private Node() {
        }
    }

    private class LinkedIterator
    implements Iterator<Item> {
        private Node<Item> current;

        public LinkedIterator(Node<Item> first) {
            this.current = first;
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

