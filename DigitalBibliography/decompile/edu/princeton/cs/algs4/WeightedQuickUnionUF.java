/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WeightedQuickUnionUF {
    private int[] parent;
    private int[] size;
    private int count;

    public WeightedQuickUnionUF(int n) {
        this.count = n;
        this.parent = new int[n];
        this.size = new int[n];
        for (int i = 0; i < n; ++i) {
            this.parent[i] = i;
            this.size[i] = 1;
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

    @Deprecated
    public boolean connected(int p, int q) {
        return this.find(p) == this.find(q);
    }

    private void validate(int p) {
        int n = this.parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    public void union(int p, int q) {
        int rootQ;
        int rootP = this.find(p);
        if (rootP == (rootQ = this.find(q))) {
            return;
        }
        if (this.size[rootP] < this.size[rootQ]) {
            this.parent[rootP] = rootQ;
            int n = rootQ;
            this.size[n] = this.size[n] + this.size[rootP];
        } else {
            this.parent[rootQ] = rootP;
            int n = rootP;
            this.size[n] = this.size[n] + this.size[rootQ];
        }
        --this.count;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(n);
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

