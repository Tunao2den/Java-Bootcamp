/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SuffixArray;

public class KWIK {
    private KWIK() {
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int context = Integer.parseInt(args[1]);
        String text = in.readAll().replaceAll("\\s+", " ");
        int n = text.length();
        SuffixArray sa = new SuffixArray(text);
        while (StdIn.hasNextLine()) {
            int to1;
            int from1;
            String query = StdIn.readLine();
            for (int i = sa.rank(query); i < n && query.equals(text.substring(from1 = sa.index(i), to1 = Math.min(n, from1 + query.length()))); ++i) {
                int from2 = Math.max(0, sa.index(i) - context);
                int to2 = Math.min(n, sa.index(i) + context + query.length());
                StdOut.println(text.substring(from2, to2));
            }
            StdOut.println();
        }
    }
}

