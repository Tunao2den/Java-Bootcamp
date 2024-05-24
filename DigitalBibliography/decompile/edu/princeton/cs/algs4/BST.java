/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;

public class BST<Key extends Comparable<Key>, Value> {
    private Node root;

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.size(this.root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    public boolean contains(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        }
        return this.get(key) != null;
    }

    public Value get(Key key) {
        return this.get(this.root, key);
    }

    private Value get(Node x, Key key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with a null key");
        }
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return this.get(x.left, key);
        }
        if (cmp > 0) {
            return this.get(x.right, key);
        }
        return x.val;
    }

    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        if (val == null) {
            this.delete(key);
            return;
        }
        this.root = this.put(this.root, key, val);
        assert (this.check());
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) {
            return new Node(this, key, val, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = this.put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = this.put(x.right, key, val);
        } else {
            x.val = val;
        }
        x.size = 1 + this.size(x.left) + this.size(x.right);
        return x;
    }

    public void deleteMin() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Symbol table underflow");
        }
        this.root = this.deleteMin(this.root);
        assert (this.check());
    }

    private Node deleteMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = this.deleteMin(x.left);
        x.size = this.size(x.left) + this.size(x.right) + 1;
        return x;
    }

    public void deleteMax() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Symbol table underflow");
        }
        this.root = this.deleteMax(this.root);
        assert (this.check());
    }

    private Node deleteMax(Node x) {
        if (x.right == null) {
            return x.left;
        }
        x.right = this.deleteMax(x.right);
        x.size = this.size(x.left) + this.size(x.right) + 1;
        return x;
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("calls delete() with a null key");
        }
        this.root = this.delete(this.root, key);
        assert (this.check());
    }

    private Node delete(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = this.delete(x.left, key);
        } else if (cmp > 0) {
            x.right = this.delete(x.right, key);
        } else {
            if (x.right == null) {
                return x.left;
            }
            if (x.left == null) {
                return x.right;
            }
            Node t = x;
            x = this.min(t.right);
            x.right = this.deleteMin(t.right);
            x.left = t.left;
        }
        x.size = this.size(x.left) + this.size(x.right) + 1;
        return x;
    }

    public Key min() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("calls min() with empty symbol table");
        }
        return this.min((Node)this.root).key;
    }

    private Node min(Node x) {
        if (x.left == null) {
            return x;
        }
        return this.min(x.left);
    }

    public Key max() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("calls max() with empty symbol table");
        }
        return this.max((Node)this.root).key;
    }

    private Node max(Node x) {
        if (x.right == null) {
            return x;
        }
        return this.max(x.right);
    }

    public Key floor(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to floor() is null");
        }
        if (this.isEmpty()) {
            throw new NoSuchElementException("calls floor() with empty symbol table");
        }
        Node x = this.floor(this.root, key);
        if (x == null) {
            throw new NoSuchElementException("argument to floor() is too small");
        }
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        if (cmp < 0) {
            return this.floor(x.left, key);
        }
        Node t = this.floor(x.right, key);
        if (t != null) {
            return t;
        }
        return x;
    }

    public Key floor2(Key key) {
        Key x = this.floor2(this.root, key, null);
        if (x == null) {
            throw new NoSuchElementException("argument to floor() is too small");
        }
        return x;
    }

    private Key floor2(Node x, Key key, Key best) {
        if (x == null) {
            return best;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return this.floor2(x.left, key, best);
        }
        if (cmp > 0) {
            return this.floor2(x.right, key, x.key);
        }
        return x.key;
    }

    public Key ceiling(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to ceiling() is null");
        }
        if (this.isEmpty()) {
            throw new NoSuchElementException("calls ceiling() with empty symbol table");
        }
        Node x = this.ceiling(this.root, key);
        if (x == null) {
            throw new NoSuchElementException("argument to ceiling() is too large");
        }
        return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            return x;
        }
        if (cmp < 0) {
            Node t = this.ceiling(x.left, key);
            if (t != null) {
                return t;
            }
            return x;
        }
        return this.ceiling(x.right, key);
    }

    public Key select(int rank) {
        if (rank < 0 || rank >= this.size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + rank);
        }
        return this.select(this.root, rank);
    }

    private Key select(Node x, int rank) {
        if (x == null) {
            return null;
        }
        int leftSize = this.size(x.left);
        if (leftSize > rank) {
            return this.select(x.left, rank);
        }
        if (leftSize < rank) {
            return this.select(x.right, rank - leftSize - 1);
        }
        return x.key;
    }

    public int rank(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to rank() is null");
        }
        return this.rank(key, this.root);
    }

    private int rank(Key key, Node x) {
        if (x == null) {
            return 0;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return this.rank(key, x.left);
        }
        if (cmp > 0) {
            return 1 + this.size(x.left) + this.rank(key, x.right);
        }
        return this.size(x.left);
    }

    public Iterable<Key> keys() {
        if (this.isEmpty()) {
            return new Queue();
        }
        return this.keys(this.min(), this.max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) {
            throw new IllegalArgumentException("first argument to keys() is null");
        }
        if (hi == null) {
            throw new IllegalArgumentException("second argument to keys() is null");
        }
        Queue queue = new Queue();
        this.keys(this.root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) {
            return;
        }
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) {
            this.keys(x.left, queue, lo, hi);
        }
        if (cmplo <= 0 && cmphi >= 0) {
            queue.enqueue(x.key);
        }
        if (cmphi > 0) {
            this.keys(x.right, queue, lo, hi);
        }
    }

    public int size(Key lo, Key hi) {
        if (lo == null) {
            throw new IllegalArgumentException("first argument to size() is null");
        }
        if (hi == null) {
            throw new IllegalArgumentException("second argument to size() is null");
        }
        if (lo.compareTo(hi) > 0) {
            return 0;
        }
        if (this.contains(hi)) {
            return this.rank(hi) - this.rank(lo) + 1;
        }
        return this.rank(hi) - this.rank(lo);
    }

    public int height() {
        return this.height(this.root);
    }

    private int height(Node x) {
        if (x == null) {
            return -1;
        }
        return 1 + Math.max(this.height(x.left), this.height(x.right));
    }

    public Iterable<Key> levelOrder() {
        Queue keys = new Queue();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(this.root);
        while (!queue.isEmpty()) {
            Node x = (Node)queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    private boolean check() {
        if (!this.isBST()) {
            StdOut.println("Not in symmetric order");
        }
        if (!this.isSizeConsistent()) {
            StdOut.println("Subtree counts not consistent");
        }
        if (!this.isRankConsistent()) {
            StdOut.println("Ranks not consistent");
        }
        return this.isBST() && this.isSizeConsistent() && this.isRankConsistent();
    }

    private boolean isBST() {
        return this.isBST(this.root, null, null);
    }

    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) {
            return true;
        }
        if (min != null && x.key.compareTo(min) <= 0) {
            return false;
        }
        if (max != null && x.key.compareTo(max) >= 0) {
            return false;
        }
        return this.isBST(x.left, min, x.key) && this.isBST(x.right, x.key, max);
    }

    private boolean isSizeConsistent() {
        return this.isSizeConsistent(this.root);
    }

    private boolean isSizeConsistent(Node x) {
        if (x == null) {
            return true;
        }
        if (x.size != this.size(x.left) + this.size(x.right) + 1) {
            return false;
        }
        return this.isSizeConsistent(x.left) && this.isSizeConsistent(x.right);
    }

    private boolean isRankConsistent() {
        for (int i = 0; i < this.size(); ++i) {
            if (i == this.rank(this.select(i))) continue;
            return false;
        }
        for (Comparable key : this.keys()) {
            if (key.compareTo(this.select(this.rank(key))) == 0) continue;
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        BST<String, Integer> st = new BST<String, Integer>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            st.put(key, i);
            ++i;
        }
        for (String s : st.levelOrder()) {
            StdOut.println(s + " " + st.get(s));
        }
        StdOut.println();
        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }
    }

    private class Node {
        private Key key;
        private Value val;
        private Node left;
        private Node right;
        private int size;
        final /* synthetic */ BST this$0;

        /*
         * WARNING - Possible parameter corruption
         */
        public Node(Key key, Value val, int size) {
            this.this$0 = (BST)n;
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }
}

