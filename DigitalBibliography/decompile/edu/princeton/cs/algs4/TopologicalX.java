/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DigraphGenerator;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class TopologicalX {
    private Queue<Integer> order;
    private int[] ranks;

    public TopologicalX(Digraph G) {
        int v;
        int[] indegree = new int[G.V()];
        for (int v2 = 0; v2 < G.V(); ++v2) {
            indegree[v2] = G.indegree(v2);
        }
        this.ranks = new int[G.V()];
        this.order = new Queue();
        int count = 0;
        Queue<Integer> queue = new Queue<Integer>();
        for (v = 0; v < G.V(); ++v) {
            if (indegree[v] != 0) continue;
            queue.enqueue(v);
        }
        while (!queue.isEmpty()) {
            v = (Integer)queue.dequeue();
            this.order.enqueue(v);
            this.ranks[v] = count++;
            Iterator<Integer> iterator = G.adj(v).iterator();
            while (iterator.hasNext()) {
                int w;
                int n = w = iterator.next().intValue();
                indegree[n] = indegree[n] - 1;
                if (indegree[w] != 0) continue;
                queue.enqueue(w);
            }
        }
        if (count != G.V()) {
            this.order = null;
        }
        assert (this.check(G));
    }

    public TopologicalX(EdgeWeightedDigraph G) {
        int v;
        int[] indegree = new int[G.V()];
        for (int v2 = 0; v2 < G.V(); ++v2) {
            indegree[v2] = G.indegree(v2);
        }
        this.ranks = new int[G.V()];
        this.order = new Queue();
        int count = 0;
        Queue<Integer> queue = new Queue<Integer>();
        for (v = 0; v < G.V(); ++v) {
            if (indegree[v] != 0) continue;
            queue.enqueue(v);
        }
        while (!queue.isEmpty()) {
            v = (Integer)queue.dequeue();
            this.order.enqueue(v);
            this.ranks[v] = count++;
            for (DirectedEdge e : G.adj(v)) {
                int w;
                int n = w = e.to();
                indegree[n] = indegree[n] - 1;
                if (indegree[w] != 0) continue;
                queue.enqueue(w);
            }
        }
        if (count != G.V()) {
            this.order = null;
        }
        assert (this.check(G));
    }

    public Iterable<Integer> order() {
        return this.order;
    }

    public boolean hasOrder() {
        return this.order != null;
    }

    public int rank(int v) {
        this.validateVertex(v);
        if (this.hasOrder()) {
            return this.ranks[v];
        }
        return -1;
    }

    private boolean check(Digraph G) {
        if (this.hasOrder()) {
            int i;
            boolean[] found = new boolean[G.V()];
            for (i = 0; i < G.V(); ++i) {
                found[this.rank((int)i)] = true;
            }
            for (i = 0; i < G.V(); ++i) {
                if (found[i]) continue;
                System.err.println("No vertex with rank " + i);
                return false;
            }
            for (int v = 0; v < G.V(); ++v) {
                for (int w : G.adj(v)) {
                    if (this.rank(v) <= this.rank(w)) continue;
                    System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n", v, w, v, this.rank(v), w, this.rank(w));
                    return false;
                }
            }
            int r = 0;
            for (int v : this.order()) {
                if (this.rank(v) != r) {
                    System.err.println("order() and rank() inconsistent");
                    return false;
                }
                ++r;
            }
        }
        return true;
    }

    private boolean check(EdgeWeightedDigraph G) {
        if (this.hasOrder()) {
            int i;
            boolean[] found = new boolean[G.V()];
            for (i = 0; i < G.V(); ++i) {
                found[this.rank((int)i)] = true;
            }
            for (i = 0; i < G.V(); ++i) {
                if (found[i]) continue;
                System.err.println("No vertex with rank " + i);
                return false;
            }
            for (int v = 0; v < G.V(); ++v) {
                for (DirectedEdge e : G.adj(v)) {
                    int w = e.to();
                    if (this.rank(v) <= this.rank(w)) continue;
                    System.err.printf("%d-%d: rank(%d) = %d, rank(%d) = %d\n", v, w, v, this.rank(v), w, this.rank(w));
                    return false;
                }
            }
            int r = 0;
            Iterator<Object> iterator = this.order().iterator();
            while (iterator.hasNext()) {
                int v = (Integer)iterator.next();
                if (this.rank(v) != r) {
                    System.err.println("order() and rank() inconsistent");
                    return false;
                }
                ++r;
            }
        }
        return true;
    }

    private void validateVertex(int v) {
        int V = this.ranks.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        Digraph G1 = DigraphGenerator.dag(V, E);
        EdgeWeightedDigraph G2 = new EdgeWeightedDigraph(V);
        for (int v = 0; v < G1.V(); ++v) {
            for (int w : G1.adj(v)) {
                G2.addEdge(new DirectedEdge(v, w, 0.0));
            }
        }
        for (int i = 0; i < F; ++i) {
            int w;
            int v = StdRandom.uniformInt(V);
            w = StdRandom.uniformInt(V);
            G1.addEdge(v, w);
            G2.addEdge(new DirectedEdge(v, w, 0.0));
        }
        StdOut.println(G1);
        StdOut.println();
        StdOut.println(G2);
        TopologicalX topological1 = new TopologicalX(G1);
        if (!topological1.hasOrder()) {
            StdOut.println("Not a DAG");
        } else {
            StdOut.print("Topological order: ");
            for (int v : topological1.order()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        TopologicalX topological2 = new TopologicalX(G2);
        if (!topological2.hasOrder()) {
            StdOut.println("Not a DAG");
        } else {
            StdOut.print("Topological order: ");
            for (int v : topological2.order()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
    }
}

