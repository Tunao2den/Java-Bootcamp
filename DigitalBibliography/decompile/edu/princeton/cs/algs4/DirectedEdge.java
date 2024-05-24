/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;

public class DirectedEdge {
    private final int v;
    private final int w;
    private final double weight;

    public DirectedEdge(int v, int w, double weight) {
        if (v < 0) {
            throw new IllegalArgumentException("Vertex names must be non-negative integers");
        }
        if (w < 0) {
            throw new IllegalArgumentException("Vertex names must be non-negative integers");
        }
        if (Double.isNaN(weight)) {
            throw new IllegalArgumentException("Weight is NaN");
        }
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from() {
        return this.v;
    }

    public int to() {
        return this.w;
    }

    public double weight() {
        return this.weight;
    }

    public String toString() {
        return this.v + "->" + this.w + " " + String.format("%5.2f", this.weight);
    }

    public static void main(String[] args) {
        DirectedEdge e = new DirectedEdge(12, 34, 5.67);
        StdOut.println(e);
    }
}

