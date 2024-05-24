/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedStack<Item>
implements Iterable<Item> {
    private int n = 0;
    private Node first = null;

    public LinkedStack() {
        assert (this.check());
    }

    public boolean isEmpty() {
        return this.first == null;
    }

    public int size() {
        return this.n;
    }

    public void push(Item item) {
        Node oldfirst = this.first;
        this.first = new Node();
        this.first.item = item;
        this.first.next = oldfirst;
        ++this.n;
        assert (this.check());
    }

    public Item pop() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        Object item = this.first.item;
        this.first = this.first.next;
        --this.n;
        assert (this.check());
        return item;
    }

    public Item peek() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        return this.first.item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item + " ");
        }
        return s.toString();
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator();
    }

    private boolean check() {
        int numberOfNodes;
        if (this.n < 0) {
            return false;
        }
        if (this.n == 0) {
            if (this.first != null) {
                return false;
            }
        } else if (this.n == 1) {
            if (this.first == null) {
                return false;
            }
            if (this.first.next != null) {
                return false;
            }
        } else {
            if (this.first == null) {
                return false;
            }
            if (this.first.next == null) {
                return false;
            }
        }
        Node x = this.first;
        for (numberOfNodes = 0; x != null && numberOfNodes <= this.n; ++numberOfNodes) {
            x = x.next;
        }
        return numberOfNodes == this.n;
    }

    public static void main(String[] args) {
        LinkedStack<String> stack = new LinkedStack<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) {
                stack.push(item);
                continue;
            }
            if (stack.isEmpty()) continue;
            StdOut.print((String)stack.pop() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
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
            this.current = LinkedStack.this.first;
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

