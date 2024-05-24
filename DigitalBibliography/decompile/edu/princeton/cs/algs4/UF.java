/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class UF {
    private int[] parent;
    private byte[] rank;
    private int count;

    public UF(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        this.count = n;
        this.parent = new int[n];
        this.rank = new byte[n];
        for (int i = 0; i < n; ++i) {
            this.parent[i] = i;
            this.rank[i] = 0;
        }
    }

    public int find(int p) {
        this.validate(p);
        while (p != this.parent[p]) {
            this.parent[p] = this.parent[this.parent[p]];
            p = this.parent[p];
        }
        return p;
    }

    public int count() {
        return this.count;
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
        if (this.rank[rootP] < this.rank[rootQ]) {
            this.parent[rootP] = rootQ;
        } else if (this.rank[rootP] > this.rank[rootQ]) {
            this.parent[rootQ] = rootP;
        } else {
            this.parent[rootQ] = rootP;
            int n = rootP;
            this.rank[n] = (byte)(this.rank[n] + 1);
        }
        --this.count;
    }

    private void validate(int p) {
        int n = this.parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        UF uf = new UF(n);
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

