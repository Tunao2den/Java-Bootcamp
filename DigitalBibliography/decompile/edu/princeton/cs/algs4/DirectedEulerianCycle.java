/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DigraphGenerator;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class DirectedEulerianCycle {
    private Stack<Integer> cycle = null;

    public DirectedEulerianCycle(Digraph G) {
        if (G.E() == 0) {
            return;
        }
        for (int v = 0; v < G.V(); ++v) {
            if (G.outdegree(v) == G.indegree(v)) continue;
            return;
        }
        Iterator[] adj = new Iterator[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            adj[v] = G.adj(v).iterator();
        }
        int s = DirectedEulerianCycle.nonIsolatedVertex(G);
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(s);
        this.cycle = new Stack();
        while (!stack.isEmpty()) {
            int v = (Integer)stack.pop();
            while (adj[v].hasNext()) {
                stack.push(v);
                v = (Integer)adj[v].next();
            }
            this.cycle.push(v);
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

    private static int nonIsolatedVertex(Digraph G) {
        for (int v = 0; v < G.V(); ++v) {
            if (G.outdegree(v) <= 0) continue;
            return v;
        }
        return -1;
    }

    private static boolean satisfiesNecessaryAndSufficientConditions(Digraph G) {
        if (G.E() == 0) {
            return false;
        }
        for (int v = 0; v < G.V(); ++v) {
            if (G.outdegree(v) == G.indegree(v)) continue;
            return false;
        }
        Graph H = new Graph(G.V());
        for (int v = 0; v < G.V(); ++v) {
            for (int w : G.adj(v)) {
                H.addEdge(v, w);
            }
        }
        int s = DirectedEulerianCycle.nonIsolatedVertex(G);
        BreadthFirstPaths bfs = new BreadthFirstPaths(H, s);
        for (int v = 0; v < G.V(); ++v) {
            if (H.degree(v) <= 0 || bfs.hasPathTo(v)) continue;
            return false;
        }
        return true;
    }

    private boolean certifySolution(Digraph G) {
        if (this.hasEulerianCycle() == (this.cycle() == null)) {
            return false;
        }
        if (this.hasEulerianCycle() != DirectedEulerianCycle.satisfiesNecessaryAndSufficientConditions(G)) {
            return false;
        }
        if (this.cycle == null) {
            return true;
        }
        return this.cycle.size() == G.E() + 1;
    }

    private static void unitTest(Digraph G, String description) {
        StdOut.println(description);
        StdOut.println("-------------------------------------");
        StdOut.print(G);
        DirectedEulerianCycle euler = new DirectedEulerianCycle(G);
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
        Digraph G1 = DigraphGenerator.eulerianCycle(V, E);
        DirectedEulerianCycle.unitTest(G1, "Eulerian cycle");
        Digraph G2 = DigraphGenerator.eulerianPath(V, E);
        DirectedEulerianCycle.unitTest(G2, "Eulerian path");
        Digraph G3 = new Digraph(V);
        DirectedEulerianCycle.unitTest(G3, "empty digraph");
        Digraph G4 = new Digraph(V);
        int v4 = StdRandom.uniformInt(V);
        G4.addEdge(v4, v4);
        DirectedEulerianCycle.unitTest(G4, "single self loop");
        Digraph H1 = DigraphGenerator.eulerianCycle(V / 2, E / 2);
        Digraph H2 = DigraphGenerator.eulerianCycle(V - V / 2, E - E / 2);
        int[] perm = new int[V];
        for (int i = 0; i < V; ++i) {
            perm[i] = i;
        }
        StdRandom.shuffle(perm);
        Digraph G5 = new Digraph(V);
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
        DirectedEulerianCycle.unitTest(G5, "Union of two disjoint cycles");
        Digraph G6 = DigraphGenerator.simple(V, E);
        DirectedEulerianCycle.unitTest(G6, "simple digraph");
        Digraph G7 = new Digraph(new In("eulerianD.txt"));
        DirectedEulerianCycle.unitTest(G7, "4-vertex Eulerian digraph");
    }
}

