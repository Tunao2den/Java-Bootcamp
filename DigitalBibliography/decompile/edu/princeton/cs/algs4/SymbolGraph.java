/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SymbolGraph {
    private ST<String, Integer> st = new ST();
    private String[] keys;
    private Graph graph;

    public SymbolGraph(String filename, String delimiter) {
        Object a;
        In in = new In(filename);
        while (!in.isEmpty()) {
            a = in.readLine().split(delimiter);
            for (int i = 0; i < ((String[])a).length; ++i) {
                if (this.st.contains((String)a[i])) continue;
                this.st.put((String)a[i], this.st.size());
            }
        }
        this.keys = new String[this.st.size()];
        a = this.st.keys().iterator();
        while (a.hasNext()) {
            String name;
            this.keys[this.st.get((String)name).intValue()] = name = (String)a.next();
        }
        this.graph = new Graph(this.st.size());
        in = new In(filename);
        while (in.hasNextLine()) {
            a = in.readLine().split(delimiter);
            int v = this.st.get((String)a[0]);
            for (int i = 1; i < ((Object)a).length; ++i) {
                int w = this.st.get((String)a[i]);
                this.graph.addEdge(v, w);
            }
        }
    }

    public boolean contains(String s) {
        return this.st.contains(s);
    }

    @Deprecated
    public int index(String s) {
        return this.st.get(s);
    }

    public int indexOf(String s) {
        return this.st.get(s);
    }

    @Deprecated
    public String name(int v) {
        this.validateVertex(v);
        return this.keys[v];
    }

    public String nameOf(int v) {
        this.validateVertex(v);
        return this.keys[v];
    }

    @Deprecated
    public Graph G() {
        return this.graph;
    }

    public Graph graph() {
        return this.graph;
    }

    private void validateVertex(int v) {
        int V = this.graph.V();
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        String delimiter = args[1];
        SymbolGraph sg = new SymbolGraph(filename, delimiter);
        Graph graph = sg.graph();
        while (StdIn.hasNextLine()) {
            String source = StdIn.readLine();
            if (sg.contains(source)) {
                int s = sg.index(source);
                for (int v : graph.adj(s)) {
                    StdOut.println("   " + sg.nameOf(v));
                }
                continue;
            }
            StdOut.println("input not contain '" + source + "'");
        }
    }
}

