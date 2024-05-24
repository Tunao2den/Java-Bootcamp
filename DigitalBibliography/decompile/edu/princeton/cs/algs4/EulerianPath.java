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

public class EulerianPath {
    private Stack<Integer> path = null;

    public EulerianPath(Graph G) {
        int v;
        int oddDegreeVertices = 0;
        int s = EulerianPath.nonIsolatedVertex(G);
        for (int v2 = 0; v2 < G.V(); ++v2) {
            if (G.degree(v2) % 2 == 0) continue;
            ++oddDegreeVertices;
            s = v2;
        }
        if (oddDegreeVertices > 2) {
            return;
        }
        if (s == -1) {
            s = 0;
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
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(s);
        this.path = new Stack();
        while (!stack.isEmpty()) {
            int v3 = (Integer)stack.pop();
            while (!adj[v3].isEmpty()) {
                Edge edge = (Edge)adj[v3].dequeue();
                if (edge.isUsed) continue;
                edge.isUsed = true;
                stack.push(v3);
                v3 = edge.other(v3);
            }
            this.path.push(v3);
        }
        if (this.path.size() != G.E() + 1) {
            this.path = null;
        }
        assert (this.certifySolution(G));
    }

    public Iterable<Integer> path() {
        return this.path;
    }

    public boolean hasEulerianPath() {
        return this.path != null;
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
            return true;
        }
        int oddDegreeVertices = 0;
        for (int v = 0; v < G.V(); ++v) {
            if (G.degree(v) % 2 == 0) continue;
            ++oddDegreeVertices;
        }
        if (oddDegreeVertices > 2) {
            return false;
        }
        int s = EulerianPath.nonIsolatedVertex(G);
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);
        for (int v = 0; v < G.V(); ++v) {
            if (G.degree(v) <= 0 || bfs.hasPathTo(v)) continue;
            return false;
        }
        return true;
    }

    private boolean certifySolution(Graph G) {
        if (this.hasEulerianPath() == (this.path() == null)) {
            return false;
        }
        if (this.hasEulerianPath() != EulerianPath.satisfiesNecessaryAndSufficientConditions(G)) {
            return false;
        }
        if (this.path == null) {
            return true;
        }
        return this.path.size() == G.E() + 1;
    }

    private static void unitTest(Graph G, String description) {
        StdOut.println(description);
        StdOut.println("-------------------------------------");
        StdOut.print(G);
        EulerianPath euler = new EulerianPath(G);
        StdOut.print("Eulerian path:  ");
        if (euler.hasEulerianPath()) {
            for (int v : euler.path()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("none");
        }
        StdOut.println();
    }

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        Graph G1 = GraphGenerator.eulerianCycle(V, E);
        EulerianPath.unitTest(G1, "Eulerian cycle");
        Graph G2 = GraphGenerator.eulerianPath(V, E);
        EulerianPath.unitTest(G2, "Eulerian path");
        Graph G3 = new Graph(G2);
        G3.addEdge(StdRandom.uniformInt(V), StdRandom.uniformInt(V));
        EulerianPath.unitTest(G3, "one random edge added to Eulerian path");
        Graph G4 = new Graph(V);
        int v4 = StdRandom.uniformInt(V);
        G4.addEdge(v4, v4);
        EulerianPath.unitTest(G4, "single self loop");
        Graph G5 = new Graph(V);
        G5.addEdge(StdRandom.uniformInt(V), StdRandom.uniformInt(V));
        EulerianPath.unitTest(G5, "single edge");
        Graph G6 = new Graph(V);
        EulerianPath.unitTest(G6, "empty graph");
        Graph G7 = GraphGenerator.simple(V, E);
        EulerianPath.unitTest(G7, "simple graph");
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

