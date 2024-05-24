/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.GraphGenerator;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Bipartite {
    private boolean isBipartite = true;
    private boolean[] color;
    private boolean[] marked;
    private int[] edgeTo;
    private Stack<Integer> cycle;

    public Bipartite(Graph G) {
        this.color = new boolean[G.V()];
        this.marked = new boolean[G.V()];
        this.edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
        }
        assert (this.check(G));
    }

    private void dfs(Graph G, int v) {
        this.marked[v] = true;
        for (int w : G.adj(v)) {
            if (this.cycle != null) {
                return;
            }
            if (!this.marked[w]) {
                this.edgeTo[w] = v;
                this.color[w] = !this.color[v];
                this.dfs(G, w);
                continue;
            }
            if (this.color[w] != this.color[v]) continue;
            this.isBipartite = false;
            this.cycle = new Stack();
            this.cycle.push(w);
            int x = v;
            while (x != w) {
                this.cycle.push(x);
                x = this.edgeTo[x];
            }
            this.cycle.push(w);
        }
    }

    public boolean isBipartite() {
        return this.isBipartite;
    }

    public boolean color(int v) {
        this.validateVertex(v);
        if (!this.isBipartite) {
            throw new UnsupportedOperationException("graph is not bipartite");
        }
        return this.color[v];
    }

    public Iterable<Integer> oddCycle() {
        return this.cycle;
    }

    private boolean check(Graph G) {
        if (this.isBipartite) {
            for (int v = 0; v < G.V(); ++v) {
                for (int w : G.adj(v)) {
                    if (this.color[v] != this.color[w]) continue;
                    System.err.printf("edge %d-%d with %d and %d in same side of bipartition\n", v, w, v, w);
                    return false;
                }
            }
        } else {
            int first = -1;
            int last = -1;
            for (int v : this.oddCycle()) {
                if (first == -1) {
                    first = v;
                }
                last = v;
            }
            if (first != last) {
                System.err.printf("cycle begins with %d and ends with %d\n", first, last);
                return false;
            }
        }
        return true;
    }

    private void validateVertex(int v) {
        int V = this.marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        int v;
        int V1 = Integer.parseInt(args[0]);
        int V2 = Integer.parseInt(args[1]);
        int E = Integer.parseInt(args[2]);
        int F = Integer.parseInt(args[3]);
        Graph G = GraphGenerator.bipartite(V1, V2, E);
        for (int i = 0; i < F; ++i) {
            v = StdRandom.uniformInt(V1 + V2);
            int w = StdRandom.uniformInt(V1 + V2);
            G.addEdge(v, w);
        }
        StdOut.println(G);
        Bipartite b = new Bipartite(G);
        if (b.isBipartite()) {
            StdOut.println("Graph is bipartite");
            for (v = 0; v < G.V(); ++v) {
                StdOut.println(v + ": " + b.color(v));
            }
        } else {
            StdOut.print("Graph has an odd-length cycle: ");
            for (int x : b.oddCycle()) {
                StdOut.print(x + " ");
            }
            StdOut.println();
        }
    }
}

