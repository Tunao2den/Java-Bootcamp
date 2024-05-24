/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BlockFilter {
    private BlockFilter() {
    }

    public static void main(String[] args) {
        String word;
        SET<String> set = new SET<String>();
        In in = new In(args[0]);
        while (!in.isEmpty()) {
            word = in.readString();
            set.add(word);
        }
        while (!StdIn.isEmpty()) {
            word = StdIn.readString();
            if (set.contains(word)) continue;
            StdOut.println(word);
        }
    }
}

