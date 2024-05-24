/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Quick3string {
    private static final int CUTOFF = 15;

    private Quick3string() {
    }

    public static void sort(String[] a) {
        StdRandom.shuffle(a);
        Quick3string.sort(a, 0, a.length - 1, 0);
        assert (Quick3string.isSorted(a));
    }

    private static int charAt(String s, int d) {
        assert (d >= 0 && d <= s.length());
        if (d == s.length()) {
            return -1;
        }
        return s.charAt(d);
    }

    private static void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo + 15) {
            Quick3string.insertion(a, lo, hi, d);
            return;
        }
        int lt = lo;
        int gt = hi;
        int v = Quick3string.charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = Quick3string.charAt(a[i], d);
            if (t < v) {
                Quick3string.exch(a, lt++, i++);
                continue;
            }
            if (t > v) {
                Quick3string.exch(a, i, gt--);
                continue;
            }
            ++i;
        }
        Quick3string.sort(a, lo, lt - 1, d);
        if (v >= 0) {
            Quick3string.sort(a, lt, gt, d + 1);
        }
        Quick3string.sort(a, gt + 1, hi, d);
    }

    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && Quick3string.less(a[j], a[j - 1], d); --j) {
                Quick3string.exch(a, j, j - 1);
            }
        }
    }

    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(String v, String w, int d) {
        assert (v.substring(0, d).equals(w.substring(0, d)));
        for (int i = d; i < Math.min(v.length(), w.length()); ++i) {
            if (v.charAt(i) < w.charAt(i)) {
                return true;
            }
            if (v.charAt(i) <= w.charAt(i)) continue;
            return false;
        }
        return v.length() < w.length();
    }

    private static boolean isSorted(String[] a) {
        for (int i = 1; i < a.length; ++i) {
            if (a[i].compareTo(a[i - 1]) >= 0) continue;
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        Quick3string.sort(a);
        for (int i = 0; i < n; ++i) {
            StdOut.println(a[i]);
        }
    }
}

