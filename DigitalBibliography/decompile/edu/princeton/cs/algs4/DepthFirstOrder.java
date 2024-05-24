/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class DepthFirstOrder {
    private boolean[] marked;
    private int[] pre;
    private int[] post;
    private Queue<Integer> preorder;
    private Queue<Integer> postorder;
    private int preCounter;
    private int postCounter;

    public DepthFirstOrder(Digraph G) {
        this.pre = new int[G.V()];
        this.post = new int[G.V()];
        this.postorder = new Queue();
        this.preorder = new Queue();
        this.marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
        }
        assert (this.check());
    }

    public DepthFirstOrder(EdgeWeightedDigraph G) {
        this.pre = new int[G.V()];
        this.post = new int[G.V()];
        this.postorder = new Queue();
        this.preorder = new Queue();
        this.marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); ++v) {
            if (this.marked[v]) continue;
            this.dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {
        this.marked[v] = true;
        ++this.preCounter;
        this.preorder.enqueue(v);
        for (int w : G.adj(v)) {
            if (this.marked[w]) continue;
            this.dfs(G, w);
        }
        this.postorder.enqueue(v);
        ++this.postCounter;
    }

    private void dfs(EdgeWeightedDigraph G, int v) {
        this.marked[v] = true;
        ++this.preCounter;
        this.preorder.enqueue(v);
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (this.marked[w]) continue;
            this.dfs(G, w);
        }
        this.postorder.enqueue(v);
        ++this.postCounter;
    }

    public int pre(int v) {
        this.validateVertex(v);
        return this.pre[v];
    }

    public int post(int v) {
        this.validateVertex(v);
        return this.post[v];
    }

    public Iterable<Integer> post() {
        return this.postorder;
    }

    public Iterable<Integer> pre() {
        return this.preorder;
    }

    public Iterable<Integer> reversePost() {
        Stack<Integer> reverse = new Stack<Integer>();
        for (int v : this.postorder) {
            reverse.push(v);
        }
        return reverse;
    }

    private boolean check() {
        int r = 0;
        for (int v : this.post()) {
            if (this.post(v) != r) {
                StdOut.println("post(v) and post() inconsistent");
                return false;
            }
            ++r;
        }
        r = 0;
        for (int v : this.pre()) {
            if (this.pre(v) != r) {
                StdOut.println("pre(v) and pre() inconsistent");
                return false;
            }
            ++r;
        }
        return true;
    }

    private void validateVertex(int v) {
        int V = this.marked.length;
        if (v < 0 || v >= V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        DepthFirstOrder dfs = new DepthFirstOrder(G);
        StdOut.println("   v  pre post");
        StdOut.println("--------------");
        for (int v = 0; v < G.V(); ++v) {
            StdOut.printf("%4d %4d %4d\n", v, dfs.pre(v), dfs.post(v));
        }
        StdOut.print("Preorder:  ");
        for (int v : dfs.pre()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.print("Postorder: ");
        for (int v : dfs.post()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.print("Reverse postorder: ");
        for (int v : dfs.reversePost()) {
            StdOut.print(v + " ");
        }
        StdOut.println();
    }
}

