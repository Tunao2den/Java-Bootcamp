/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class Selection {
    private Selection() {
    }

    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int min = i;
            for (int j = i + 1; j < n; ++j) {
                if (!Selection.less(a[j], a[min])) continue;
                min = j;
            }
            Selection.exch(a, i, min);
            assert (Selection.isSorted(a, 0, i));
        }
        assert (Selection.isSorted(a));
    }

    public static void sort(Object[] a, Comparator comparator) {
        int n = a.length;
        for (int i = 0; i < n; ++i) {
            int min = i;
            for (int j = i + 1; j < n; ++j) {
                if (!Selection.less(comparator, a[j], a[min])) continue;
                min = j;
            }
            Selection.exch(a, i, min);
            assert (Selection.isSorted(a, comparator, 0, i));
        }
        assert (Selection.isSorted(a, comparator));
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static boolean less(Comparator comparator, Object v, Object w) {
        return comparator.compare(v, w) < 0;
    }

    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean isSorted(Comparable[] a) {
        return Selection.isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!Selection.less(a[i], a[i - 1])) continue;
            return false;
        }
        return true;
    }

    private static boolean isSorted(Object[] a, Comparator comparator) {
        return Selection.isSorted(a, comparator, 0, a.length - 1);
    }

    private static boolean isSorted(Object[] a, Comparator comparator, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!Selection.less(comparator, a[i], a[i - 1])) continue;
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
        Selection.sort((Comparable[])a);
        Selection.show((Comparable[])a);
    }
}

