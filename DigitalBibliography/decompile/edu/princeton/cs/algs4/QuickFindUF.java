/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class QuickFindUF {
    private int[] id;
    private int count;

    public QuickFindUF(int n) {
        this.count = n;
        this.id = new int[n];
        for (int i = 0; i < n; ++i) {
            this.id[i] = i;
        }
    }

    public int count() {
        return this.count;
    }

    public int find(int p) {
        this.validate(p);
        return this.id[p];
    }

    private void validate(int p) {
        int n = this.id.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    @Deprecated
    public boolean connected(int p, int q) {
        this.validate(p);
        this.validate(q);
        return this.id[p] == this.id[q];
    }

    public void union(int p, int q) {
        this.validate(p);
        this.validate(q);
        int pID = this.id[p];
        int qID = this.id[q];
        if (pID == qID) {
            return;
        }
        for (int i = 0; i < this.id.length; ++i) {
            if (this.id[i] != pID) continue;
            this.id[i] = qID;
        }
        --this.count;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        QuickFindUF uf = new QuickFindUF(n);
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

