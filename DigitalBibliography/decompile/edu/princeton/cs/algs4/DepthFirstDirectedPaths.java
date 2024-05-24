/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstDirectedPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private final int s;

    public DepthFirstDirectedPaths(Digraph G, int s) {
        this.marked = new boolean[G.V()];
        this.edgeTo = new int[G.V()];
        this.s = s;
        this.validateVertex(s);
        this.dfs(G, s);
    }

    private void dfs(Digraph G, int v) {
        this.marked[v] = true;
        for (int w : G.adj(v)) {
            if (this.marked[w]) continue;
            this.edgeTo[w] = v;
            this.dfs(G, w);
        }
    }

    public boolean hasPathTo(int v) {
        this.validateVertex(v);
        return this.marked[v];
    }

    public Iterable<Integer> pathTo(int v) {
        this.validateVertex(v);
        if (!this.hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<Integer>();
        int x = v;
        while (x != this.s) {
            path.push(x);
            x = this.edgeTo[x];
        }
        path.push(this.s);
        return path;
    }

    private void validateVertex(int v) {
        int V = this.marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        int s = Integer.parseInt(args[1]);
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(G, s);
        for (int v = 0; v < G.V(); ++v) {
            if (dfs.hasPathTo(v)) {
                StdOut.printf("%d to %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) {
                        StdOut.print(x);
                        continue;
                    }
                    StdOut.print("-" + x);
                }
                StdOut.println();
                continue;
            }
            StdOut.printf("%d to %d:  not connected\n", s, v);
        }
    }
}

