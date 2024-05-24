/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.AdjMatrixEdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.EdgeWeightedDirectedCycle;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class FloydWarshall {
    private boolean hasNegativeCycle;
    private double[][] distTo;
    private DirectedEdge[][] edgeTo;

    public FloydWarshall(AdjMatrixEdgeWeightedDigraph G) {
        int v;
        int V = G.V();
        this.distTo = new double[V][V];
        this.edgeTo = new DirectedEdge[V][V];
        for (v = 0; v < V; ++v) {
            for (int w = 0; w < V; ++w) {
                this.distTo[v][w] = Double.POSITIVE_INFINITY;
            }
        }
        for (v = 0; v < G.V(); ++v) {
            for (DirectedEdge e : G.adj(v)) {
                this.distTo[e.from()][e.to()] = e.weight();
                this.edgeTo[e.from()][e.to()] = e;
            }
            if (!(this.distTo[v][v] >= 0.0)) continue;
            this.distTo[v][v] = 0.0;
            this.edgeTo[v][v] = null;
        }
        for (int i = 0; i < V; ++i) {
            for (int v2 = 0; v2 < V; ++v2) {
                if (this.edgeTo[v2][i] == null) continue;
                for (int w = 0; w < V; ++w) {
                    if (!(this.distTo[v2][w] > this.distTo[v2][i] + this.distTo[i][w])) continue;
                    this.distTo[v2][w] = this.distTo[v2][i] + this.distTo[i][w];
                    this.edgeTo[v2][w] = this.edgeTo[i][w];
                }
                if (!(this.distTo[v2][v2] < 0.0)) continue;
                this.hasNegativeCycle = true;
                return;
            }
        }
        assert (this.check(G));
    }

    public boolean hasNegativeCycle() {
        return this.hasNegativeCycle;
    }

    public Iterable<DirectedEdge> negativeCycle() {
        for (int v = 0; v < this.distTo.length; ++v) {
            if (!(this.distTo[v][v] < 0.0)) continue;
            int V = this.edgeTo.length;
            EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
            for (int w = 0; w < V; ++w) {
                if (this.edgeTo[v][w] == null) continue;
                spt.addEdge(this.edgeTo[v][w]);
            }
            EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
            assert (finder.hasCycle());
            return finder.cycle();
        }
        return null;
    }

    public boolean hasPath(int s, int t) {
        this.validateVertex(s);
        this.validateVertex(t);
        return this.distTo[s][t] < Double.POSITIVE_INFINITY;
    }

    public double dist(int s, int t) {
        this.validateVertex(s);
        this.validateVertex(t);
        if (this.hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cost cycle exists");
        }
        return this.distTo[s][t];
    }

    public Iterable<DirectedEdge> path(int s, int t) {
        this.validateVertex(s);
        this.validateVertex(t);
        if (this.hasNegativeCycle()) {
            throw new UnsupportedOperationException("Negative cost cycle exists");
        }
        if (!this.hasPath(s, t)) {
            return null;
        }
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        DirectedEdge e = this.edgeTo[s][t];
        while (e != null) {
            path.push(e);
            e = this.edgeTo[s][e.from()];
        }
        return path;
    }

    private boolean check(AdjMatrixEdgeWeightedDigraph G) {
        if (!this.hasNegativeCycle()) {
            for (int v = 0; v < G.V(); ++v) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    for (int i = 0; i < G.V(); ++i) {
                        if (!(this.distTo[i][w] > this.distTo[i][v] + e.weight())) continue;
                        System.err.println("edge " + e + " is eligible");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void validateVertex(int v) {
        int V = this.distTo.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        int w;
        int v;
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        AdjMatrixEdgeWeightedDigraph G = new AdjMatrixEdgeWeightedDigraph(V);
        for (int i = 0; i < E; ++i) {
            v = StdRandom.uniformInt(V);
            w = StdRandom.uniformInt(V);
            double weight = 0.01 * (double)StdRandom.uniformInt(-15, 100);
            if (v == w) {
                G.addEdge(new DirectedEdge(v, w, Math.abs(weight)));
                continue;
            }
            G.addEdge(new DirectedEdge(v, w, weight));
        }
        StdOut.println(G);
        FloydWarshall spt = new FloydWarshall(G);
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
        if (spt.hasNegativeCycle()) {
            StdOut.println("Negative cost cycle:");
            for (DirectedEdge e : spt.negativeCycle()) {
                StdOut.println(e);
            }
            StdOut.println();
        } else {
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
}

