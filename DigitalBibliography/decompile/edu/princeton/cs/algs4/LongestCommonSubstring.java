/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SuffixArray;

public class LongestCommonSubstring {
    private LongestCommonSubstring() {
    }

    private static String lcp(String s, int p, String t, int q) {
        int n = Math.min(s.length() - p, t.length() - q);
        for (int i = 0; i < n; ++i) {
            if (s.charAt(p + i) == t.charAt(q + i)) continue;
            return s.substring(p, p + i);
        }
        return s.substring(p, p + n);
    }

    private static int compare(String s, int p, String t, int q) {
        int n = Math.min(s.length() - p, t.length() - q);
        for (int i = 0; i < n; ++i) {
            if (s.charAt(p + i) == t.charAt(q + i)) continue;
            return s.charAt(p + i) - t.charAt(q + i);
        }
        if (s.length() - p < t.length() - q) {
            return -1;
        }
        if (s.length() - p > t.length() - q) {
            return 1;
        }
        return 0;
    }

    public static String lcs(String s, String t) {
        SuffixArray suffix1 = new SuffixArray(s);
        SuffixArray suffix2 = new SuffixArray(t);
        String lcs = "";
        int i = 0;
        int j = 0;
        while (i < s.length() && j < t.length()) {
            int q;
            int p = suffix1.index(i);
            String x = LongestCommonSubstring.lcp(s, p, t, q = suffix2.index(j));
            if (x.length() > lcs.length()) {
                lcs = x;
            }
            if (LongestCommonSubstring.compare(s, p, t, q) < 0) {
                ++i;
                continue;
            }
            ++j;
        }
        return lcs;
    }

    public static void main(String[] args) {
        In in1 = new In(args[0]);
        In in2 = new In(args[1]);
        String s = in1.readAll().trim().replaceAll("\\s+", " ");
        String t = in2.readAll().trim().replaceAll("\\s+", " ");
        StdOut.println("'" + LongestCommonSubstring.lcs(s, t) + "'");
    }
}

