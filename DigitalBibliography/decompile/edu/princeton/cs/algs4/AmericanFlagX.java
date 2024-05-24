/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class AmericanFlagX {
    private static final int R = 256;
    private static final int CUTOFF = 15;

    private AmericanFlagX() {
    }

    private static int charAt(String s, int d) {
        assert (d >= 0 && d <= s.length());
        if (d == s.length()) {
            return -1;
        }
        return s.charAt(d);
    }

    public static void sort(String[] a) {
        AmericanFlagX.sort(a, 0, a.length - 1);
    }

    public static void sort(String[] a, int lo, int hi) {
        Stack<Integer> st = new Stack<Integer>();
        int[] count = new int[257];
        int d = 0;
        st.push(lo);
        st.push(hi);
        st.push(d);
        while (!st.isEmpty()) {
            int c;
            int c2;
            d = (Integer)st.pop();
            hi = (Integer)st.pop();
            if (hi <= (lo = ((Integer)st.pop()).intValue()) + 15) {
                AmericanFlagX.insertion(a, lo, hi, d);
                continue;
            }
            for (int i = lo; i <= hi; ++i) {
                int n = c2 = AmericanFlagX.charAt(a[i], d) + 1;
                count[n] = count[n] + 1;
            }
            count[0] = count[0] + lo;
            for (c = 0; c < 256; ++c) {
                int n = c + 1;
                count[n] = count[n] + count[c];
                if (c <= 0 || count[c + 1] - 1 <= count[c]) continue;
                st.push(count[c]);
                st.push(count[c + 1] - 1);
                st.push(d + 1);
            }
            block3: for (int r = hi; r >= lo; --r) {
                c2 = AmericanFlagX.charAt(a[r], d) + 1;
                while (r >= lo && count[c2] - 1 <= r) {
                    if (count[c2] - 1 == r) {
                        int n = c2;
                        count[n] = count[n] - 1;
                    }
                    if (--r < lo) continue;
                    c2 = AmericanFlagX.charAt(a[r], d) + 1;
                }
                if (r < lo) break;
                while (true) {
                    int n = c2;
                    count[n] = count[n] - 1;
                    if (count[n] == r) continue block3;
                    AmericanFlagX.exch(a, r, count[c2]);
                    c2 = AmericanFlagX.charAt(a[r], d) + 1;
                }
            }
            for (c = 0; c < 257; ++c) {
                count[c] = 0;
            }
        }
    }

    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && AmericanFlagX.less(a[j], a[j - 1], d); --j) {
                AmericanFlagX.exch(a, j, j - 1);
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
        AmericanFlagX.sort(a);
        for (int i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
    }
}

