/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class TrieST<Value> {
    private static final int R = 256;
    private Node root;
    private int n;

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        Node x = this.get(this.root, key, 0);
        if (x == null) {
            return null;
        }
        return (Value)x.val;
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return this.get(key) != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        char c = key.charAt(d);
        return this.get(x.next[c], key, d + 1);
    }

    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        if (val == null) {
            this.delete(key);
        } else {
            this.root = this.put(this.root, key, val, 0);
        }
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            if (x.val == null) {
                ++this.n;
            }
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = this.put(x.next[c], key, val, d + 1);
        return x;
    }

    public int size() {
        return this.n;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Iterable<String> keys() {
        return this.keysWithPrefix("");
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = this.get(this.root, prefix, 0);
        this.collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) {
            return;
        }
        if (x.val != null) {
            results.enqueue(prefix.toString());
        }
        for (char c = '\u0000'; c < '\u0100'; c = (char)(c + '\u0001')) {
            prefix.append(c);
            this.collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<String>();
        this.collect(this.root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) {
            return;
        }
        int d = prefix.length();
        if (d == pattern.length() && x.val != null) {
            results.enqueue(prefix.toString());
        }
        if (d == pattern.length()) {
            return;
        }
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = '\u0000'; ch < '\u0100'; ch = (char)(ch + '\u0001')) {
                prefix.append(ch);
                this.collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        } else {
            prefix.append(c);
            this.collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        }
        int length = this.longestPrefixOf(this.root, query, 0, -1);
        if (length == -1) {
            return null;
        }
        return query.substring(0, length);
    }

    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) {
            return length;
        }
        if (x.val != null) {
            length = d;
        }
        if (d == query.length()) {
            return length;
        }
        char c = query.charAt(d);
        return this.longestPrefixOf(x.next[c], query, d + 1, length);
    }

    public void delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        this.root = this.delete(this.root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        int c;
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            if (x.val != null) {
                --this.n;
            }
            x.val = null;
        } else {
            c = key.charAt(d);
            x.next[c] = this.delete(x.next[c], key, d + 1);
        }
        if (x.val != null) {
            return x;
        }
        for (c = 0; c < 256; ++c) {
            if (x.next[c] == null) continue;
            return x;
        }
        return null;
    }

    public static void main(String[] args) {
        TrieST<Integer> st = new TrieST<Integer>();
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
        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st.longestPrefixOf("quicksort"));
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

    private static class Node {
        private Object val;
        private Node[] next = new Node[256];

        private Node() {
        }
    }
}

