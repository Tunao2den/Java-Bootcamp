/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SymbolDigraph {
    private ST<String, Integer> st = new ST();
    private String[] keys;
    private Digraph graph;

    public SymbolDigraph(String filename, String delimiter) {
        Object a;
        In in = new In(filename);
        while (in.hasNextLine()) {
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
        this.graph = new Digraph(this.st.size());
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
    public Digraph G() {
        return this.graph;
    }

    public Digraph digraph() {
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
        SymbolDigraph sg = new SymbolDigraph(filename, delimiter);
        Digraph graph = sg.digraph();
        while (!StdIn.isEmpty()) {
            String t = StdIn.readLine();
            for (int v : graph.adj(sg.index(t))) {
                StdOut.println("   " + sg.nameOf(v));
            }
        }
    }
}

