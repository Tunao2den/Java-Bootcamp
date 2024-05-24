/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BreadthFirstDirectedPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;

    public BreadthFirstDirectedPaths(Digraph G, int s) {
        this.marked = new boolean[G.V()];
        this.distTo = new int[G.V()];
        this.edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            this.distTo[v] = Integer.MAX_VALUE;
        }
        this.validateVertex(s);
        this.bfs(G, s);
    }

    public BreadthFirstDirectedPaths(Digraph G, Iterable<Integer> sources) {
        this.marked = new boolean[G.V()];
        this.distTo = new int[G.V()];
        this.edgeTo = new int[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            this.distTo[v] = Integer.MAX_VALUE;
        }
        this.validateVertices(sources);
        this.bfs(G, sources);
    }

    private void bfs(Digraph G, int s) {
        Queue<Integer> q = new Queue<Integer>();
        this.marked[s] = true;
        this.distTo[s] = 0;
        q.enqueue(s);
        while (!q.isEmpty()) {
            int v = (Integer)q.dequeue();
            for (int w : G.adj(v)) {
                if (this.marked[w]) continue;
                this.edgeTo[w] = v;
                this.distTo[w] = this.distTo[v] + 1;
                this.marked[w] = true;
                q.enqueue(w);
            }
        }
    }

    private void bfs(Digraph G, Iterable<Integer> sources) {
        Queue<Integer> q = new Queue<Integer>();
        for (int s : sources) {
            this.marked[s] = true;
            this.distTo[s] = 0;
            q.enqueue(s);
        }
        while (!q.isEmpty()) {
            int v = (Integer)q.dequeue();
            for (int w : G.adj(v)) {
                if (this.marked[w]) continue;
                this.edgeTo[w] = v;
                this.distTo[w] = this.distTo[v] + 1;
                this.marked[w] = true;
                q.enqueue(w);
            }
        }
    }

    public boolean hasPathTo(int v) {
        this.validateVertex(v);
        return this.marked[v];
    }

    public int distTo(int v) {
        this.validateVertex(v);
        return this.distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        this.validateVertex(v);
        if (!this.hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<Integer>();
        int x = v;
        while (this.distTo[x] != 0) {
            path.push(x);
            x = this.edgeTo[x];
        }
        path.push(x);
        return path;
    }

    private void validateVertex(int v) {
        int V = this.marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int vertexCount = 0;
        for (Integer v : vertices) {
            ++vertexCount;
            if (v == null) {
                throw new IllegalArgumentException("vertex is null");
            }
            this.validateVertex(v);
        }
        if (vertexCount == 0) {
            throw new IllegalArgumentException("zero vertices");
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        int s = Integer.parseInt(args[1]);
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, s);
        for (int v = 0; v < G.V(); ++v) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) {
                        StdOut.print(x);
                        continue;
                    }
                    StdOut.print("->" + x);
                }
                StdOut.println();
                continue;
            }
            StdOut.printf("%d to %d (-):  not connected\n", s, v);
        }
    }
}

