/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.UF;

public class BoruvkaMST {
    private static final double FLOATING_POINT_EPSILON = 1.0E-12;
    private Bag<Edge> mst = new Bag();
    private double weight;

    public BoruvkaMST(EdgeWeightedGraph G) {
        UF uf = new UF(G.V());
        for (int t = 1; t < G.V() && this.mst.size() < G.V() - 1; t += t) {
            int w;
            int v;
            Edge[] closest = new Edge[G.V()];
            for (Edge e : G.edges()) {
                int j;
                v = e.either();
                w = e.other(v);
                int i = uf.find(v);
                if (i == (j = uf.find(w))) continue;
                if (closest[i] == null || BoruvkaMST.less(e, closest[i])) {
                    closest[i] = e;
                }
                if (closest[j] != null && !BoruvkaMST.less(e, closest[j])) continue;
                closest[j] = e;
            }
            for (int i = 0; i < G.V(); ++i) {
                Edge e;
                e = closest[i];
                if (e == null) continue;
                v = e.either();
                w = e.other(v);
                if (uf.find(v) == uf.find(w)) continue;
                this.mst.add(e);
                this.weight += e.weight();
                uf.union(v, w);
            }
        }
        assert (this.check(G));
    }

    public Iterable<Edge> edges() {
        return this.mst;
    }

    public double weight() {
        return this.weight;
    }

    private static boolean less(Edge e, Edge f) {
        return e.compareTo(f) < 0;
    }

    private boolean check(EdgeWeightedGraph G) {
        int w;
        int v;
        double totalWeight = 0.0;
        for (Edge e : this.edges()) {
            totalWeight += e.weight();
        }
        if (Math.abs(totalWeight - this.weight()) > 1.0E-12) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", totalWeight, this.weight());
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
        BoruvkaMST mst = new BoruvkaMST(G);
        for (Edge e : mst.edges()) {
            StdOut.println(e);
        }
        StdOut.printf("%.5f\n", mst.weight());
    }
}

