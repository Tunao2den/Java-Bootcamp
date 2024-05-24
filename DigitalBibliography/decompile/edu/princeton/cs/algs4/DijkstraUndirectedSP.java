/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DijkstraUndirectedSP {
    private double[] distTo;
    private Edge[] edgeTo;
    private IndexMinPQ<Double> pq;

    public DijkstraUndirectedSP(EdgeWeightedGraph G, int s) {
        int v;
        for (Edge e : G.edges()) {
            if (!(e.weight() < 0.0)) continue;
            throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
        this.distTo = new double[G.V()];
        this.edgeTo = new Edge[G.V()];
        this.validateVertex(s);
        for (v = 0; v < G.V(); ++v) {
            this.distTo[v] = Double.POSITIVE_INFINITY;
        }
        this.distTo[s] = 0.0;
        this.pq = new IndexMinPQ(G.V());
        this.pq.insert(s, this.distTo[s]);
        while (!this.pq.isEmpty()) {
            v = this.pq.delMin();
            for (Edge e : G.adj(v)) {
                this.relax(e, v);
            }
        }
        assert (this.check(G, s));
    }

    private void relax(Edge e, int v) {
        int w = e.other(v);
        if (this.distTo[w] > this.distTo[v] + e.weight()) {
            this.distTo[w] = this.distTo[v] + e.weight();
            this.edgeTo[w] = e;
            if (this.pq.contains(w)) {
                this.pq.decreaseKey(w, this.distTo[w]);
            } else {
                this.pq.insert(w, this.distTo[w]);
            }
        }
    }

    public double distTo(int v) {
        this.validateVertex(v);
        return this.distTo[v];
    }

    public boolean hasPathTo(int v) {
        this.validateVertex(v);
        return this.distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<Edge> pathTo(int v) {
        this.validateVertex(v);
        if (!this.hasPathTo(v)) {
            return null;
        }
        Stack<Edge> path = new Stack<Edge>();
        int x = v;
        Edge e = this.edgeTo[v];
        while (e != null) {
            path.push(e);
            x = e.other(x);
            e = this.edgeTo[x];
        }
        return path;
    }

    private boolean check(EdgeWeightedGraph G, int s) {
        int v;
        for (Edge edge : G.edges()) {
            if (!(edge.weight() < 0.0)) continue;
            System.err.println("negative edge weight detected");
            return false;
        }
        if (this.distTo[s] != 0.0 || this.edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (v = 0; v < G.V(); ++v) {
            if (v == s || this.edgeTo[v] != null || this.distTo[v] == Double.POSITIVE_INFINITY) continue;
            System.err.println("distTo[] and edgeTo[] inconsistent");
            return false;
        }
        for (v = 0; v < G.V(); ++v) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (!(this.distTo[v] + e.weight() < this.distTo[w])) continue;
                System.err.println("edge " + e + " not relaxed");
                return false;
            }
        }
        for (int w = 0; w < G.V(); ++w) {
            if (this.edgeTo[w] == null) continue;
            Edge edge = this.edgeTo[w];
            if (w != edge.either() && w != edge.other(edge.either())) {
                return false;
            }
            int v2 = edge.other(w);
            if (this.distTo[v2] + edge.weight() == this.distTo[w]) continue;
            System.err.println("edge " + edge + " on shortest path not tight");
            return false;
        }
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
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        int s = Integer.parseInt(args[1]);
        DijkstraUndirectedSP sp = new DijkstraUndirectedSP(G, s);
        for (int t = 0; t < G.V(); ++t) {
            if (sp.hasPathTo(t)) {
                StdOut.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
                for (Edge e : sp.pathTo(t)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
                continue;
            }
            StdOut.printf("%d to %d         no path\n", s, t);
        }
    }
}

