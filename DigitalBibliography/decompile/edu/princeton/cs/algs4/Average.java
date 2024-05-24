/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Average {
    private Average() {
    }

    public static void main(String[] args) {
        int count = 0;
        double sum = 0.0;
        while (!StdIn.isEmpty()) {
            double value = StdIn.readDouble();
            sum += value;
            ++count;
        }
        double average = sum / (double)count;
        StdOut.println("Average is " + average);
    }
}

