/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class FlowNetwork {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Bag<FlowEdge>[] adj;

    public FlowNetwork(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices in a Graph must be non-negative");
        }
        this.V = V;
        this.E = 0;
        this.adj = new Bag[V];
        for (int v = 0; v < V; ++v) {
            this.adj[v] = new Bag();
        }
    }

    public FlowNetwork(int V, int E) {
        this(V);
        if (E < 0) {
            throw new IllegalArgumentException("Number of edges must be non-negative");
        }
        for (int i = 0; i < E; ++i) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            double capacity = StdRandom.uniformInt(100);
            this.addEdge(new FlowEdge(v, w, capacity));
        }
    }

    public FlowNetwork(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) {
            throw new IllegalArgumentException("number of edges must be non-negative");
        }
        for (int i = 0; i < E; ++i) {
            int v = in.readInt();
            int w = in.readInt();
            this.validateVertex(v);
            this.validateVertex(w);
            double capacity = in.readDouble();
            this.addEdge(new FlowEdge(v, w, capacity));
        }
    }

    public int V() {
        return this.V;
    }

    public int E() {
        return this.E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= this.V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
        }
    }

    public void addEdge(FlowEdge e) {
        int v = e.from();
        int w = e.to();
        this.validateVertex(v);
        this.validateVertex(w);
        this.adj[v].add(e);
        this.adj[w].add(e);
        ++this.E;
    }

    public Iterable<FlowEdge> adj(int v) {
        this.validateVertex(v);
        return this.adj[v];
    }

    public Iterable<FlowEdge> edges() {
        Bag<FlowEdge> list = new Bag<FlowEdge>();
        for (int v = 0; v < this.V; ++v) {
            for (FlowEdge e : this.adj(v)) {
                if (e.to() == v) continue;
                list.add(e);
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.V + " " + this.E + NEWLINE);
        for (int v = 0; v < this.V; ++v) {
            s.append(v + ":  ");
            for (FlowEdge e : this.adj[v]) {
                if (e.to() == v) continue;
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        FlowNetwork G = new FlowNetwork(in);
        StdOut.println(G);
    }
}

