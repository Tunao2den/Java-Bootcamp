/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class EdgeWeightedDirectedCycle {
    private boolean[] marked;
    private DirectedEdge[] edgeTo;
    private boolean[] onStack;
    private Stack<DirectedEdge> cycle;

    public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
        this.marked = new boolean[G.V()];
        this.onStack = new boolean[G.V()];
        this.edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
        }
        assert (this.check());
    }

    private void dfs(EdgeWeightedDigraph G, int v) {
        this.onStack[v] = true;
        this.marked[v] = true;
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (this.cycle != null) {
                return;
            }
            if (!this.marked[w]) {
                this.edgeTo[w] = e;
                this.dfs(G, w);
                continue;
            }
            if (!this.onStack[w]) continue;
            this.cycle = new Stack();
            DirectedEdge f = e;
            while (f.from() != w) {
                this.cycle.push(f);
                f = this.edgeTo[f.from()];
            }
            this.cycle.push(f);
            return;
        }
        this.onStack[v] = false;
    }

    public boolean hasCycle() {
        return this.cycle != null;
    }

    public Iterable<DirectedEdge> cycle() {
        return this.cycle;
    }

    private boolean check() {
        if (this.hasCycle()) {
            DirectedEdge first = null;
            DirectedEdge last = null;
            for (DirectedEdge e : this.cycle()) {
                if (first == null) {
                    first = e;
                }
                if (last != null && last.to() != e.from()) {
                    System.err.printf("cycle edges %s and %s not incident\n", last, e);
                    return false;
                }
                last = e;
            }
            if (first == null || last == null) {
                System.err.printf("cycle contains no edges\n", new Object[0]);
                return false;
            }
            if (last.to() != first.from()) {
                System.err.printf("cycle edges %s and %s not incident\n", last, first);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        double weight;
        int w;
        int v;
        int i;
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 0; i < E; ++i) {
            while ((v = StdRandom.uniformInt(V)) >= (w = StdRandom.uniformInt(V))) {
            }
            weight = StdRandom.uniformDouble(0.0, 1.0);
            G.addEdge(new DirectedEdge(v, w, weight));
        }
        for (i = 0; i < F; ++i) {
            v = StdRandom.uniformInt(V);
            w = StdRandom.uniformInt(V);
            weight = StdRandom.uniformDouble(0.0, 1.0);
            G.addEdge(new DirectedEdge(v, w, weight));
        }
        StdOut.println(G);
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (finder.hasCycle()) {
            StdOut.print("Cycle: ");
            for (DirectedEdge e : finder.cycle()) {
                StdOut.print(e + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("No directed cycle");
        }
    }
}

