/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;

public class FlowEdge {
    private static final double FLOATING_POINT_EPSILON = 1.0E-10;
    private final int v;
    private final int w;
    private final double capacity;
    private double flow;

    public FlowEdge(int v, int w, double capacity) {
        if (v < 0) {
            throw new IllegalArgumentException("vertex index must be a non-negative integer");
        }
        if (w < 0) {
            throw new IllegalArgumentException("vertex index must be a non-negative integer");
        }
        if (!(capacity >= 0.0)) {
            throw new IllegalArgumentException("Edge capacity must be non-negative");
        }
        this.v = v;
        this.w = w;
        this.capacity = capacity;
        this.flow = 0.0;
    }

    public FlowEdge(int v, int w, double capacity, double flow) {
        if (v < 0) {
            throw new IllegalArgumentException("vertex index must be a non-negative integer");
        }
        if (w < 0) {
            throw new IllegalArgumentException("vertex index must be a non-negative integer");
        }
        if (!(capacity >= 0.0)) {
            throw new IllegalArgumentException("edge capacity must be non-negative");
        }
        if (!(flow <= capacity)) {
            throw new IllegalArgumentException("flow exceeds capacity");
        }
        if (!(flow >= 0.0)) {
            throw new IllegalArgumentException("flow must be non-negative");
        }
        this.v = v;
        this.w = w;
        this.capacity = capacity;
        this.flow = flow;
    }

    public FlowEdge(FlowEdge e) {
        this.v = e.v;
        this.w = e.w;
        this.capacity = e.capacity;
        this.flow = e.flow;
    }

    public int from() {
        return this.v;
    }

    public int to() {
        return this.w;
    }

    public double capacity() {
        return this.capacity;
    }

    public double flow() {
        return this.flow;
    }

    public int other(int vertex) {
        if (vertex == this.v) {
            return this.w;
        }
        if (vertex == this.w) {
            return this.v;
        }
        throw new IllegalArgumentException("invalid endpoint");
    }

    public double residualCapacityTo(int vertex) {
        if (vertex == this.v) {
            return this.flow;
        }
        if (vertex == this.w) {
            return this.capacity - this.flow;
        }
        throw new IllegalArgumentException("invalid endpoint");
    }

    public void addResidualFlowTo(int vertex, double delta) {
        if (!(delta >= 0.0)) {
            throw new IllegalArgumentException("Delta must be non-negative");
        }
        if (vertex == this.v) {
            this.flow -= delta;
        } else if (vertex == this.w) {
            this.flow += delta;
        } else {
            throw new IllegalArgumentException("invalid endpoint");
        }
        if (Math.abs(this.flow) <= 1.0E-10) {
            this.flow = 0.0;
        }
        if (Math.abs(this.flow - this.capacity) <= 1.0E-10) {
            this.flow = this.capacity;
        }
        if (!(this.flow >= 0.0)) {
            throw new IllegalArgumentException("Flow is negative");
        }
        if (!(this.flow <= this.capacity)) {
            throw new IllegalArgumentException("Flow exceeds capacity");
        }
    }

    public String toString() {
        return this.v + "->" + this.w + " " + this.flow + "/" + this.capacity;
    }

    public static void main(String[] args) {
        FlowEdge e = new FlowEdge(12, 23, 4.56);
        StdOut.println(e);
    }
}

