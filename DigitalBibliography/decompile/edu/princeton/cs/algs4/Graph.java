/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.NoSuchElementException;

public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;
    private int E;
    private Bag<Integer>[] adj;

    public Graph(int V) {
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

    public Graph(In in) {
        if (in == null) {
            throw new IllegalArgumentException("argument is null");
        }
        try {
            this.V = in.readInt();
            if (this.V < 0) {
                throw new IllegalArgumentException("number of vertices in a Graph must be non-negative");
            }
            this.adj = new Bag[this.V];
            for (int v = 0; v < this.V; ++v) {
                this.adj[v] = new Bag();
            }
            int E = in.readInt();
            if (E < 0) {
                throw new IllegalArgumentException("number of edges in a Graph must be non-negative");
            }
            for (int i = 0; i < E; ++i) {
                int v = in.readInt();
                int w = in.readInt();
                this.validateVertex(v);
                this.validateVertex(w);
                this.addEdge(v, w);
            }
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }

    public Graph(Graph G) {
        int v;
        this.V = G.V();
        this.E = G.E();
        if (this.V < 0) {
            throw new IllegalArgumentException("Number of vertices must be non-negative");
        }
        this.adj = new Bag[this.V];
        for (v = 0; v < this.V; ++v) {
            this.adj[v] = new Bag();
        }
        for (v = 0; v < G.V(); ++v) {
            Stack<Integer> reverse = new Stack<Integer>();
            for (int w : G.adj[v]) {
                reverse.push(w);
            }
            for (int w : reverse) {
                this.adj[v].add(w);
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

    public void addEdge(int v, int w) {
        this.validateVertex(v);
        this.validateVertex(w);
        ++this.E;
        this.adj[v].add(w);
        this.adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        this.validateVertex(v);
        return this.adj[v];
    }

    public int degree(int v) {
        this.validateVertex(v);
        return this.adj[v].size();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.V + " vertices, " + this.E + " edges " + NEWLINE);
        for (int v = 0; v < this.V; ++v) {
            s.append(v + ": ");
            for (int w : this.adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Graph G = new Graph(in);
        StdOut.println(G);
    }
}

