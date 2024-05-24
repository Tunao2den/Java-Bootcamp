/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StaticSETofInts;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Allowlist {
    private Allowlist() {
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] white = in.readAllInts();
        StaticSETofInts set = new StaticSETofInts(white);
        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (set.contains(key)) continue;
            StdOut.println(key);
        }
    }
}

