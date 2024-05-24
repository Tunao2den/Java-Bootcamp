/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;

public class RedBlackBST<Key extends Comparable<Key>, Value> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private Node root;

    private boolean isRed(Node x) {
        if (x == null) {
            return false;
        }
        return x.color;
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    public int size() {
        return this.size(this.root);
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        return this.get(this.root, key);
    }

    private Value get(Node x, Key key) {
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
                continue;
            }
            if (cmp > 0) {
                x = x.right;
                continue;
            }
            return x.val;
        }
        return null;
    }

    public boolean contains(Key key) {
        return this.get(key) != null;
    }

    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("first argument to put() is null");
        }
        if (val == null) {
            this.delete(key);
            return;
        }
        this.root = this.put(this.root, key, val);
        this.root.color = false;
    }

    private Node put(Node h, Key key, Value val) {
        if (h == null) {
            return new Node(this, key, val, true, 1);
        }
        int cmp = key.compareTo(h.key);
        if (cmp < 0) {
            h.left = this.put(h.left, key, val);
        } else if (cmp > 0) {
            h.right = this.put(h.right, key, val);
        } else {
            h.val = val;
        }
        if (this.isRed(h.right) && !this.isRed(h.left)) {
            h = this.rotateLeft(h);
        }
        if (this.isRed(h.left) && this.isRed(h.left.left)) {
            h = this.rotateRight(h);
        }
        if (this.isRed(h.left) && this.isRed(h.right)) {
            this.flipColors(h);
        }
        h.size = this.size(h.left) + this.size(h.right) + 1;
        return h;
    }

    public void deleteMin() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("BST underflow");
        }
        if (!this.isRed(this.root.left) && !this.isRed(this.root.right)) {
            this.root.color = true;
        }
        this.root = this.deleteMin(this.root);
        if (!this.isEmpty()) {
            this.root.color = false;
        }
    }

    private Node deleteMin(Node h) {
        if (h.left == null) {
            return null;
        }
        if (!this.isRed(h.left) && !this.isRed(h.left.left)) {
            h = this.moveRedLeft(h);
        }
        h.left = this.deleteMin(h.left);
        return this.balance(h);
    }

    public void deleteMax() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("BST underflow");
        }
        if (!this.isRed(this.root.left) && !this.isRed(this.root.right)) {
            this.root.color = true;
        }
        this.root = this.deleteMax(this.root);
        if (!this.isEmpty()) {
            this.root.color = false;
        }
    }

    private Node deleteMax(Node h) {
        if (this.isRed(h.left)) {
            h = this.rotateRight(h);
        }
        if (h.right == null) {
            return null;
        }
        if (!this.isRed(h.right) && !this.isRed(h.right.left)) {
            h = this.moveRedRight(h);
        }
        h.right = this.deleteMax(h.right);
        return this.balance(h);
    }

    public void delete(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        }
        if (!this.contains(key)) {
            return;
        }
        if (!this.isRed(this.root.left) && !this.isRed(this.root.right)) {
            this.root.color = true;
        }
        this.root = this.delete(this.root, key);
        if (!this.isEmpty()) {
            this.root.color = false;
        }
    }

    private Node delete(Node h, Key key) {
        if (key.compareTo(h.key) < 0) {
            if (!this.isRed(h.left) && !this.isRed(h.left.left)) {
                h = this.moveRedLeft(h);
            }
            h.left = this.delete(h.left, key);
        } else {
            if (this.isRed(h.left)) {
                h = this.rotateRight(h);
            }
            if (key.compareTo(h.key) == 0 && h.right == null) {
                return null;
            }
            if (!this.isRed(h.right) && !this.isRed(h.right.left)) {
                h = this.moveRedRight(h);
            }
            if (key.compareTo(h.key) == 0) {
                Node x = this.min(h.right);
                h.key = x.key;
                h.val = x.val;
                h.right = this.deleteMin(h.right);
            } else {
                h.right = this.delete(h.right, key);
            }
        }
        return this.balance(h);
    }

    private Node rotateRight(Node h) {
        assert (h != null && this.isRed(h.left));
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = true;
        x.size = h.size;
        h.size = this.size(h.left) + this.size(h.right) + 1;
        return x;
    }

    private Node rotateLeft(Node h) {
        assert (h != null && this.isRed(h.right));
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = true;
        x.size = h.size;
        h.size = this.size(h.left) + this.size(h.right) + 1;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }

    private Node moveRedLeft(Node h) {
        this.flipColors(h);
        if (this.isRed(h.right.left)) {
            h.right = this.rotateRight(h.right);
            h = this.rotateLeft(h);
            this.flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        this.flipColors(h);
        if (this.isRed(h.left.left)) {
            h = this.rotateRight(h);
            this.flipColors(h);
        }
        return h;
    }

    private Node balance(Node h) {
        if (this.isRed(h.right) && !this.isRed(h.left)) {
            h = this.rotateLeft(h);
        }
        if (this.isRed(h.left) && this.isRed(h.left.left)) {
            h = this.rotateRight(h);
        }
        if (this.isRed(h.left) && this.isRed(h.right)) {
            this.flipColors(h);
        }
        h.size = this.size(h.left) + this.size(h.right) + 1;
        return h;
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
        if (cmp > 0) {
            return this.ceiling(x.right, key);
        }
        Node t = this.ceiling(x.left, key);
        if (t != null) {
            return t;
        }
        return x;
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
        if (!this.is23()) {
            StdOut.println("Not a 2-3 tree");
        }
        if (!this.isBalanced()) {
            StdOut.println("Not balanced");
        }
        return this.isBST() && this.isSizeConsistent() && this.isRankConsistent() && this.is23() && this.isBalanced();
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

    private boolean is23() {
        return this.is23(this.root);
    }

    private boolean is23(Node x) {
        if (x == null) {
            return true;
        }
        if (this.isRed(x.right)) {
            return false;
        }
        if (x != this.root && this.isRed(x) && this.isRed(x.left)) {
            return false;
        }
        return this.is23(x.left) && this.is23(x.right);
    }

    private boolean isBalanced() {
        int black = 0;
        Node x = this.root;
        while (x != null) {
            if (!this.isRed(x)) {
                ++black;
            }
            x = x.left;
        }
        return this.isBalanced(this.root, black);
    }

    private boolean isBalanced(Node x, int black) {
        if (x == null) {
            return black == 0;
        }
        if (!this.isRed(x)) {
            --black;
        }
        return this.isBalanced(x.left, black) && this.isBalanced(x.right, black);
    }

    public static void main(String[] args) {
        RedBlackBST<String, Integer> st = new RedBlackBST<String, Integer>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            st.put(key, i);
            ++i;
        }
        StdOut.println();
        for (String s : st.keys()) {
            StdOut.println(s + " " + st.get(s));
        }
        StdOut.println();
    }

    private class Node {
        private Key key;
        private Value val;
        private Node left;
        private Node right;
        private boolean color;
        private int size;
        final /* synthetic */ RedBlackBST this$0;

        /*
         * WARNING - Possible parameter corruption
         */
        public Node(Key key, Value val, boolean color, int size) {
            this.this$0 = (RedBlackBST)n;
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }
}

