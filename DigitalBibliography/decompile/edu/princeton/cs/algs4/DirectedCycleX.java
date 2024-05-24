/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DigraphGenerator;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class DirectedCycleX {
    private Stack<Integer> cycle;

    public DirectedCycleX(Digraph G) {
        int v;
        int[] indegree = new int[G.V()];
        for (int v2 = 0; v2 < G.V(); ++v2) {
            indegree[v2] = G.indegree(v2);
        }
        Queue<Integer> queue = new Queue<Integer>();
        for (v = 0; v < G.V(); ++v) {
            if (indegree[v] != 0) continue;
            queue.enqueue(v);
        }
        while (!queue.isEmpty()) {
            v = (Integer)queue.dequeue();
            Iterator<Integer> iterator = G.adj(v).iterator();
            while (iterator.hasNext()) {
                int w;
                int n = w = iterator.next().intValue();
                indegree[n] = indegree[n] - 1;
                if (indegree[w] != 0) continue;
                queue.enqueue(w);
            }
        }
        int[] edgeTo = new int[G.V()];
        int root = -1;
        for (int v3 = 0; v3 < G.V(); ++v3) {
            if (indegree[v3] == 0) continue;
            root = v3;
            for (int w : G.adj(v3)) {
                if (indegree[w] <= 0) continue;
                edgeTo[w] = v3;
            }
        }
        if (root != -1) {
            boolean[] visited = new boolean[G.V()];
            while (!visited[root]) {
                visited[root] = true;
                root = edgeTo[root];
            }
            this.cycle = new Stack();
            int v4 = root;
            do {
                this.cycle.push(v4);
            } while ((v4 = edgeTo[v4]) != root);
            this.cycle.push(root);
        }
        assert (this.check());
    }

    public Iterable<Integer> cycle() {
        return this.cycle;
    }

    public boolean hasCycle() {
        return this.cycle != null;
    }

    private boolean check() {
        if (this.hasCycle()) {
            int first = -1;
            int last = -1;
            for (int v : this.cycle()) {
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

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int F = Integer.parseInt(args[2]);
        Digraph G = DigraphGenerator.dag(V, E);
        for (int i = 0; i < F; ++i) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            G.addEdge(v, w);
        }
        StdOut.println(G);
        DirectedCycleX finder = new DirectedCycleX(G);
        if (finder.hasCycle()) {
            StdOut.print("Directed cycle: ");
            for (int v : finder.cycle()) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        } else {
            StdOut.println("No directed cycle");
        }
        StdOut.println();
    }
}

