/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AdjMatrixEdgeWeightedDigraph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private DirectedEdge[][] adj;

    public AdjMatrixEdgeWeightedDigraph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("number of vertices must be non-negative");
        }
        this.V = V;
        this.E = 0;
        this.adj = new DirectedEdge[V][V];
    }

    public AdjMatrixEdgeWeightedDigraph(int V, int E) {
        this(V);
        if (E < 0) {
            throw new IllegalArgumentException("number of edges must be non-negative");
        }
        if (E > V * V) {
            throw new IllegalArgumentException("too many edges");
        }
        while (this.E != E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            double weight = 0.01 * (double)StdRandom.uniformInt(0, 100);
            this.addEdge(new DirectedEdge(v, w, weight));
        }
    }

    public int V() {
        return this.V;
    }

    public int E() {
        return this.E;
    }

    public void addEdge(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        this.validateVertex(v);
        this.validateVertex(w);
        if (this.adj[v][w] == null) {
            ++this.E;
            this.adj[v][w] = e;
        }
    }

    public Iterable<DirectedEdge> adj(int v) {
        this.validateVertex(v);
        return new AdjIterator(v);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.V + " " + this.E + NEWLINE);
        for (int v = 0; v < this.V; ++v) {
            s.append(v + ": ");
            for (DirectedEdge e : this.adj(v)) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= this.V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
        }
    }

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(V, E);
        StdOut.println(G);
    }

    private class AdjIterator
    implements Iterator<DirectedEdge>,
    Iterable<DirectedEdge> {
        private int v;
        private int w = 0;

        public AdjIterator(int v) {
            this.v = v;
        }

        @Override
        public Iterator<DirectedEdge> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            while (this.w < AdjMatrixEdgeWeightedDigraph.this.V) {
                if (AdjMatrixEdgeWeightedDigraph.this.adj[this.v][this.w] != null) {
                    return true;
                }
                ++this.w;
            }
            return false;
        }

        @Override
        public DirectedEdge next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return AdjMatrixEdgeWeightedDigraph.this.adj[this.v][this.w++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

