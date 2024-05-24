/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.GraphGenerator;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class EulerianCycle {
    private Stack<Integer> cycle = new Stack();

    public EulerianCycle(Graph G) {
        int v;
        if (G.E() == 0) {
            return;
        }
        for (int v2 = 0; v2 < G.V(); ++v2) {
            if (G.degree(v2) % 2 == 0) continue;
            return;
        }
        Queue[] adj = new Queue[G.V()];
        for (v = 0; v < G.V(); ++v) {
            adj[v] = new Queue();
        }
        for (v = 0; v < G.V(); ++v) {
            int selfLoops = 0;
            for (int w : G.adj(v)) {
                Edge e;
                if (v == w) {
                    if (selfLoops % 2 == 0) {
                        e = new Edge(v, w);
                        adj[v].enqueue(e);
                        adj[w].enqueue(e);
                    }
                    ++selfLoops;
                    continue;
                }
                if (v >= w) continue;
                e = new Edge(v, w);
                adj[v].enqueue(e);
                adj[w].enqueue(e);
            }
        }
        int s = EulerianCycle.nonIsolatedVertex(G);
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(s);
        this.cycle = new Stack();
        while (!stack.isEmpty()) {
            int v3 = (Integer)stack.pop();
            while (!adj[v3].isEmpty()) {
                Edge edge = (Edge)adj[v3].dequeue();
                if (edge.isUsed) continue;
                edge.isUsed = true;
                stack.push(v3);
                v3 = edge.other(v3);
            }
            this.cycle.push(v3);
        }
        if (this.cycle.size() != G.E() + 1) {
            this.cycle = null;
        }
        assert (this.certifySolution(G));
    }

    public Iterable<Integer> cycle() {
        return this.cycle;
    }

    public boolean hasEulerianCycle() {
        return this.cycle != null;
    }

    private static int nonIsolatedVertex(Graph G) {
        for (int v = 0; v < G.V(); ++v) {
            if (G.degree(v) <= 0) continue;
            return v;
        }
        return -1;
    }

    private static boolean satisfiesNecessaryAndSufficientConditions(Graph G) {
        if (G.E() == 0) {
            return false;
        }
        for (int v = 0; v < G.V(); ++v) {
            if (G.degree(v) % 2 == 0) continue;
            return false;
        }
        int s = EulerianCycle.nonIsolatedVertex(G);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);
        for (int v = 0; v < G.V(); ++v) {
            if (G.degree(v) <= 0 || bfs.hasPathTo(v)) continue;
            return false;
        }
        return true;
    }

    private boolean certifySolution(Graph G) {
        if (this.hasEulerianCycle() == (this.cycle() == null)) {
            return false;
        }
        if (this.hasEulerianCycle() != EulerianCycle.satisfiesNecessaryAndSufficientConditions(G)) {
            return false;
        }
        if (this.cycle == null) {
            return true;
        }
        if (this.cycle.size() != G.E() + 1) {
            return false;
        }
        int first = -1;
        int last = -1;
        for (int v : this.cycle()) {
            if (first == -1) {
                first = v;
            }
            last = v;
        }
        return first == last;
    }

    private static void unitTest(Graph G, String description) {
        StdOut.println(description);
        StdOut.println("-------------------------------------");
        StdOut.print(G);
        EulerianCycle euler = new EulerianCycle(G);
        StdOut.print("Eulerian cycle: ");
        if (euler.hasEulerianCycle()) {
            for (int v : euler.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("none");
        }
        StdOut.println();
    }

    public static void main(String[] args) {
        int v;
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Graph G1 = GraphGenerator.eulerianCycle(V, E);
        EulerianCycle.unitTest(G1, "Eulerian cycle");
        Graph G2 = GraphGenerator.eulerianPath(V, E);
        EulerianCycle.unitTest(G2, "Eulerian path");
        Graph G3 = new Graph(V);
        EulerianCycle.unitTest(G3, "empty graph");
        Graph G4 = new Graph(V);
        int v4 = StdRandom.uniformInt(V);
        G4.addEdge(v4, v4);
        EulerianCycle.unitTest(G4, "single self loop");
        Graph H1 = GraphGenerator.eulerianCycle(V / 2, E / 2);
        Graph H2 = GraphGenerator.eulerianCycle(V - V / 2, E - E / 2);
        int[] perm = new int[V];
        for (int i = 0; i < V; ++i) {
            perm[i] = i;
        }
        StdRandom.shuffle(perm);
        Graph G5 = new Graph(V);
        for (v = 0; v < H1.V(); ++v) {
            for (int w : H1.adj(v)) {
                G5.addEdge(perm[v], perm[w]);
            }
        }
        for (v = 0; v < H2.V(); ++v) {
            for (int w : H2.adj(v)) {
                G5.addEdge(perm[V / 2 + v], perm[V / 2 + w]);
            }
        }
        EulerianCycle.unitTest(G5, "Union of two disjoint cycles");
        Graph G6 = GraphGenerator.simple(V, E);
        EulerianCycle.unitTest(G6, "simple graph");
    }

    private static class Edge {
        private final int v;
        private final int w;
        private boolean isUsed;

        public Edge(int v, int w) {
            this.v = v;
            this.w = w;
            this.isUsed = false;
        }

        public int other(int vertex) {
            if (vertex == this.v) {
                return this.w;
            }
            if (vertex == this.w) {
                return this.v;
            }
            throw new IllegalArgumentException("Illegal endpoint");
        }
    }
}

