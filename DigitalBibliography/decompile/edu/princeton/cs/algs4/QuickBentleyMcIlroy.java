/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class QuickBentleyMcIlroy {
    private static final int INSERTION_SORT_CUTOFF = 8;
    private static final int MEDIAN_OF_3_CUTOFF = 40;

    private QuickBentleyMcIlroy() {
    }

    public static void sort(Comparable[] a) {
        QuickBentleyMcIlroy.sort(a, 0, a.length - 1);
    }

    private static void sort(Comparable[] a, int lo, int hi) {
        int k;
        int n = hi - lo + 1;
        if (n <= 8) {
            QuickBentleyMcIlroy.insertionSort(a, lo, hi);
            return;
        }
        if (n <= 40) {
            int m = QuickBentleyMcIlroy.median3(a, lo, lo + n / 2, hi);
            QuickBentleyMcIlroy.exch(a, m, lo);
        } else {
            int eps = n / 8;
            int mid = lo + n / 2;
            int m1 = QuickBentleyMcIlroy.median3(a, lo, lo + eps, lo + eps + eps);
            int m2 = QuickBentleyMcIlroy.median3(a, mid - eps, mid, mid + eps);
            int m3 = QuickBentleyMcIlroy.median3(a, hi - eps - eps, hi - eps, hi);
            int ninther = QuickBentleyMcIlroy.median3(a, m1, m2, m3);
            QuickBentleyMcIlroy.exch(a, ninther, lo);
        }
        int i = lo;
        int j = hi + 1;
        int p = lo;
        int q = hi + 1;
        Comparable v = a[lo];
        while (true) {
            if (QuickBentleyMcIlroy.less(a[++i], v) && i != hi) {
                continue;
            }
            while (QuickBentleyMcIlroy.less(v, a[--j]) && j != lo) {
            }
            if (i == j && QuickBentleyMcIlroy.eq(a[i], v)) {
                QuickBentleyMcIlroy.exch(a, ++p, i);
            }
            if (i >= j) break;
            QuickBentleyMcIlroy.exch(a, i, j);
            if (QuickBentleyMcIlroy.eq(a[i], v)) {
                QuickBentleyMcIlroy.exch(a, ++p, i);
            }
            if (!QuickBentleyMcIlroy.eq(a[j], v)) continue;
            QuickBentleyMcIlroy.exch(a, --q, j);
        }
        i = j + 1;
        for (k = lo; k <= p; ++k) {
            QuickBentleyMcIlroy.exch(a, k, j--);
        }
        for (k = hi; k >= q; --k) {
            QuickBentleyMcIlroy.exch(a, k, i++);
        }
        QuickBentleyMcIlroy.sort(a, lo, j);
        QuickBentleyMcIlroy.sort(a, i, hi);
    }

    private static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && QuickBentleyMcIlroy.less(a[j], a[j - 1]); --j) {
                QuickBentleyMcIlroy.exch(a, j, j - 1);
            }
        }
    }

    private static int median3(Comparable[] a, int i, int j, int k) {
        return QuickBentleyMcIlroy.less(a[i], a[j]) ? (QuickBentleyMcIlroy.less(a[j], a[k]) ? j : (QuickBentleyMcIlroy.less(a[i], a[k]) ? k : i)) : (QuickBentleyMcIlroy.less(a[k], a[j]) ? j : (QuickBentleyMcIlroy.less(a[k], a[i]) ? k : i));
    }

    private static boolean less(Comparable v, Comparable w) {
        if (v == w) {
            return false;
        }
        return v.compareTo(w) < 0;
    }

    private static boolean eq(Comparable v, Comparable w) {
        if (v == w) {
            return true;
        }
        return v.compareTo(w) == 0;
    }

    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; ++i) {
            if (!QuickBentleyMcIlroy.less(a[i], a[i - 1])) continue;
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
        QuickBentleyMcIlroy.sort((Comparable[])a);
        assert (QuickBentleyMcIlroy.isSorted((Comparable[])a));
        QuickBentleyMcIlroy.show((Comparable[])a);
    }
}

