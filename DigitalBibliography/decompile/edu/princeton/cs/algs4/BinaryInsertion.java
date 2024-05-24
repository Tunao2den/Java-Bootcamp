/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BinaryInsertion {
    private BinaryInsertion() {
    }

    public static void sort(Comparable[] a) {
        int n = a.length;
        for (int i = 1; i < n; ++i) {
            Comparable v = a[i];
            int lo = 0;
            int hi = i;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (BinaryInsertion.less(v, a[mid])) {
                    hi = mid;
                    continue;
                }
                lo = mid + 1;
            }
            for (int j = i; j > lo; --j) {
                a[j] = a[j - 1];
            }
            a[lo] = v;
        }
        assert (BinaryInsertion.isSorted(a));
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static boolean isSorted(Comparable[] a) {
        return BinaryInsertion.isSorted(a, 0, a.length - 1);
    }

    private static boolean isSorted(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; ++i) {
            if (!BinaryInsertion.less(a[i], a[i - 1])) continue;
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
        BinaryInsertion.sort((Comparable[])a);
        BinaryInsertion.show((Comparable[])a);
    }
}

