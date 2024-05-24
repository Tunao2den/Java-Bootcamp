/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMaxPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

public class GlobalMincut {
    private static final double FLOATING_POINT_EPSILON = 1.0E-11;
    private double weight = Double.POSITIVE_INFINITY;
    private boolean[] cut;
    private int V;

    public GlobalMincut(EdgeWeightedGraph G) {
        this.V = G.V();
        this.validate(G);
        this.minCut(G, 0);
        assert (this.check(G));
    }

    private void validate(EdgeWeightedGraph G) {
        if (G.V() < 2) {
            throw new IllegalArgumentException("number of vertices of G is less than 2");
        }
        for (Edge e : G.edges()) {
            if (!(e.weight() < 0.0)) continue;
            throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
    }

    public double weight() {
        return this.weight;
    }

    public boolean cut(int v) {
        this.validateVertex(v);
        return this.cut[v];
    }

    private void makeCut(int t, UF uf) {
        for (int v = 0; v < this.cut.length; ++v) {
            this.cut[v] = uf.find(v) == uf.find(t);
        }
    }

    private void minCut(EdgeWeightedGraph G, int a) {
        UF uf = new UF(G.V());
        boolean[] marked = new boolean[G.V()];
        this.cut = new boolean[G.V()];
        CutPhase cp = new CutPhase(0.0, a, a);
        for (int v = G.V(); v > 1; --v) {
            this.minCutPhase(G, marked, cp);
            if (cp.weight < this.weight) {
                this.weight = cp.weight;
                this.makeCut(cp.t, uf);
            }
            G = this.contractEdge(G, cp.s, cp.t);
            marked[cp.t] = true;
            uf.union(cp.s, cp.t);
        }
    }

    private void minCutPhase(EdgeWeightedGraph G, boolean[] marked, CutPhase cp) {
        int v;
        IndexMaxPQ<Double> pq = new IndexMaxPQ<Double>(G.V());
        for (v = 0; v < G.V(); ++v) {
            if (v == cp.s || marked[v]) continue;
            pq.insert(v, 0.0);
        }
        pq.insert(cp.s, Double.POSITIVE_INFINITY);
        while (!pq.isEmpty()) {
            v = pq.delMax();
            cp.s = cp.t;
            cp.t = v;
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (!pq.contains(w)) continue;
                pq.increaseKey(w, (Double)pq.keyOf(w) + e.weight());
            }
        }
        cp.weight = 0.0;
        for (Edge e : G.adj(cp.t)) {
            cp.weight += e.weight();
        }
    }

    private EdgeWeightedGraph contractEdge(EdgeWeightedGraph G, int s, int t) {
        EdgeWeightedGraph H = new EdgeWeightedGraph(G.V());
        for (int v = 0; v < G.V(); ++v) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (v == s && w == t || v == t && w == s || v >= w) continue;
                if (w == t) {
                    H.addEdge(new Edge(v, s, e.weight()));
                    continue;
                }
                if (v == t) {
                    H.addEdge(new Edge(w, s, e.weight()));
                    continue;
                }
                H.addEdge(new Edge(v, w, e.weight()));
            }
        }
        return H;
    }

    private boolean check(EdgeWeightedGraph G) {
        double value = Double.POSITIVE_INFINITY;
        int s = 0;
        for (int t = 1; t < G.V(); ++t) {
            FlowNetwork F = new FlowNetwork(G.V());
            for (Edge e : G.edges()) {
                int v = e.either();
                int w = e.other(v);
                F.addEdge(new FlowEdge(v, w, e.weight()));
                F.addEdge(new FlowEdge(w, v, e.weight()));
            }
            FordFulkerson maxflow = new FordFulkerson(F, s, t);
            value = Math.min(value, maxflow.value());
        }
        if (Math.abs(this.weight - value) > 1.0E-11) {
            System.err.println("Min cut weight = " + this.weight + " , max flow value = " + value);
            return false;
        }
        return true;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= this.V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        GlobalMincut mc = new GlobalMincut(G);
        StdOut.print("Min cut: ");
        for (int v = 0; v < G.V(); ++v) {
            if (!mc.cut(v)) continue;
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.println("Min cut weight = " + mc.weight());
    }

    private class CutPhase {
        private double weight;
        private int s;
        private int t;

        public CutPhase(double weight, int s, int t) {
            this.weight = weight;
            this.s = s;
            this.t = t;
        }
    }
}

