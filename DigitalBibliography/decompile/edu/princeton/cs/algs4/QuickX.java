/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Insertion;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class QuickX {
    private static final int INSERTION_SORT_CUTOFF = 8;

    private QuickX() {
    }

    public static void sort(Comparable[] a) {
        QuickX.sort(a, 0, a.length - 1);
        assert (QuickX.isSorted(a));
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int n = hi - lo + 1;
        if (n <= 8) {
            Insertion.sort(a, lo, hi + 1);
            return;
        }
        int j = QuickX.partition(a, lo, hi);
        QuickX.sort(a, lo, j - 1);
        QuickX.sort(a, j + 1, hi);
    }

    private static int partition(Comparable[] a, int lo, int hi) {
        int n = hi - lo + 1;
        int m = QuickX.median3(a, lo, lo + n / 2, hi);
        QuickX.exch(a, m, lo);
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        while (QuickX.less(a[++i], v)) {
            if (i != hi) continue;
            QuickX.exch(a, lo, hi);
            return hi;
        }
        while (QuickX.less(v, a[--j])) {
            if (j != lo + 1) continue;
            return lo;
        }
        while (i < j) {
            QuickX.exch(a, i, j);
            while (QuickX.less(a[++i], v)) {
            }
            while (QuickX.less(v, a[--j])) {
            }
        }
        QuickX.exch(a, lo, j);
        return j;
    }

    private static int median3(Comparable[] a, int i, int j, int k) {
        return QuickX.less(a[i], a[j]) ? (QuickX.less(a[j], a[k]) ? j : (QuickX.less(a[i], a[k]) ? k : i)) : (QuickX.less(a[k], a[j]) ? j : (QuickX.less(a[k], a[i]) ? k : i));
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; ++i) {
            if (!QuickX.less(a[i], a[i - 1])) continue;
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
        String[] a = StdIn.readAllStrings();
        QuickX.sort((Comparable[])a);
        assert (QuickX.isSorted((Comparable[])a));
        QuickX.show((Comparable[])a);
    }
}

