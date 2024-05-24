/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DirectedDFS {
    private boolean[] marked;
    private int count;

    public DirectedDFS(Digraph G, int s) {
        this.marked = new boolean[G.V()];
        this.validateVertex(s);
        this.dfs(G, s);
    }

    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        this.marked = new boolean[G.V()];
        this.validateVertices(sources);
        for (int v : sources) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {
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

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int vertexCount = 0;
        for (Integer v : vertices) {
            ++vertexCount;
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            this.validateVertex(v);
        }
        if (vertexCount == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        Bag<Integer> sources = new Bag<Integer>();
        for (int i = 1; i < args.length; ++i) {
            int s = Integer.parseInt(args[i]);
            sources.add(s);
        }
        DirectedDFS dfs = new DirectedDFS(G, sources);
        for (int v = 0; v < G.V(); ++v) {
            if (!dfs.marked(v)) continue;
            StdOut.print(v + " ");
        }
        StdOut.println();
    }
}

