/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DirectedCycle {
    private boolean[] marked;
    private int[] edgeTo;
    private boolean[] onStack;
    private Stack<Integer> cycle;

    public DirectedCycle(Digraph G) {
        this.marked = new boolean[G.V()];
        this.onStack = new boolean[G.V()];
        this.edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v] || this.cycle != null) continue;
            this.dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {
        this.onStack[v] = true;
        this.marked[v] = true;
        for (int w : G.adj(v)) {
            if (this.cycle != null) {
                return;
            }
            if (!this.marked[w]) {
                this.edgeTo[w] = v;
                this.dfs(G, w);
                continue;
            }
            if (!this.onStack[w]) continue;
            this.cycle = new Stack();
            int x = v;
            while (x != w) {
                this.cycle.push(x);
                x = this.edgeTo[x];
            }
            this.cycle.push(w);
            this.cycle.push(v);
            assert (this.check());
        }
        this.onStack[v] = false;
    }

    public boolean hasCycle() {
        return this.cycle != null;
    }

    public Iterable<Integer> cycle() {
        return this.cycle;
    }

    private boolean check() {
        if (this.hasCycle()) {
            int first = -1;
            int last = -1;
            for (int v : this.cycle()) {
                if (first == -1) {
                    first = v;
                }
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Directed cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("No directed cycle");
        }
        StdOut.println();
    }
}

