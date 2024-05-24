/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class MergeX {
    private static final int CUTOFF = 7;

    private MergeX() {
    }

    private static void merge(Comparable[] src, Comparable[] dst, int lo, int mid, int hi) {
        assert (MergeX.isSorted(src, lo, mid));
        assert (MergeX.isSorted(src, mid + 1, hi));
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            dst[k] = i > mid ? src[j++] : (j > hi ? src[i++] : (MergeX.less(src[j], src[i]) ? src[j++] : src[i++]));
        }
        assert (MergeX.isSorted(dst, lo, hi));
    }

    private static void sort(Comparable[] src, Comparable[] dst, int lo, int hi) {
        if (hi <= lo + 7) {
            MergeX.insertionSort(dst, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        MergeX.sort(dst, src, lo, mid);
        MergeX.sort(dst, src, mid + 1, hi);
        if (!MergeX.less(src[mid + 1], src[mid])) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1);
            return;
        }
        MergeX.merge(src, dst, lo, mid, hi);
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = (Comparable[])a.clone();
        MergeX.sort(aux, a, 0, a.length - 1);
        assert (MergeX.isSorted(a));
    }

    private static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && MergeX.less(a[j], a[j - 1]); --j) {
                MergeX.exch(a, j, j - 1);
            }
        }
    }

    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    private static boolean less(Object a, Object b, Comparator comparator) {
        return comparator.compare(a, b) < 0;
    }

    public static void sort(Object[] a, Comparator comparator) {
        Object[] aux = (Object[])a.clone();
        MergeX.sort(aux, a, 0, a.length - 1, comparator);
        assert (MergeX.isSorted(a, comparator));
    }

    private static void merge(Object[] src, Object[] dst, int lo, int mid, int hi, Comparator comparator) {
        assert (MergeX.isSorted(src, lo, mid, comparator));
        assert (MergeX.isSorted(src, mid + 1, hi, comparator));
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            dst[k] = i > mid ? src[j++] : (j > hi ? src[i++] : (MergeX.less(src[j], src[i], comparator) ? src[j++] : src[i++]));
        }
        assert (MergeX.isSorted(dst, lo, hi, comparator));
    }

    private static void sort(Object[] src, Object[] dst, int lo, int hi, Comparator comparator) {
        if (hi <= lo + 7) {
            MergeX.insertionSort(dst, lo, hi, comparator);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        MergeX.sort(dst, src, lo, mid, comparator);
        MergeX.sort(dst, src, mid + 1, hi, comparator);
        if (!MergeX.less(src[mid + 1], src[mid], comparator)) {
            System.arraycopy(src, lo, dst, lo, hi - lo + 1);
            return;
        }
        MergeX.merge(src, dst, lo, mid, hi, comparator);
    }

    private static void insertionSort(Object[] a, int lo, int hi, Comparator comparator) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && MergeX.less(a[j], a[j - 1], comparator); --j) {
                MergeX.exch(a, j, j - 1);
            }
        }
    }

    private static boolean isSorted(Comparable[] a) {
        return MergeX.isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!MergeX.less(a[i], a[i - 1])) continue;
            return false;
        }
        return true;
    }

    private static boolean isSorted(Object[] a, Comparator comparator) {
        return MergeX.isSorted(a, 0, a.length - 1, comparator);
    }

    private static boolean isSorted(Object[] a, int lo, int hi, Comparator comparator) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!MergeX.less(a[i], a[i - 1], comparator)) continue;
            return false;
        }
        return true;
    }

    private static void show(Object[] a) {
        for (int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
    }

    public static void main(String[] args) {
        Object[] a = StdIn.readAllStrings();
        MergeX.sort((Comparable[])a);
        MergeX.show(a);
    }
}

