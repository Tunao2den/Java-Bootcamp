/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class InsertionX {
    private InsertionX() {
    }

    public static void sort(Comparable[] a) {
        int i;
        int n = a.length;
        int exchanges = 0;
        for (i = n - 1; i > 0; --i) {
            if (!InsertionX.less(a[i], a[i - 1])) continue;
            InsertionX.exch(a, i, i - 1);
            ++exchanges;
        }
        if (exchanges == 0) {
            return;
        }
        for (i = 2; i < n; ++i) {
            Comparable v = a[i];
            int j = i;
            while (InsertionX.less(v, a[j - 1])) {
                a[j] = a[j - 1];
                --j;
            }
            a[j] = v;
        }
        assert (InsertionX.isSorted(a));
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
            if (!InsertionX.less(a[i], a[i - 1])) continue;
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
        InsertionX.sort((Comparable[])a);
        InsertionX.show((Comparable[])a);
    }
}

