/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BipartiteX;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.GraphGenerator;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class HopcroftKarp {
    private static final int UNMATCHED = -1;
    private final int V;
    private BipartiteX bipartition;
    private int cardinality;
    private int[] mate;
    private boolean[] inMinVertexCover;
    private boolean[] marked;
    private int[] distTo;

    public HopcroftKarp(Graph G) {
        int v;
        this.bipartition = new BipartiteX(G);
        if (!this.bipartition.isBipartite()) {
            throw new IllegalArgumentException("graph is not bipartite");
        }
        this.V = G.V();
        this.mate = new int[this.V];
        for (v = 0; v < this.V; ++v) {
            this.mate[v] = -1;
        }
        while (this.hasAugmentingPath(G)) {
            Iterator[] adj = new Iterator[G.V()];
            for (int v2 = 0; v2 < G.V(); ++v2) {
                adj[v2] = G.adj(v2).iterator();
            }
            for (int s = 0; s < this.V; ++s) {
                if (this.isMatched(s) || !this.bipartition.color(s)) continue;
                Stack<Integer> path = new Stack<Integer>();
                path.push(s);
                while (!path.isEmpty()) {
                    int v3 = (Integer)path.peek();
                    if (!adj[v3].hasNext()) {
                        path.pop();
                        continue;
                    }
                    int w = (Integer)adj[v3].next();
                    if (!this.isLevelGraphEdge(v3, w)) continue;
                    path.push(w);
                    if (this.isMatched(w)) continue;
                    while (!path.isEmpty()) {
                        int y;
                        int x = (Integer)path.pop();
                        this.mate[x] = y = ((Integer)path.pop()).intValue();
                        this.mate[y] = x;
                    }
                    ++this.cardinality;
                }
            }
        }
        this.inMinVertexCover = new boolean[this.V];
        for (v = 0; v < this.V; ++v) {
            if (this.bipartition.color(v) && !this.marked[v]) {
                this.inMinVertexCover[v] = true;
            }
            if (this.bipartition.color(v) || !this.marked[v]) continue;
            this.inMinVertexCover[v] = true;
        }
        assert (this.certifySolution(G));
    }

    private static String toString(Iterable<Integer> path) {
        StringBuilder sb = new StringBuilder();
        for (int v : path) {
            sb.append(v + "-");
        }
        String s = sb.toString();
        s = s.substring(0, s.lastIndexOf(45));
        return s;
    }

    private boolean isLevelGraphEdge(int v, int w) {
        return this.distTo[w] == this.distTo[v] + 1 && this.isResidualGraphEdge(v, w);
    }

    private boolean isResidualGraphEdge(int v, int w) {
        if (this.mate[v] != w && this.bipartition.color(v)) {
            return true;
        }
        return this.mate[v] == w && !this.bipartition.color(v);
    }

    private boolean hasAugmentingPath(Graph G) {
        this.marked = new boolean[this.V];
        this.distTo = new int[this.V];
        for (int v = 0; v < this.V; ++v) {
            this.distTo[v] = Integer.MAX_VALUE;
        }
        Queue<Integer> queue = new Queue<Integer>();
        for (int v = 0; v < this.V; ++v) {
            if (!this.bipartition.color(v) || this.isMatched(v)) continue;
            queue.enqueue(v);
            this.marked[v] = true;
            this.distTo[v] = 0;
        }
        boolean hasAugmentingPath = false;
        while (!queue.isEmpty()) {
            int v = (Integer)queue.dequeue();
            for (int w : G.adj(v)) {
                if (!this.isResidualGraphEdge(v, w) || this.marked[w]) continue;
                this.distTo[w] = this.distTo[v] + 1;
                this.marked[w] = true;
                if (!this.isMatched(w)) {
                    hasAugmentingPath = true;
                }
                if (hasAugmentingPath) continue;
                queue.enqueue(w);
            }
        }
        return hasAugmentingPath;
    }

    public int mate(int v) {
        this.validate(v);
        return this.mate[v];
    }

    public boolean isMatched(int v) {
        this.validate(v);
        return this.mate[v] != -1;
    }

    public int size() {
        return this.cardinality;
    }

    public boolean isPerfect() {
        return this.cardinality * 2 == this.V;
    }

    public boolean inMinVertexCover(int v) {
        this.validate(v);
        return this.inMinVertexCover[v];
    }

    private void validate(int v) {
        if (v < 0 || v >= this.V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
        }
    }

    private boolean certifySolution(Graph G) {
        int v;
        for (int v2 = 0; v2 < this.V; ++v2) {
            if (this.mate(v2) == -1 || this.mate(this.mate(v2)) == v2) continue;
            return false;
        }
        int matchedVertices = 0;
        for (int v3 = 0; v3 < this.V; ++v3) {
            if (this.mate(v3) == -1) continue;
            ++matchedVertices;
        }
        if (2 * this.size() != matchedVertices) {
            return false;
        }
        int sizeOfMinVertexCover = 0;
        for (int v4 = 0; v4 < this.V; ++v4) {
            if (!this.inMinVertexCover(v4)) continue;
            ++sizeOfMinVertexCover;
        }
        if (this.size() != sizeOfMinVertexCover) {
            return false;
        }
        boolean[] isMatched = new boolean[this.V];
        for (v = 0; v < this.V; ++v) {
            int w = this.mate[v];
            if (w == -1) continue;
            if (v == w) {
                return false;
            }
            if (v >= w) continue;
            if (isMatched[v] || isMatched[w]) {
                return false;
            }
            isMatched[v] = true;
            isMatched[w] = true;
        }
        for (v = 0; v < this.V; ++v) {
            if (this.mate(v) == -1) continue;
            boolean isEdge = false;
            for (int w : G.adj(v)) {
                if (this.mate(v) != w) continue;
                isEdge = true;
            }
            if (isEdge) continue;
            return false;
        }
        for (v = 0; v < this.V; ++v) {
            for (int w : G.adj(v)) {
                if (this.inMinVertexCover(v) || this.inMinVertexCover(w)) continue;
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int v;
        int E;
        int V2;
        int V1 = Integer.parseInt(args[0]);
        Graph G = GraphGenerator.bipartite(V1, V2 = Integer.parseInt(args[1]), E = Integer.parseInt(args[2]));
        if (G.V() < 1000) {
            StdOut.println(G);
        }
        HopcroftKarp matching = new HopcroftKarp(G);
        StdOut.printf("Number of edges in max matching        = %d\n", matching.size());
        StdOut.printf("Number of vertices in min vertex cover = %d\n", matching.size());
        StdOut.printf("Graph has a perfect matching           = %b\n", matching.isPerfect());
        StdOut.println();
        if (G.V() >= 1000) {
            return;
        }
        StdOut.print("Max matching: ");
        for (v = 0; v < G.V(); ++v) {
            int w = matching.mate(v);
            if (!matching.isMatched(v) || v >= w) continue;
            StdOut.print(v + "-" + w + " ");
        }
        StdOut.println();
        StdOut.print("Min vertex cover: ");
        for (v = 0; v < G.V(); ++v) {
            if (!matching.inMinVertexCover(v)) continue;
            StdOut.print(v + " ");
        }
        StdOut.println();
    }
}

