/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstSearch {
    private boolean[] marked;
    private int count;

    public DepthFirstSearch(Graph G, int s) {
        this.marked = new boolean[G.V()];
        this.validateVertex(s);
        this.dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        ++this.count;
        this.marked[v] = true;
        for (int w : G.adj(v)) {
            if (this.marked[w]) continue;
            this.dfs(G, w);
        }
    }

    public boolean marked(int v) {
        this.validateVertex(v);
        return this.marked[v];
    }

    public int count() {
        return this.count;
    }

    private void validateVertex(int v) {
        int V = this.marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        int s = Integer.parseInt(args[1]);
        DepthFirstSearch search = new DepthFirstSearch(G, s);
        for (int v = 0; v < G.V(); ++v) {
            if (!search.marked(v)) continue;
            StdOut.print(v + " ");
        }
        StdOut.println();
        if (search.count() != G.V()) {
            StdOut.println("NOT connected");
        } else {
            StdOut.println("connected");
        }
    }
}

