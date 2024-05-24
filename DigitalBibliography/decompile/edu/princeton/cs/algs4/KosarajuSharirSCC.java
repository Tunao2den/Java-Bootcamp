/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TransitiveClosure;
import java.util.Iterator;

public class KosarajuSharirSCC {
    private boolean[] marked;
    private int[] id;
    private int count;

    public KosarajuSharirSCC(Digraph G) {
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());
        this.marked = new boolean[G.V()];
        this.id = new int[G.V()];
        for (int v : dfs.reversePost()) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
            ++this.count;
        }
        assert (this.check(G));
    }

    private void dfs(Digraph G, int v) {
        this.marked[v] = true;
        this.id[v] = this.count;
        for (int w : G.adj(v)) {
            if (this.marked[w]) continue;
            this.dfs(G, w);
        }
    }

    public int count() {
        return this.count;
    }

    public boolean stronglyConnected(int v, int w) {
        this.validateVertex(v);
        this.validateVertex(w);
        return this.id[v] == this.id[w];
    }

    public int id(int v) {
        this.validateVertex(v);
        return this.id[v];
    }

    private boolean check(Digraph G) {
        TransitiveClosure tc = new TransitiveClosure(G);
        for (int v = 0; v < G.V(); ++v) {
            for (int w = 0; w < G.V(); ++w) {
                if (this.stronglyConnected(v, w) == (tc.reachable(v, w) && tc.reachable(w, v))) continue;
                return false;
            }
        }
        return true;
    }

    private void validateVertex(int v) {
        int V = this.marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        int i;
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        KosarajuSharirSCC scc = new KosarajuSharirSCC(G);
        int m = scc.count();
        StdOut.println(m + " strong components");
        Queue[] components = new Queue[m];
        for (i = 0; i < m; ++i) {
            components[i] = new Queue();
        }
        for (int v = 0; v < G.V(); ++v) {
            components[scc.id(v)].enqueue(v);
        }
        for (i = 0; i < m; ++i) {
            Iterator iterator = components[i].iterator();
            while (iterator.hasNext()) {
                int v = (Integer)iterator.next();
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
    }
}

