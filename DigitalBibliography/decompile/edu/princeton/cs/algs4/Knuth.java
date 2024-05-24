/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Knuth {
    private Knuth() {
    }

    public static void shuffle(Object[] a) {
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int r = (int)(Math.random() * (double)(i + 1));
            Object swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
    }

    public static void shuffleAlternate(Object[] a) {
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int r = i + (int)(Math.random() * (double)(n - i));
            Object swap = a[r];
            a[r] = a[i];
            a[i] = swap;
        }
    }

    public static void main(String[] args) {
        Object[] a = StdIn.readAllStrings();
        Knuth.shuffle(a);
        for (int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
    }
}

