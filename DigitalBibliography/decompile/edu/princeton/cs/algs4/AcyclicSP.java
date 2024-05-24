/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

public class AcyclicSP {
    private double[] distTo;
    private DirectedEdge[] edgeTo;

    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        this.distTo = new double[G.V()];
        this.edgeTo = new DirectedEdge[G.V()];
        this.validateVertex(s);
        for (int v = 0; v < G.V(); ++v) {
            this.distTo[v] = Double.POSITIVE_INFINITY;
        }
        this.distTo[s] = 0.0;
        Topological topological = new Topological(G);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException("Digraph is not acyclic.");
        }
        for (int v : topological.order()) {
            for (DirectedEdge e : G.adj(v)) {
                this.relax(e);
            }
        }
    }

    private void relax(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        if (this.distTo[w] > this.distTo[v] + e.weight()) {
            this.distTo[w] = this.distTo[v] + e.weight();
            this.edgeTo[w] = e;
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

    public Iterable<DirectedEdge> pathTo(int v) {
        this.validateVertex(v);
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

    private void validateVertex(int v) {
        int V = this.distTo.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int s = Integer.parseInt(args[1]);
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        AcyclicSP sp = new AcyclicSP(G, s);
        for (int v = 0; v < G.V(); ++v) {
            if (sp.hasPathTo(v)) {
                StdOut.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v));
                for (DirectedEdge e : sp.pathTo(v)) {
                    StdOut.print(e + "   ");
                }
                StdOut.println();
                continue;
            }
            StdOut.printf("%d to %d         no path\n", s, v);
        }
    }
}

