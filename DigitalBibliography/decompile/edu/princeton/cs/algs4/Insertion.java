/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class Insertion {
    private Insertion() {
    }

    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int i = 1; i < n; ++i) {
            for (int j = i; j > 0 && Insertion.less(a[j], a[j - 1]); --j) {
                Insertion.exch(a, j, j - 1);
            }
            assert (Insertion.isSorted(a, 0, i));
        }
        assert (Insertion.isSorted(a));
    }

    public static void sort(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; ++i) {
            for (int j = i; j > lo && Insertion.less(a[j], a[j - 1]); --j) {
                Insertion.exch(a, j, j - 1);
            }
        }
        assert (Insertion.isSorted(a, lo, hi));
    }

    public static void sort(Object[] a, Comparator comparator) {
        int n = a.length;
        for (int i = 1; i < n; ++i) {
            for (int j = i; j > 0 && Insertion.less(a[j], a[j - 1], comparator); --j) {
                Insertion.exch(a, j, j - 1);
            }
            assert (Insertion.isSorted(a, 0, i, comparator));
        }
        assert (Insertion.isSorted(a, comparator));
    }

    public static void sort(Object[] a, int lo, int hi, Comparator comparator) {
        for (int i = lo + 1; i < hi; ++i) {
            for (int j = i; j > lo && Insertion.less(a[j], a[j - 1], comparator); --j) {
                Insertion.exch(a, j, j - 1);
            }
        }
        assert (Insertion.isSorted(a, lo, hi, comparator));
    }

    public static int[] indexSort(Comparable[] a) {
        int i;
        int n = a.length;
        int[] index = new int[n];
        for (i = 0; i < n; ++i) {
            index[i] = i;
        }
        for (i = 1; i < n; ++i) {
            for (int j = i; j > 0 && Insertion.less(a[index[j]], a[index[j - 1]]); --j) {
                Insertion.exch(index, j, j - 1);
            }
        }
        return index;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static boolean less(Object v, Object w, Comparator comparator) {
        return comparator.compare(v, w) < 0;
    }

    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static void exch(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean isSorted(Comparable[] a) {
        return Insertion.isSorted(a, 0, a.length);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; ++i) {
            if (!Insertion.less(a[i], a[i - 1])) continue;
            return false;
        }
        return true;
    }

    private static boolean isSorted(Object[] a, Comparator comparator) {
        return Insertion.isSorted(a, 0, a.length, comparator);
    }

    private static boolean isSorted(Object[] a, int lo, int hi, Comparator comparator) {
        for (int i = lo + 1; i < hi; ++i) {
            if (!Insertion.less(a[i], a[i - 1], comparator)) continue;
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
        Insertion.sort((Comparable[])a);
        Insertion.show((Comparable[])a);
    }
}

