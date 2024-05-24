/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class CC {
    private boolean[] marked;
    private int[] id;
    private int[] size;
    private int count;

    public CC(Graph G) {
        this.marked = new boolean[G.V()];
        this.id = new int[G.V()];
        this.size = new int[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
            ++this.count;
        }
    }

    public CC(EdgeWeightedGraph G) {
        this.marked = new boolean[G.V()];
        this.id = new int[G.V()];
        this.size = new int[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
            ++this.count;
        }
    }

    private void dfs(Graph G, int v) {
        this.marked[v] = true;
        this.id[v] = this.count;
        int n = this.count;
        this.size[n] = this.size[n] + 1;
        for (int w : G.adj(v)) {
            if (this.marked[w]) continue;
            this.dfs(G, w);
        }
    }

    private void dfs(EdgeWeightedGraph G, int v) {
        this.marked[v] = true;
        this.id[v] = this.count;
        int n = this.count;
        this.size[n] = this.size[n] + 1;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (this.marked[w]) continue;
            this.dfs(G, w);
        }
    }

    public int id(int v) {
        this.validateVertex(v);
        return this.id[v];
    }

    public int size(int v) {
        this.validateVertex(v);
        return this.size[this.id[v]];
    }

    public int count() {
        return this.count;
    }

    public boolean connected(int v, int w) {
        this.validateVertex(v);
        this.validateVertex(w);
        return this.id(v) == this.id(w);
    }

    @Deprecated
    public boolean areConnected(int v, int w) {
        this.validateVertex(v);
        this.validateVertex(w);
        return this.id(v) == this.id(w);
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
        Graph G = new Graph(in);
        CC cc = new CC(G);
        int m = cc.count();
        StdOut.println(m + " components");
        Queue[] components = new Queue[m];
        for (i = 0; i < m; ++i) {
            components[i] = new Queue();
        }
        for (int v = 0; v < G.V(); ++v) {
            components[cc.id(v)].enqueue(v);
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

