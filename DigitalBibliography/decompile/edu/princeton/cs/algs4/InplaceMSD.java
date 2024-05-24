/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class InplaceMSD {
    private static final int R = 256;
    private static final int CUTOFF = 15;

    private InplaceMSD() {
    }

    public static void sort(String[] a) {
        int n = a.length;
        InplaceMSD.sort(a, 0, n - 1, 0);
    }

    private static int charAt(String s, int d) {
        assert (d >= 0 && d <= s.length());
        if (d == s.length()) {
            return -1;
        }
        return s.charAt(d);
    }

    private static void sort(String[] a, int lo, int hi, int d) {
        int r;
        int c;
        if (hi <= lo + 15) {
            InplaceMSD.insertion(a, lo, hi, d);
            return;
        }
        int[] heads = new int[258];
        int[] tails = new int[257];
        for (int i = lo; i <= hi; ++i) {
            c = InplaceMSD.charAt(a[i], d);
            int n = c + 2;
            heads[n] = heads[n] + 1;
        }
        heads[0] = lo;
        for (r = 0; r < 257; ++r) {
            int n = r + 1;
            heads[n] = heads[n] + heads[r];
            tails[r] = heads[r + 1];
        }
        for (r = 0; r < 257; ++r) {
            while (heads[r] < tails[r]) {
                c = InplaceMSD.charAt(a[heads[r]], d);
                while (c + 1 != r) {
                    int n = c + 1;
                    int n2 = heads[n];
                    heads[n] = n2 + 1;
                    InplaceMSD.exch(a, heads[r], n2);
                    c = InplaceMSD.charAt(a[heads[r]], d);
                }
                int n = r;
                heads[n] = heads[n] + 1;
            }
        }
        for (r = 0; r < 256; ++r) {
            InplaceMSD.sort(a, tails[r], tails[r + 1] - 1, d + 1);
        }
    }

    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && InplaceMSD.less(a[j], a[j - 1], d); --j) {
                InplaceMSD.exch(a, j, j - 1);
            }
        }
    }

    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    private static boolean less(String v, String w, int d) {
        for (int i = d; i < Math.min(v.length(), w.length()); ++i) {
            if (v.charAt(i) < w.charAt(i)) {
                return true;
            }
            if (v.charAt(i) <= w.charAt(i)) continue;
            return false;
        }
        return v.length() < w.length();
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        InplaceMSD.sort(a);
        for (int i = 0; i < n; ++i) {
            StdOut.println(a[i]);
        }
    }
}

