/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Accumulator {
    private int n = 0;
    private double sum = 0.0;
    private double mu = 0.0;

    public void addDataValue(double x) {
        ++this.n;
        double delta = x - this.mu;
        this.mu += delta / (double)this.n;
        this.sum += (double)(this.n - 1) / (double)this.n * delta * delta;
    }

    public double mean() {
        return this.mu;
    }

    public double var() {
        if (this.n <= 1) {
            return Double.NaN;
        }
        return this.sum / (double)(this.n - 1);
    }

    public double stddev() {
        return Math.sqrt(this.var());
    }

    public int count() {
        return this.n;
    }

    public String toString() {
        return "n = " + this.n + ", mean = " + this.mean() + ", stddev = " + this.stddev();
    }

    public static void main(String[] args) {
        Accumulator stats = new Accumulator();
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            stats.addDataValue(x);
        }
        StdOut.printf("n      = %d\n", stats.count());
        StdOut.printf("mean   = %.5f\n", stats.mean());
        StdOut.printf("stddev = %.5f\n", stats.stddev());
        StdOut.printf("var    = %.5f\n", stats.var());
        StdOut.println(stats);
    }
}

