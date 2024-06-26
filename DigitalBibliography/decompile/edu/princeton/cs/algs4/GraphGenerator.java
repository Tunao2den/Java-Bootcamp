/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class GraphGenerator {
    private GraphGenerator() {
    }

    public static Graph simple(int V, int E) {
        if ((long)E > (long)V * (long)(V - 1) / 2L) {
            throw new IllegalArgumentException("Too many edges");
        }
        if (E < 0) {
            throw new IllegalArgumentException("Too few edges");
        }
        Graph G = new Graph(V);
        SET<Edge> set = new SET<Edge>();
        while (G.E() < E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            Edge e = new Edge(v, w);
            if (v == w || set.contains(e)) continue;
            set.add(e);
            G.addEdge(v, w);
        }
        return G;
    }

    public static Graph simple(int V, double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }
        Graph G = new Graph(V);
        for (int v = 0; v < V; ++v) {
            for (int w = v + 1; w < V; ++w) {
                if (!StdRandom.bernoulli(p)) continue;
                G.addEdge(v, w);
            }
        }
        return G;
    }

    public static Graph complete(int V) {
        return GraphGenerator.simple(V, 1.0);
    }

    public static Graph completeBipartite(int V1, int V2) {
        return GraphGenerator.bipartite(V1, V2, V1 * V2);
    }

    public static Graph bipartite(int V1, int V2, int E) {
        if ((long)E > (long)V1 * (long)V2) {
            throw new IllegalArgumentException("Too many edges");
        }
        if (E < 0) {
            throw new IllegalArgumentException("Too few edges");
        }
        Graph G = new Graph(V1 + V2);
        int[] vertices = new int[V1 + V2];
        for (int i = 0; i < V1 + V2; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        SET<Edge> set = new SET<Edge>();
        while (G.E() < E) {
            int j;
            int i = StdRandom.uniformInt(V1);
            Edge e = new Edge(vertices[i], vertices[j = V1 + StdRandom.uniformInt(V2)]);
            if (set.contains(e)) continue;
            set.add(e);
            G.addEdge(vertices[i], vertices[j]);
        }
        return G;
    }

    public static Graph bipartite(int V1, int V2, double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }
        int[] vertices = new int[V1 + V2];
        for (int i = 0; i < V1 + V2; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        Graph G = new Graph(V1 + V2);
        for (int i = 0; i < V1; ++i) {
            for (int j = 0; j < V2; ++j) {
                if (!StdRandom.bernoulli(p)) continue;
                G.addEdge(vertices[i], vertices[V1 + j]);
            }
        }
        return G;
    }

    public static Graph path(int V) {
        int i;
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 0; i < V - 1; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        return G;
    }

    public static Graph binaryTree(int V) {
        int i;
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 1; i < V; ++i) {
            G.addEdge(vertices[i], vertices[(i - 1) / 2]);
        }
        return G;
    }

    public static Graph cycle(int V) {
        int i;
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 0; i < V - 1; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        G.addEdge(vertices[V - 1], vertices[0]);
        return G;
    }

    public static Graph eulerianCycle(int V, int E) {
        int i;
        if (E <= 0) {
            throw new IllegalArgumentException("An Eulerian cycle must have at least one edge");
        }
        if (V <= 0) {
            throw new IllegalArgumentException("An Eulerian cycle must have at least one vertex");
        }
        Graph G = new Graph(V);
        int[] vertices = new int[E];
        for (i = 0; i < E; ++i) {
            vertices[i] = StdRandom.uniformInt(V);
        }
        for (i = 0; i < E - 1; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        G.addEdge(vertices[E - 1], vertices[0]);
        return G;
    }

    public static Graph eulerianPath(int V, int E) {
        int i;
        if (E < 0) {
            throw new IllegalArgumentException("negative number of edges");
        }
        if (V <= 0) {
            throw new IllegalArgumentException("An Eulerian path must have at least one vertex");
        }
        Graph G = new Graph(V);
        int[] vertices = new int[E + 1];
        for (i = 0; i < E + 1; ++i) {
            vertices[i] = StdRandom.uniformInt(V);
        }
        for (i = 0; i < E; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        return G;
    }

    public static Graph wheel(int V) {
        int i;
        if (V <= 1) {
            throw new IllegalArgumentException("Number of vertices must be at least 2");
        }
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 1; i < V - 1; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        G.addEdge(vertices[V - 1], vertices[1]);
        for (i = 1; i < V; ++i) {
            G.addEdge(vertices[0], vertices[i]);
        }
        return G;
    }

    public static Graph star(int V) {
        int i;
        if (V <= 0) {
            throw new IllegalArgumentException("Number of vertices must be at least 1");
        }
        Graph G = new Graph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 1; i < V; ++i) {
            G.addEdge(vertices[0], vertices[i]);
        }
        return G;
    }

    public static Graph regular(int V, int k) {
        if (V * k % 2 != 0) {
            throw new IllegalArgumentException("Number of vertices * k must be even");
        }
        Graph G = new Graph(V);
        int[] vertices = new int[V * k];
        for (int v = 0; v < V; ++v) {
            for (int j = 0; j < k; ++j) {
                vertices[v + V * j] = v;
            }
        }
        StdRandom.shuffle(vertices);
        for (int i = 0; i < V * k / 2; ++i) {
            G.addEdge(vertices[2 * i], vertices[2 * i + 1]);
        }
        return G;
    }

    public static Graph tree(int V) {
        Graph G = new Graph(V);
        if (V == 1) {
            return G;
        }
        int[] prufer = new int[V - 2];
        for (int i = 0; i < V - 2; ++i) {
            prufer[i] = StdRandom.uniformInt(V);
        }
        int[] degree = new int[V];
        for (int v = 0; v < V; ++v) {
            degree[v] = 1;
        }
        for (int i = 0; i < V - 2; ++i) {
            int n = prufer[i];
            degree[n] = degree[n] + 1;
        }
        MinPQ<Integer> pq = new MinPQ<Integer>();
        for (int v = 0; v < V; ++v) {
            if (degree[v] != 1) continue;
            pq.insert(v);
        }
        for (int i = 0; i < V - 2; ++i) {
            int v = (Integer)pq.delMin();
            G.addEdge(v, prufer[i]);
            int n = v;
            degree[n] = degree[n] - 1;
            int n2 = prufer[i];
            degree[n2] = degree[n2] - 1;
            if (degree[prufer[i]] != 1) continue;
            pq.insert(prufer[i]);
        }
        G.addEdge((Integer)pq.delMin(), (Integer)pq.delMin());
        return G;
    }

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int V1 = V / 2;
        int V2 = V - V1;
        StdOut.println("complete graph");
        StdOut.println(GraphGenerator.complete(V));
        StdOut.println();
        StdOut.println("simple");
        StdOut.println(GraphGenerator.simple(V, E));
        StdOut.println();
        StdOut.println("Erdos-Renyi");
        double p = (double)E / ((double)(V * (V - 1)) / 2.0);
        StdOut.println(GraphGenerator.simple(V, p));
        StdOut.println();
        StdOut.println("complete bipartite");
        StdOut.println(GraphGenerator.completeBipartite(V1, V2));
        StdOut.println();
        StdOut.println("bipartite");
        StdOut.println(GraphGenerator.bipartite(V1, V2, E));
        StdOut.println();
        StdOut.println("Erdos Renyi bipartite");
        double q = (double)E / (double)(V1 * V2);
        StdOut.println(GraphGenerator.bipartite(V1, V2, q));
        StdOut.println();
        StdOut.println("path");
        StdOut.println(GraphGenerator.path(V));
        StdOut.println();
        StdOut.println("cycle");
        StdOut.println(GraphGenerator.cycle(V));
        StdOut.println();
        StdOut.println("binary tree");
        StdOut.println(GraphGenerator.binaryTree(V));
        StdOut.println();
        StdOut.println("tree");
        StdOut.println(GraphGenerator.tree(V));
        StdOut.println();
        StdOut.println("4-regular");
        StdOut.println(GraphGenerator.regular(V, 4));
        StdOut.println();
        StdOut.println("star");
        StdOut.println(GraphGenerator.star(V));
        StdOut.println();
        StdOut.println("wheel");
        StdOut.println(GraphGenerator.wheel(V));
        StdOut.println();
    }

    private static final class Edge
    implements Comparable<Edge> {
        private int v;
        private int w;

        private Edge(int v, int w) {
            if (v < w) {
                this.v = v;
                this.w = w;
            } else {
                this.v = w;
                this.w = v;
            }
        }

        @Override
        public int compareTo(Edge that) {
            if (this.v < that.v) {
                return -1;
            }
            if (this.v > that.v) {
                return 1;
            }
            if (this.w < that.w) {
                return -1;
            }
            if (this.w > that.w) {
                return 1;
            }
            return 0;
        }
    }
}

