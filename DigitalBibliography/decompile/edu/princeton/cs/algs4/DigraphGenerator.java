/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class DigraphGenerator {
    private DigraphGenerator() {
    }

    public static Digraph simple(int V, int E) {
        if ((long)E > (long)V * (long)(V - 1)) {
            throw new IllegalArgumentException("Too many edges");
        }
        if (E < 0) {
            throw new IllegalArgumentException("Too few edges");
        }
        Digraph G = new Digraph(V);
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

    public static Digraph simple(int V, double p) {
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }
        Digraph G = new Digraph(V);
        for (int v = 0; v < V; ++v) {
            for (int w = 0; w < V; ++w) {
                if (v == w || !StdRandom.bernoulli(p)) continue;
                G.addEdge(v, w);
            }
        }
        return G;
    }

    public static Digraph complete(int V) {
        Digraph G = new Digraph(V);
        for (int v = 0; v < V; ++v) {
            for (int w = 0; w < V; ++w) {
                if (v == w) continue;
                G.addEdge(v, w);
            }
        }
        return G;
    }

    public static Digraph dag(int V, int E) {
        if ((long)E > (long)V * (long)(V - 1) / 2L) {
            throw new IllegalArgumentException("Too many edges");
        }
        if (E < 0) {
            throw new IllegalArgumentException("Too few edges");
        }
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        int[] vertices = new int[V];
        for (int i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        while (G.E() < E) {
            int v = StdRandom.uniformInt(V);
            int w = StdRandom.uniformInt(V);
            Edge e = new Edge(v, w);
            if (v >= w || set.contains(e)) continue;
            set.add(e);
            G.addEdge(vertices[v], vertices[w]);
        }
        return G;
    }

    public static Digraph tournament(int V) {
        Digraph G = new Digraph(V);
        for (int v = 0; v < G.V(); ++v) {
            for (int w = v + 1; w < G.V(); ++w) {
                if (StdRandom.bernoulli(0.5)) {
                    G.addEdge(v, w);
                    continue;
                }
                G.addEdge(w, v);
            }
        }
        return G;
    }

    public static Digraph completeRootedInDAG(int V) {
        int i;
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 0; i < V; ++i) {
            for (int j = i + 1; j < V; ++j) {
                G.addEdge(vertices[i], vertices[j]);
            }
        }
        return G;
    }

    public static Digraph rootedInDAG(int V, int E) {
        Edge e;
        int w;
        int v;
        if ((long)E > (long)V * (long)(V - 1) / 2L) {
            throw new IllegalArgumentException("Too many edges");
        }
        if (E < V - 1) {
            throw new IllegalArgumentException("Too few edges");
        }
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        int[] vertices = new int[V];
        for (int i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (v = 0; v < V - 1; ++v) {
            w = StdRandom.uniformInt(v + 1, V);
            e = new Edge(v, w);
            set.add(e);
            G.addEdge(vertices[v], vertices[w]);
        }
        while (G.E() < E) {
            v = StdRandom.uniformInt(V);
            w = StdRandom.uniformInt(V);
            e = new Edge(v, w);
            if (v >= w || set.contains(e)) continue;
            set.add(e);
            G.addEdge(vertices[v], vertices[w]);
        }
        return G;
    }

    public static Digraph completeRootedOutDAG(int V) {
        int i;
        Digraph G = new Digraph(V);
        int[] vertices = new int[V];
        for (i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (i = 0; i < V; ++i) {
            for (int j = i + 1; j < V; ++j) {
                G.addEdge(vertices[j], vertices[i]);
            }
        }
        return G;
    }

    public static Digraph rootedOutDAG(int V, int E) {
        Edge e;
        int w;
        int v;
        if ((long)E > (long)V * (long)(V - 1) / 2L) {
            throw new IllegalArgumentException("Too many edges");
        }
        if (E < V - 1) {
            throw new IllegalArgumentException("Too few edges");
        }
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        int[] vertices = new int[V];
        for (int i = 0; i < V; ++i) {
            vertices[i] = i;
        }
        StdRandom.shuffle(vertices);
        for (v = 0; v < V - 1; ++v) {
            w = StdRandom.uniformInt(v + 1, V);
            e = new Edge(w, v);
            set.add(e);
            G.addEdge(vertices[w], vertices[v]);
        }
        while (G.E() < E) {
            v = StdRandom.uniformInt(V);
            w = StdRandom.uniformInt(V);
            e = new Edge(w, v);
            if (v >= w || set.contains(e)) continue;
            set.add(e);
            G.addEdge(vertices[w], vertices[v]);
        }
        return G;
    }

    public static Digraph rootedInTree(int V) {
        return DigraphGenerator.rootedInDAG(V, V - 1);
    }

    public static Digraph rootedOutTree(int V) {
        return DigraphGenerator.rootedOutDAG(V, V - 1);
    }

    public static Digraph path(int V) {
        int i;
        Digraph G = new Digraph(V);
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

    public static Digraph binaryTree(int V) {
        int i;
        Digraph G = new Digraph(V);
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

    public static Digraph cycle(int V) {
        int i;
        Digraph G = new Digraph(V);
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

    public static Digraph eulerianCycle(int V, int E) {
        int i;
        if (E <= 0) {
            throw new IllegalArgumentException("An Eulerian cycle must have at least one edge");
        }
        if (V <= 0) {
            throw new IllegalArgumentException("An Eulerian cycle must have at least one vertex");
        }
        Digraph G = new Digraph(V);
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

    public static Digraph eulerianPath(int V, int E) {
        int i;
        if (E < 0) {
            throw new IllegalArgumentException("negative number of edges");
        }
        if (V <= 0) {
            throw new IllegalArgumentException("An Eulerian path must have at least one vertex");
        }
        Digraph G = new Digraph(V);
        int[] vertices = new int[E + 1];
        for (i = 0; i < E + 1; ++i) {
            vertices[i] = StdRandom.uniformInt(V);
        }
        for (i = 0; i < E; ++i) {
            G.addEdge(vertices[i], vertices[i + 1]);
        }
        return G;
    }

    public static Digraph strong(int V, int E, int c) {
        int v;
        if (c >= V || c <= 0) {
            throw new IllegalArgumentException("Number of components must be between 1 and V");
        }
        if (E <= 2 * (V - c)) {
            throw new IllegalArgumentException("Number of edges must be at least 2(V-c)");
        }
        if ((long)E > (long)V * (long)(V - 1) / 2L) {
            throw new IllegalArgumentException("Too many edges");
        }
        Digraph G = new Digraph(V);
        SET<Edge> set = new SET<Edge>();
        int[] label = new int[V];
        for (v = 0; v < V; ++v) {
            label[v] = StdRandom.uniformInt(c);
        }
        for (int i = 0; i < c; ++i) {
            Edge e;
            int w;
            int v2;
            int count = 0;
            for (int v3 = 0; v3 < G.V(); ++v3) {
                if (label[v3] != i) continue;
                ++count;
            }
            int[] vertices = new int[count];
            int j = 0;
            for (v2 = 0; v2 < V; ++v2) {
                if (label[v2] != i) continue;
                vertices[j++] = v2;
            }
            StdRandom.shuffle(vertices);
            for (v2 = 0; v2 < count - 1; ++v2) {
                w = StdRandom.uniformInt(v2 + 1, count);
                e = new Edge(w, v2);
                set.add(e);
                G.addEdge(vertices[w], vertices[v2]);
            }
            for (v2 = 0; v2 < count - 1; ++v2) {
                w = StdRandom.uniformInt(v2 + 1, count);
                e = new Edge(v2, w);
                set.add(e);
                G.addEdge(vertices[v2], vertices[w]);
            }
        }
        while (G.E() < E) {
            int w;
            v = StdRandom.uniformInt(V);
            Edge e = new Edge(v, w = StdRandom.uniformInt(V));
            if (set.contains(e) || v == w || label[v] > label[w]) continue;
            set.add(e);
            G.addEdge(v, w);
        }
        return G;
    }

    public static void main(String[] args) {
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        StdOut.println("complete graph");
        StdOut.println(DigraphGenerator.complete(V));
        StdOut.println();
        StdOut.println("simple");
        StdOut.println(DigraphGenerator.simple(V, E));
        StdOut.println();
        StdOut.println("path");
        StdOut.println(DigraphGenerator.path(V));
        StdOut.println();
        StdOut.println("cycle");
        StdOut.println(DigraphGenerator.cycle(V));
        StdOut.println();
        StdOut.println("Eulierian path");
        StdOut.println(DigraphGenerator.eulerianPath(V, E));
        StdOut.println();
        StdOut.println("Eulierian cycle");
        StdOut.println(DigraphGenerator.eulerianCycle(V, E));
        StdOut.println();
        StdOut.println("binary tree");
        StdOut.println(DigraphGenerator.binaryTree(V));
        StdOut.println();
        StdOut.println("tournament");
        StdOut.println(DigraphGenerator.tournament(V));
        StdOut.println();
        StdOut.println("DAG");
        StdOut.println(DigraphGenerator.dag(V, E));
        StdOut.println();
        StdOut.println("rooted-in DAG");
        StdOut.println(DigraphGenerator.rootedInDAG(V, E));
        StdOut.println();
        StdOut.println("rooted-out DAG");
        StdOut.println(DigraphGenerator.rootedOutDAG(V, E));
        StdOut.println();
        StdOut.println("rooted-in tree");
        StdOut.println(DigraphGenerator.rootedInTree(V));
        StdOut.println();
        StdOut.println("rooted-out DAG");
        StdOut.println(DigraphGenerator.rootedOutTree(V));
        StdOut.println();
    }

    private static final class Edge
    implements Comparable<Edge> {
        private final int v;
        private final int w;

        private Edge(int v, int w) {
            this.v = v;
            this.w = w;
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

