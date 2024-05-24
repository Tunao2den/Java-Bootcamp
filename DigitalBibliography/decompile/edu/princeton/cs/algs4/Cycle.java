/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Cycle {
    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;

    public Cycle(Graph G) {
        if (this.hasParallelEdges(G)) {
            return;
        }
        this.marked = new boolean[G.V()];
        this.edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.dfs(G, -1, v);
        }
    }

    private boolean hasSelfLoop(Graph G) {
        for (int v = 0; v < G.V(); ++v) {
            for (int w : G.adj(v)) {
                if (v != w) continue;
                this.cycle = new Stack();
                this.cycle.push(v);
                this.cycle.push(v);
                return true;
            }
        }
        return false;
    }

    private boolean hasParallelEdges(Graph G) {
        this.marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            for (int w : G.adj(v)) {
                if (this.marked[w]) {
                    this.cycle = new Stack();
                    this.cycle.push(v);
                    this.cycle.push(w);
                    this.cycle.push(v);
                    return true;
                }
                this.marked[w] = true;
            }
            for (int w : G.adj(v)) {
                this.marked[w] = false;
            }
        }
        return false;
    }

    public boolean hasCycle() {
        return this.cycle != null;
    }

    public Iterable<Integer> cycle() {
        return this.cycle;
    }

    private void dfs(Graph G, int u, int v) {
        this.marked[v] = true;
        for (int w : G.adj(v)) {
            if (this.cycle != null) {
                return;
            }
            if (!this.marked[w]) {
                this.edgeTo[w] = v;
                this.dfs(G, v, w);
                continue;
            }
            if (w == u) continue;
            this.cycle = new Stack();
            int x = v;
            while (x != w) {
                this.cycle.push(x);
                x = this.edgeTo[x];
            }
            this.cycle.push(w);
            this.cycle.push(v);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        Cycle finder = new Cycle(G);
        if (finder.hasCycle()) {
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("Graph is acyclic");
        }
    }
}

