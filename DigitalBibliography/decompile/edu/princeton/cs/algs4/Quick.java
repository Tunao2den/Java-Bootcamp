/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Quick {
    private Quick() {
    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        Quick.sort(a, 0, a.length - 1);
        assert (Quick.isSorted(a));
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int j = Quick.partition(a, lo, hi);
        Quick.sort(a, lo, j - 1);
        Quick.sort(a, j + 1, hi);
        assert (Quick.isSorted(a, lo, hi));
    }

    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        while (true) {
            if (Quick.less(a[++i], v) && i != hi) {
                continue;
            }
            while (Quick.less(v, a[--j]) && j != lo) {
            }
            if (i >= j) break;
            Quick.exch(a, i, j);
        }
        Quick.exch(a, lo, j);
        return j;
    }

    public static Comparable select(Comparable[] a, int k) {
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException("index is not between 0 and " + a.length + ": " + k);
        }
        StdRandom.shuffle(a);
        int lo = 0;
        int hi = a.length - 1;
        while (hi > lo) {
            int i = Quick.partition(a, lo, hi);
            if (i > k) {
                hi = i - 1;
                continue;
            }
            if (i < k) {
                lo = i + 1;
                continue;
            }
            return a[i];
        }
        return a[lo];
    }

    private static boolean less(Comparable v, Comparable w) {
        if (v == w) {
            return false;
        }
        return v.compareTo(w) < 0;
    }

    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean isSorted(Comparable[] a) {
        return Quick.isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!Quick.less(a[i], a[i - 1])) continue;
            return false;
        }
        return true;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
    }

    public static void main(String[] args) {
        Object[] a = StdIn.readAllStrings();
        Quick.sort((Comparable[])a);
        Quick.show((Comparable[])a);
        assert (Quick.isSorted((Comparable[])a));
        StdRandom.shuffle(a);
        StdOut.println();
        for (int i = 0; i < a.length; ++i) {
            String ith = (String)((Object)Quick.select((Comparable[])a, i));
            StdOut.println(ith);
        }
    }
}

