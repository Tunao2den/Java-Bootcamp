/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedDirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BellmanFordSP {
    private static final double EPSILON = 1.0E-14;
    private double[] distTo;
    private DirectedEdge[] edgeTo;
    private boolean[] onQueue;
    private Queue<Integer> queue;
    private int cost;
    private Iterable<DirectedEdge> cycle;

    public BellmanFordSP(EdgeWeightedDigraph G, int s) {
        int v;
        this.distTo = new double[G.V()];
        this.edgeTo = new DirectedEdge[G.V()];
        this.onQueue = new boolean[G.V()];
        for (v = 0; v < G.V(); ++v) {
            this.distTo[v] = Double.POSITIVE_INFINITY;
        }
        this.distTo[s] = 0.0;
        this.queue = new Queue();
        this.queue.enqueue(s);
        this.onQueue[s] = true;
        while (!this.queue.isEmpty() && !this.hasNegativeCycle()) {
            v = this.queue.dequeue();
            this.onQueue[v] = false;
            this.relax(G, v);
        }
        assert (this.check(G, s));
    }

    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (this.distTo[w] > this.distTo[v] + e.weight() + 1.0E-14) {
                this.distTo[w] = this.distTo[v] + e.weight();
                this.edgeTo[w] = e;
                if (!this.onQueue[w]) {
                    this.queue.enqueue(w);
                    this.onQueue[w] = true;
                }
            }
            if (++this.cost % G.V() != 0) continue;
            this.findNegativeCycle();
            if (!this.hasNegativeCycle()) continue;
            return;
        }
    }

    public boolean hasNegativeCycle() {
        return this.cycle != null;
    }

    public Iterable<DirectedEdge> negativeCycle() {
        return this.cycle;
    }

    private void findNegativeCycle() {
        int V = this.edgeTo.length;
        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; ++v) {
            if (this.edgeTo[v] == null) continue;
            spt.addEdge(this.edgeTo[v]);
        }
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
        this.cycle = finder.cycle();
    }

    public double distTo(int v) {
        this.validateVertex(v);
        if (this.hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cost cycle exists");
        }
        return this.distTo[v];
    }

    public boolean hasPathTo(int v) {
        this.validateVertex(v);
        return this.distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        this.validateVertex(v);
        if (this.hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cost cycle exists");
        }
        if (!this.hasPathTo(v)) {
            return null;
        }
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        DirectedEdge e = this.edgeTo[v];
        while (e != null) {
            path.push(e);
            e = this.edgeTo[e.from()];
        }
        return path;
    }

    private boolean check(EdgeWeightedDigraph G, int s) {
        if (this.hasNegativeCycle()) {
            double weight = 0.0;
            for (DirectedEdge e : this.negativeCycle()) {
                weight += e.weight();
            }
            if (weight >= 0.0) {
                System.err.println("error: weight of negative cycle = " + weight);
                return false;
            }
        } else {
            int v;
            if (this.distTo[s] != 0.0 || this.edgeTo[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
                return false;
            }
            for (v = 0; v < G.V(); ++v) {
                if (v == s || this.edgeTo[v] != null || this.distTo[v] == Double.POSITIVE_INFINITY) continue;
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
            for (v = 0; v < G.V(); ++v) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    if (!(this.distTo[v] + e.weight() < this.distTo[w])) continue;
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
            for (int w = 0; w < G.V(); ++w) {
                if (this.edgeTo[w] == null) continue;
                DirectedEdge e = this.edgeTo[w];
                int v2 = e.from();
                if (w != e.to()) {
                    return false;
                }
                if (this.distTo[v2] + e.weight() == this.distTo[w]) continue;
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        StdOut.println("Satisfies optimality conditions");
        StdOut.println();
        return true;
    }

    private void validateVertex(int v) {
        int V = this.distTo.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = Integer.parseInt(args[1]);
        BellmanFordSP sp = new BellmanFordSP(G, s);
        if (sp.hasNegativeCycle()) {
            for (DirectedEdge e : sp.negativeCycle()) {
                StdOut.println(e);
            }
        } else {
            for (int v = 0; v < G.V(); ++v) {
                if (sp.hasPathTo(v)) {
                    StdOut.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(v));
                    for (DirectedEdge e : sp.pathTo(v)) {
                        StdOut.print(e + "   ");
                    }
                    StdOut.println();
                    continue;
                }
                StdOut.printf("%d to %d           no path\n", s, v);
            }
        }
    }
}

