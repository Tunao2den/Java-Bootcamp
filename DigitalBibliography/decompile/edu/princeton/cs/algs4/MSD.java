/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class MSD {
    private static final int BITS_PER_BYTE = 8;
    private static final int BITS_PER_INT = 32;
    private static final int R = 256;
    private static final int CUTOFF = 15;

    private MSD() {
    }

    public static void sort(String[] a) {
        int n = a.length;
        String[] aux = new String[n];
        MSD.sort(a, 0, n - 1, 0, aux);
    }

    private static int charAt(String s, int d) {
        assert (d >= 0 && d <= s.length());
        if (d == s.length()) {
            return -1;
        }
        return s.charAt(d);
    }

    private static void sort(String[] a, int lo, int hi, int d, String[] aux) {
        int r;
        int c;
        int i;
        if (hi <= lo + 15) {
            MSD.insertion(a, lo, hi, d);
            return;
        }
        int[] count = new int[258];
        for (i = lo; i <= hi; ++i) {
            c = MSD.charAt(a[i], d);
            int n = c + 2;
            count[n] = count[n] + 1;
        }
        for (r = 0; r < 257; ++r) {
            int n = r + 1;
            count[n] = count[n] + count[r];
        }
        for (i = lo; i <= hi; ++i) {
            c = MSD.charAt(a[i], d);
            int n = c + 1;
            int n2 = count[n];
            count[n] = n2 + 1;
            aux[n2] = a[i];
        }
        for (i = lo; i <= hi; ++i) {
            a[i] = aux[i - lo];
        }
        for (r = 0; r < 256; ++r) {
            MSD.sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
        }
    }

    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && MSD.less(a[j], a[j - 1], d); --j) {
                MSD.exch(a, j, j - 1);
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

    public static void sort(int[] a) {
        int n = a.length;
        int[] aux = new int[n];
        MSD.sort(a, 0, n - 1, 0, aux);
    }

    private static void sort(int[] a, int lo, int hi, int d, int[] aux) {
        int r;
        int c;
        int i;
        if (hi <= lo + 15) {
            MSD.insertion(a, lo, hi);
            return;
        }
        int[] count = new int[257];
        int mask = 255;
        int shift = 32 - 8 * d - 8;
        for (i = lo; i <= hi; ++i) {
            c = a[i] >> shift & mask;
            int n = c + 1;
            count[n] = count[n] + 1;
        }
        for (r = 0; r < 256; ++r) {
            int n = r + 1;
            count[n] = count[n] + count[r];
        }
        if (d == 0) {
            int shift1 = count[256] - count[128];
            int shift2 = count[128];
            count[256] = shift1 + count[1];
            int r2 = 0;
            while (r2 < 128) {
                int n = r2++;
                count[n] = count[n] + shift1;
            }
            r2 = 128;
            while (r2 < 256) {
                int n = r2++;
                count[n] = count[n] - shift2;
            }
        }
        for (i = lo; i <= hi; ++i) {
            int n = c = a[i] >> shift & mask;
            int n2 = count[n];
            count[n] = n2 + 1;
            aux[n2] = a[i];
        }
        for (i = lo; i <= hi; ++i) {
            a[i] = aux[i - lo];
        }
        if (d == 3) {
            return;
        }
        if (d == 0 && count[128] > 0) {
            MSD.sort(a, lo, lo + count[128] - 1, d + 1, aux);
        }
        if (d != 0 && count[0] > 0) {
            MSD.sort(a, lo, lo + count[0] - 1, d + 1, aux);
        }
        for (r = 0; r < 256; ++r) {
            if (count[r + 1] <= count[r]) continue;
            MSD.sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
        }
    }

    private static void insertion(int[] a, int lo, int hi) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && a[j] < a[j - 1]; --j) {
                MSD.exch(a, j, j - 1);
            }
        }
    }

    private static void exch(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        MSD.sort(a);
        for (int i = 0; i < n; ++i) {
            StdOut.println(a[i]);
        }
    }
}

