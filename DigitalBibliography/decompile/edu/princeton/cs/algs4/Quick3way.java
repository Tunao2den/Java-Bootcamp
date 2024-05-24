/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Quick3way {
    private Quick3way() {
    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);
        Quick3way.sort(a, 0, a.length - 1);
        assert (Quick3way.isSorted(a));
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int lt = lo;
        int gt = hi;
        Comparable v = a[lo];
        int i = lo + 1;
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0) {
                Quick3way.exch(a, lt++, i++);
                continue;
            }
            if (cmp > 0) {
                Quick3way.exch(a, i, gt--);
                continue;
            }
            ++i;
        }
        Quick3way.sort(a, lo, lt - 1);
        Quick3way.sort(a, gt + 1, hi);
        assert (Quick3way.isSorted(a, lo, hi));
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
        return Quick3way.isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!Quick3way.less(a[i], a[i - 1])) continue;
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
        Quick3way.sort((Comparable[])a);
        Quick3way.show((Comparable[])a);
    }
}

