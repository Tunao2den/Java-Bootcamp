/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;
import java.util.Arrays;

public class KruskalMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;
    private double weight;
    private Queue<Edge> mst = new Queue();

    public KruskalMST(EdgeWeightedGraph G) {
        Object[] edges = new Edge[G.E()];
        int t = 0;
        for (Edge e : G.edges()) {
            edges[t++] = e;
        }
        Arrays.sort(edges);
        UF uf = new UF(G.V());
        for (int i = 0; i < G.E() && this.mst.size() < G.V() - 1; ++i) {
            Object e = edges[i];
            int v = ((Edge)e).either();
            int w = ((Edge)e).other(v);
            if (uf.find(v) == uf.find(w)) continue;
            uf.union(v, w);
            this.mst.enqueue((Edge)e);
            this.weight += ((Edge)e).weight();
        }
        assert (this.check(G));
    }

    public Iterable<Edge> edges() {
        return this.mst;
    }

    public double weight() {
        return this.weight;
    }

    private boolean check(EdgeWeightedGraph G) {
        int w;
        int v;
        double total = 0.0;
        for (Edge e : this.edges()) {
            total += e.weight();
        }
        if (Math.abs(total - this.weight()) > 1.0E-12) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, this.weight());
            return false;
        }
        UF uf = new UF(G.V());
        for (Edge e : this.edges()) {
            v = e.either();
            w = e.other(v);
            if (uf.find(v) == uf.find(w)) {
                System.err.println("Not a forest");
                return false;
            }
            uf.union(v, w);
        }
        for (Edge e : G.edges()) {
            v = e.either();
            w = e.other(v);
            if (uf.find(v) == uf.find(w)) continue;
            System.err.println("Not a spanning forest");
            return false;
        }
        for (Edge e : this.edges()) {
            int y;
            int x;
            uf = new UF(G.V());
            for (Edge f : this.mst) {
                x = f.either();
                y = f.other(x);
                if (f == e) continue;
                uf.union(x, y);
            }
            for (Edge f : G.edges()) {
                x = f.either();
                y = f.other(x);
                if (uf.find(x) == uf.find(y) || !(f.weight() < e.weight())) continue;
                System.err.println("Edge " + f + " violates cut optimality conditions");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        KruskalMST mst = new KruskalMST(G);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }
}

