/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class FordFulkerson {
    private static final double FLOATING_POINT_EPSILON = 1.0E-11;
    private final int V;
    private boolean[] marked;
    private FlowEdge[] edgeTo;
    private double value;

    public FordFulkerson(FlowNetwork G, int s, int t) {
        this.V = G.V();
        this.validate(s);
        this.validate(t);
        if (s == t) {
            throw new IllegalArgumentException("Source equals sink");
        }
        if (!this.isFeasible(G, s, t)) {
            throw new IllegalArgumentException("Initial flow is infeasible");
        }
        this.value = this.excess(G, t);
        while (this.hasAugmentingPath(G, s, t)) {
            double bottle = Double.POSITIVE_INFINITY;
            int v = t;
            while (v != s) {
                bottle = Math.min(bottle, this.edgeTo[v].residualCapacityTo(v));
                v = this.edgeTo[v].other(v);
            }
            v = t;
            while (v != s) {
                this.edgeTo[v].addResidualFlowTo(v, bottle);
                v = this.edgeTo[v].other(v);
            }
            this.value += bottle;
        }
        assert (this.check(G, s, t));
    }

    public double value() {
        return this.value;
    }

    public boolean inCut(int v) {
        this.validate(v);
        return this.marked[v];
    }

    private void validate(int v) {
        if (v < 0 || v >= this.V) {
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.V - 1));
        }
    }

    private boolean hasAugmentingPath(FlowNetwork G, int s, int t) {
        this.edgeTo = new FlowEdge[G.V()];
        this.marked = new boolean[G.V()];
        Queue<Integer> queue = new Queue<Integer>();
        queue.enqueue(s);
        this.marked[s] = true;
        while (!queue.isEmpty() && !this.marked[t]) {
            int v = (Integer)queue.dequeue();
            for (FlowEdge e : G.adj(v)) {
                int w;
                if (!(e.residualCapacityTo(w = e.other(v)) > 0.0) || this.marked[w]) continue;
                this.edgeTo[w] = e;
                this.marked[w] = true;
                queue.enqueue(w);
            }
        }
        return this.marked[t];
    }

    private double excess(FlowNetwork G, int v) {
        double excess = 0.0;
        for (FlowEdge e : G.adj(v)) {
            if (v == e.from()) {
                excess -= e.flow();
                continue;
            }
            excess += e.flow();
        }
        return excess;
    }

    private boolean isFeasible(FlowNetwork G, int s, int t) {
        int v;
        for (v = 0; v < G.V(); ++v) {
            for (FlowEdge e : G.adj(v)) {
                if (!(e.flow() < -1.0E-11) && !(e.flow() > e.capacity() + 1.0E-11)) continue;
                System.err.println("Edge does not satisfy capacity constraints: " + e);
                return false;
            }
        }
        if (Math.abs(this.value + this.excess(G, s)) > 1.0E-11) {
            System.err.println("Excess at source = " + this.excess(G, s));
            System.err.println("Max flow         = " + this.value);
            return false;
        }
        if (Math.abs(this.value - this.excess(G, t)) > 1.0E-11) {
            System.err.println("Excess at sink   = " + this.excess(G, t));
            System.err.println("Max flow         = " + this.value);
            return false;
        }
        for (v = 0; v < G.V(); ++v) {
            if (v == s || v == t || !(Math.abs(this.excess(G, v)) > 1.0E-11)) continue;
            System.err.println("Net flow out of " + v + " doesn't equal zero");
            return false;
        }
        return true;
    }

    private boolean check(FlowNetwork G, int s, int t) {
        if (!this.isFeasible(G, s, t)) {
            System.err.println("Flow is infeasible");
            return false;
        }
        if (!this.inCut(s)) {
            System.err.println("source " + s + " is not on source side of min cut");
            return false;
        }
        if (this.inCut(t)) {
            System.err.println("sink " + t + " is on source side of min cut");
            return false;
        }
        double mincutValue = 0.0;
        for (int v = 0; v < G.V(); ++v) {
            for (FlowEdge e : G.adj(v)) {
                if (v != e.from() || !this.inCut(e.from()) || this.inCut(e.to())) continue;
                mincutValue += e.capacity();
            }
        }
        if (Math.abs(mincutValue - this.value) > 1.0E-11) {
            System.err.println("Max flow value = " + this.value + ", min cut value = " + mincutValue);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int v;
        int V = Integer.parseInt(args[0]);
        int E = Integer.parseInt(args[1]);
        int s = 0;
        int t = V - 1;
        FlowNetwork G = new FlowNetwork(V, E);
        StdOut.println(G);
        FordFulkerson maxflow = new FordFulkerson(G, s, t);
        StdOut.println("Max flow from " + s + " to " + t);
        for (v = 0; v < G.V(); ++v) {
            for (FlowEdge e : G.adj(v)) {
                if (v != e.from() || !(e.flow() > 0.0)) continue;
                StdOut.println("   " + e);
            }
        }
        StdOut.print("Min cut: ");
        for (v = 0; v < G.V(); ++v) {
            if (!maxflow.inCut(v)) continue;
            StdOut.print(v + " ");
        }
        StdOut.println();
        StdOut.println("Max flow value = " + maxflow.value());
    }
}

