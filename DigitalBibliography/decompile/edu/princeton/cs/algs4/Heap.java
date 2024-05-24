/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Heap {
    private Heap() {
    }

    public static void sort(Comparable[] pq) {
        int k;
        int n = pq.length;
        for (k = n / 2; k >= 1; --k) {
            Heap.sink(pq, k, n);
        }
        k = n;
        while (k > 1) {
            Heap.exch(pq, 1, k--);
            Heap.sink(pq, 1, k);
        }
    }

    private static void sink(Comparable[] pq, int k, int n) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && Heap.less(pq, j, j + 1)) {
                ++j;
            }
            if (!Heap.less(pq, k, j)) break;
            Heap.exch(pq, k, j);
            k = j;
        }
    }

    private static boolean less(Comparable[] pq, int i, int j) {
        return pq[i - 1].compareTo(pq[j - 1]) < 0;
    }

    private static void exch(Object[] pq, int i, int j) {
        Object swap = pq[i - 1];
        pq[i - 1] = pq[j - 1];
        pq[j - 1] = swap;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Heap.sort((Comparable[])a);
        Heap.show((Comparable[])a);
    }
}

