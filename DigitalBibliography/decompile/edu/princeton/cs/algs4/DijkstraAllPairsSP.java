/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class DijkstraAllPairsSP {
    private DijkstraSP[] all;

    public DijkstraAllPairsSP(EdgeWeightedDigraph G) {
        this.all = new DijkstraSP[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            this.all[v] = new DijkstraSP(G, v);
        }
    }

    public Iterable<DirectedEdge> path(int s, int t) {
        this.validateVertex(s);
        this.validateVertex(t);
        return this.all[s].pathTo(t);
    }

    public boolean hasPath(int s, int t) {
        this.validateVertex(s);
        this.validateVertex(t);
        return this.dist(s, t) < Double.POSITIVE_INFINITY;
    }

    public double dist(int s, int t) {
        this.validateVertex(s);
        this.validateVertex(t);
        return this.all[s].distTo(t);
    }

    private void validateVertex(int v) {
        int V = this.all.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        int w;
        int v;
        In in = new In(args[0]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        DijkstraAllPairsSP spt = new DijkstraAllPairsSP(G);
        StdOut.printf("  ", new Object[0]);
        for (v = 0; v < G.V(); ++v) {
            StdOut.printf("%6d ", v);
        }
        StdOut.println();
        for (v = 0; v < G.V(); ++v) {
            StdOut.printf("%3d: ", v);
            for (w = 0; w < G.V(); ++w) {
                if (spt.hasPath(v, w)) {
                    StdOut.printf("%6.2f ", spt.dist(v, w));
                    continue;
                }
                StdOut.printf("  Inf ", new Object[0]);
            }
            StdOut.println();
        }
        StdOut.println();
        for (v = 0; v < G.V(); ++v) {
            for (w = 0; w < G.V(); ++w) {
                if (spt.hasPath(v, w)) {
                    StdOut.printf("%d to %d (%5.2f)  ", v, w, spt.dist(v, w));
                    for (DirectedEdge e : spt.path(v, w)) {
                        StdOut.print(e + "  ");
                    }
                    StdOut.println();
                    continue;
                }
                StdOut.printf("%d to %d no path\n", v, w);
            }
        }
    }
}

