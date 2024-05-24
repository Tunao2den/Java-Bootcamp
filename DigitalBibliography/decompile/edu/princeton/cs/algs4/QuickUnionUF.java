/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class QuickUnionUF {
    private int[] parent;
    private int count;

    public QuickUnionUF(int n) {
        this.parent = new int[n];
        this.count = n;
        for (int i = 0; i < n; ++i) {
            this.parent[i] = i;
        }
    }

    public int count() {
        return this.count;
    }

    public int find(int p) {
        this.validate(p);
        while (p != this.parent[p]) {
            p = this.parent[p];
        }
        return p;
    }

    private void validate(int p) {
        int n = this.parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    @Deprecated
    public boolean connected(int p, int q) {
        return this.find(p) == this.find(q);
    }

    public void union(int p, int q) {
        int rootQ;
        int rootP = this.find(p);
        if (rootP == (rootQ = this.find(q))) {
            return;
        }
        this.parent[rootP] = rootQ;
        --this.count;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        QuickUnionUF uf = new QuickUnionUF(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.find(p) == uf.find(q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        StdOut.println(uf.count() + " components");
    }
}

