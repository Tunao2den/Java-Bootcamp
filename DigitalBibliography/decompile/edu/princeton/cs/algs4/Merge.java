/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Merge {
    private Merge() {
    }

    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        assert (Merge.isSorted(a, lo, mid));
        assert (Merge.isSorted(a, mid + 1, hi));
        for (int k = lo; k <= hi; ++k) {
            aux[k] = a[k];
        }
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            a[k] = i > mid ? aux[j++] : (j > hi ? aux[i++] : (Merge.less(aux[j], aux[i]) ? aux[j++] : aux[i++]));
        }
        assert (Merge.isSorted(a, lo, hi));
    }

    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        Merge.sort(a, aux, lo, mid);
        Merge.sort(a, aux, mid + 1, hi);
        Merge.merge(a, aux, lo, mid, hi);
    }

    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        Merge.sort(a, aux, 0, a.length - 1);
        assert (Merge.isSorted(a));
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static boolean isSorted(Comparable[] a) {
        return Merge.isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!Merge.less(a[i], a[i - 1])) continue;
            return false;
        }
        return true;
    }

    private static void merge(Comparable[] a, int[] index, int[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; ++k) {
            aux[k] = index[k];
        }
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            index[k] = i > mid ? aux[j++] : (j > hi ? aux[i++] : (Merge.less(a[aux[j]], a[aux[i]]) ? aux[j++] : aux[i++]));
        }
    }

    public static int[] indexSort(Comparable[] a) {
        int n = a.length;
        int[] index = new int[n];
        for (int i = 0; i < n; ++i) {
            index[i] = i;
        }
        int[] aux = new int[n];
        Merge.sort(a, index, aux, 0, n - 1);
        return index;
    }

    private static void sort(Comparable[] a, int[] index, int[] aux, int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        Merge.sort(a, index, aux, lo, mid);
        Merge.sort(a, index, aux, mid + 1, hi);
        Merge.merge(a, index, aux, lo, mid, hi);
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Merge.sort((Comparable[])a);
        Merge.show((Comparable[])a);
    }
}

