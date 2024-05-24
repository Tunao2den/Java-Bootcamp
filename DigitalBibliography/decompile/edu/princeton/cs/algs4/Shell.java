/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Shell {
    private Shell() {
    }

    public static void sort(Comparable[] a) {
        int n = a.length;
        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }
        while (h >= 1) {
            for (int i = h; i < n; ++i) {
                for (int j = i; j >= h && Shell.less(a[j], a[j - h]); j -= h) {
                    Shell.exch(a, j, j - h);
                }
            }
            assert (Shell.isHsorted(a, h));
            h /= 3;
        }
        assert (Shell.isSorted(a));
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
            if (!Shell.less(a[i], a[i - 1])) continue;
            return false;
        }
        return true;
    }

    private static boolean isHsorted(Comparable[] a, int h) {
        for (int i = h; i < a.length; ++i) {
            if (!Shell.less(a[i], a[i - h])) continue;
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
        Shell.sort((Comparable[])a);
        Shell.show((Comparable[])a);
    }
}

