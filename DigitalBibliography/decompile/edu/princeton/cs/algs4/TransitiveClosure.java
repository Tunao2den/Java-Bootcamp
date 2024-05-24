/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class TransitiveClosure {
    private DirectedDFS[] tc;

    public TransitiveClosure(Digraph G) {
        this.tc = new DirectedDFS[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            this.tc[v] = new DirectedDFS(G, v);
        }
    }

    public boolean reachable(int v, int w) {
        this.validateVertex(v);
        this.validateVertex(w);
        return this.tc[v].marked(w);
    }

    private void validateVertex(int v) {
        int V = this.tc.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        int v;
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        TransitiveClosure tc = new TransitiveClosure(G);
        StdOut.print("     ");
        for (v = 0; v < G.V(); ++v) {
            StdOut.printf("%3d", v);
        }
        StdOut.println();
        StdOut.println("--------------------------------------------");
        for (v = 0; v < G.V(); ++v) {
            StdOut.printf("%3d: ", v);
            for (int w = 0; w < G.V(); ++w) {
                if (tc.reachable(v, w)) {
                    StdOut.printf("  T", new Object[0]);
                    continue;
                }
                StdOut.printf("   ", new Object[0]);
            }
            StdOut.println();
        }
    }
}

