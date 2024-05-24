/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Counter
implements Comparable<Counter> {
    private final String name;
    private int count = 0;

    public Counter(String id) {
        this.name = id;
    }

    public void increment() {
        ++this.count;
    }

    public int tally() {
        return this.count;
    }

    public String toString() {
        return this.count + " " + this.name;
    }

    @Override
    public int compareTo(Counter that) {
        return Integer.compare(this.count, that.count);
    }

    public static void main(String[] args) {
        int i;
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        Counter[] hits = new Counter[n];
        for (i = 0; i < n; ++i) {
            hits[i] = new Counter("counter" + i);
        }
        for (int t = 0; t < trials; ++t) {
            hits[StdRandom.uniformInt(n)].increment();
        }
        for (i = 0; i < n; ++i) {
            StdOut.println(hits[i]);
        }
    }
}

