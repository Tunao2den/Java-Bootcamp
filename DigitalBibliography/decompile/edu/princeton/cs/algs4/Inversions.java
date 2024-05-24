/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Inversions {
    private Inversions() {
    }

    private static long merge(int[] a, int[] aux, int lo, int mid, int hi) {
        long inversions = 0L;
        for (int k = lo; k <= hi; ++k) {
            aux[k] = a[k];
        }
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            if (i > mid) {
                a[k] = aux[j++];
                continue;
            }
            if (j > hi) {
                a[k] = aux[i++];
                continue;
            }
            if (aux[j] < aux[i]) {
                a[k] = aux[j++];
                inversions += (long)(mid - i + 1);
                continue;
            }
            a[k] = aux[i++];
        }
        return inversions;
    }

    private static long count(int[] a, int[] b, int[] aux, int lo, int hi) {
        long inversions = 0L;
        if (hi <= lo) {
            return 0L;
        }
        int mid = lo + (hi - lo) / 2;
        inversions += Inversions.count(a, b, aux, lo, mid);
        inversions += Inversions.count(a, b, aux, mid + 1, hi);
        assert ((inversions += Inversions.merge(b, aux, lo, mid, hi)) == Inversions.brute(a, lo, hi));
        return inversions;
    }

    public static long count(int[] a) {
        int[] b = new int[a.length];
        int[] aux = new int[a.length];
        for (int i = 0; i < a.length; ++i) {
            b[i] = a[i];
        }
        long inversions = Inversions.count(a, b, aux, 0, a.length - 1);
        return inversions;
    }

    private static <Key extends Comparable<Key>> long merge(Key[] a, Key[] aux, int lo, int mid, int hi) {
        long inversions = 0L;
        for (int k = lo; k <= hi; ++k) {
            aux[k] = a[k];
        }
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            if (i > mid) {
                a[k] = aux[j++];
                continue;
            }
            if (j > hi) {
                a[k] = aux[i++];
                continue;
            }
            if (Inversions.less(aux[j], aux[i])) {
                a[k] = aux[j++];
                inversions += (long)(mid - i + 1);
                continue;
            }
            a[k] = aux[i++];
        }
        return inversions;
    }

    private static <Key extends Comparable<Key>> long count(Key[] a, Key[] b, Key[] aux, int lo, int hi) {
        long inversions = 0L;
        if (hi <= lo) {
            return 0L;
        }
        int mid = lo + (hi - lo) / 2;
        inversions += Inversions.count(a, b, aux, (int)lo, (int)mid);
        inversions += Inversions.count(a, b, aux, (int)(mid + 1), (int)hi);
        assert ((inversions += Inversions.merge(b, aux, (int)lo, (int)mid, (int)hi)) == Inversions.brute(a, (int)lo, (int)hi));
        return inversions;
    }

    public static <Key extends Comparable<Key>> long count(Key[] a) {
        Comparable[] b = (Comparable[])a.clone();
        Comparable[] aux = (Comparable[])a.clone();
        long inversions = Inversions.count(a, (Comparable[])b, (Comparable[])aux, (int)0, (int)(a.length - 1));
        return inversions;
    }

    private static <Key extends Comparable<Key>> boolean less(Key v, Key w) {
        return v.compareTo(w) < 0;
    }

    private static <Key extends Comparable<Key>> long brute(Key[] a, int lo, int hi) {
        long inversions = 0L;
        for (int i = lo; i <= hi; ++i) {
            for (int j = i + 1; j <= hi; ++j) {
                if (!Inversions.less(a[j], a[i])) continue;
                ++inversions;
            }
        }
        return inversions;
    }

    private static long brute(int[] a, int lo, int hi) {
        long inversions = 0L;
        for (int i = lo; i <= hi; ++i) {
            for (int j = i + 1; j <= hi; ++j) {
                if (a[j] >= a[i]) continue;
                ++inversions;
            }
        }
        return inversions;
    }

    public static void main(String[] args) {
        int[] a = StdIn.readAllInts();
        int n = a.length;
        Comparable[] b = new Integer[n];
        for (int i = 0; i < n; ++i) {
            b[i] = Integer.valueOf(a[i]);
        }
        StdOut.println(Inversions.count(a));
        StdOut.println(Inversions.count((Comparable[])b));
    }
}

