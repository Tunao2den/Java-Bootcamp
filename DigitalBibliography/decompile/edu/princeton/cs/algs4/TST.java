/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class TST<Value> {
    private int n;
    private Node<Value> root;

    public int size() {
        return this.n;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return this.get(key) != null;
    }

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }
        Node<Value> x = this.get(this.root, key, 0);
        if (x == null) {
            return null;
        }
        return x.val;
    }

    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }
        char c = key.charAt(d);
        if (c < x.c) {
            return this.get(x.left, key, d);
        }
        if (c > x.c) {
            return this.get(x.right, key, d);
        }
        if (d < key.length() - 1) {
            return this.get(x.mid, key, d + 1);
        }
        return x;
    }

    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        }
        if (!this.contains(key)) {
            ++this.n;
        } else if (val == null) {
            --this.n;
        }
        this.root = this.put(this.root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) {
            x.left = this.put(x.left, key, val, d);
        } else if (c > x.c) {
            x.right = this.put(x.right, key, val, d);
        } else if (d < key.length() - 1) {
            x.mid = this.put(x.mid, key, val, d + 1);
        } else {
            x.val = val;
        }
        return x;
    }

    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        }
        if (query.length() == 0) {
            return null;
        }
        int length = 0;
        Node<Value> x = this.root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if (c < x.c) {
                x = x.left;
                continue;
            }
            if (c > x.c) {
                x = x.right;
                continue;
            }
            ++i;
            if (x.val != null) {
                length = i;
            }
            x = x.mid;
        }
        return query.substring(0, length);
    }

    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        this.collect(this.root, new StringBuilder(), queue);
        return queue;
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        }
        Queue<String> queue = new Queue<String>();
        Node<Value> x = this.get(this.root, prefix, 0);
        if (x == null) {
            return queue;
        }
        if (x.val != null) {
            queue.enqueue(prefix);
        }
        this.collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) {
            return;
        }
        this.collect(x.left, prefix, queue);
        if (x.val != null) {
            queue.enqueue(prefix.toString() + x.c);
        }
        this.collect(x.mid, prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        this.collect(x.right, prefix, queue);
    }

    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue<String>();
        this.collect(this.root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }

    private void collect(Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x == null) {
            return;
        }
        char c = pattern.charAt(i);
        if (c == '.' || c < x.c) {
            this.collect(x.left, prefix, i, pattern, queue);
        }
        if (c == '.' || c == x.c) {
            if (i == pattern.length() - 1 && x.val != null) {
                queue.enqueue(prefix.toString() + x.c);
            }
            if (i < pattern.length() - 1) {
                this.collect(x.mid, prefix.append(x.c), i + 1, pattern, queue);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (c == '.' || c > x.c) {
            this.collect(x.right, prefix, i, pattern, queue);
        }
    }

    public static void main(String[] args) {
        TST<Integer> st = new TST<Integer>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            st.put(key, i);
            ++i;
        }
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }
        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestPrefixOf("shellsort"));
        StdOut.println();
        StdOut.println("longestPrefixOf(\"shell\"):");
        StdOut.println(st.longestPrefixOf("shell"));
        StdOut.println();
        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor")) {
            StdOut.println(s);
        }
        StdOut.println();
        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st.keysThatMatch(".he.l.")) {
            StdOut.println(s);
        }
    }

    private static class Node<Value> {
        private char c;
        private Node<Value> left;
        private Node<Value> mid;
        private Node<Value> right;
        private Value val;

        private Node() {
        }
    }
}

