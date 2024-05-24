/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.NoSuchElementException;

public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Bag<Edge>[] adj;

    public EdgeWeightedGraph(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Number of vertices must be non-negative");
        }
        this.V = V;
        this.E = 0;
        this.adj = new Bag[V];
        for (int v = 0; v < V; ++v) {
            this.adj[v] = new Bag();
        }
    }

    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) {
            throw new IllegalArgumentException("Number of edges must be non-negative");
        }
        for (int i = 0; i < E; ++i) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            double weight = 0.01 * (double)StdRandom.uniformInt(0, 100);
            Edge e = new Edge(v, w, weight);
            this.addEdge(e);
        }
    }

    public EdgeWeightedGraph(In in) {
        if (in == null) {
            throw new IllegalArgumentException("argument is null");
        }
        try {
            this.V = in.readInt();
            this.adj = new Bag[this.V];
            for (int v = 0; v < this.V; ++v) {
                this.adj[v] = new Bag();
            }
            int E = in.readInt();
            if (E < 0) {
                throw new IllegalArgumentException("Number of edges must be non-negative");
            }
            for (int i = 0; i < E; ++i) {
                int v = in.readInt();
                int w = in.readInt();
                this.validateVertex(v);
                this.validateVertex(w);
                double weight = in.readDouble();
                Edge e = new Edge(v, w, weight);
                this.addEdge(e);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in EdgeWeightedGraph constructor", e);
        }
    }

    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); ++v) {
            Stack<Edge> reverse = new Stack<Edge>();
            for (Edge e : G.adj[v]) {
                reverse.push(e);
            }
            for (Edge e : reverse) {
                this.adj[v].add(e);
            }
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

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        this.validateVertex(v);
        this.validateVertex(w);
        this.adj[v].add(e);
        this.adj[w].add(e);
        ++this.E;
    }

    public Iterable<Edge> adj(int v) {
        this.validateVertex(v);
        return this.adj[v];
    }

    public int degree(int v) {
        this.validateVertex(v);
        return this.adj[v].size();
    }

    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < this.V; ++v) {
            int selfLoops = 0;
            for (Edge e : this.adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                    continue;
                }
                if (e.other(v) != v) continue;
                if (selfLoops % 2 == 0) {
                    list.add(e);
                }
                ++selfLoops;
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.V + " " + this.E + NEWLINE);
        for (int v = 0; v < this.V; ++v) {
            s.append(v + ": ");
            for (Edge e : this.adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        StdOut.println(G);
    }
}

