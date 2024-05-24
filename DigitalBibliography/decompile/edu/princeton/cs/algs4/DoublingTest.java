/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.ThreeSum;

public class DoublingTest {
    private static final int MAXIMUM_INTEGER = 1000000;

    private DoublingTest() {
    }

    public static double timeTrial(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) {
            a[i] = StdRandom.uniformInt(-1000000, 1000000);
        }
        Stopwatch timer = new Stopwatch();
        int ignore = ThreeSum.count(a);
        return timer.elapsedTime();
    }

    public static void main(String[] args) {
        int n = 250;
        while (true) {
            double time = DoublingTest.timeTrial(n);
            StdOut.printf("%7d %7.1f\n", n, time);
            n += n;
        }
    }
}

