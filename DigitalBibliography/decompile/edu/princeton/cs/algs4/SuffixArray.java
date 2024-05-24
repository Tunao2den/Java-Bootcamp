/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class SuffixArray {
    private Suffix[] suffixes;

    public SuffixArray(String text) {
        int n = text.length();
        this.suffixes = new Suffix[n];
        for (int i = 0; i < n; ++i) {
            this.suffixes[i] = new Suffix(text, i);
        }
        Arrays.sort(this.suffixes);
    }

    public int length() {
        return this.suffixes.length;
    }

    public int index(int i) {
        if (i < 0 || i >= this.suffixes.length) {
            throw new IllegalArgumentException();
        }
        return this.suffixes[i].index;
    }

    public int lcp(int i) {
        if (i < 1 || i >= this.suffixes.length) {
            throw new IllegalArgumentException();
        }
        return SuffixArray.lcpSuffix(this.suffixes[i], this.suffixes[i - 1]);
    }

    private static int lcpSuffix(Suffix s, Suffix t) {
        int n = Math.min(s.length(), t.length());
        for (int i = 0; i < n; ++i) {
            if (s.charAt(i) == t.charAt(i)) continue;
            return i;
        }
        return n;
    }

    public String select(int i) {
        if (i < 0 || i >= this.suffixes.length) {
            throw new IllegalArgumentException();
        }
        return this.suffixes[i].toString();
    }

    public int rank(String query) {
        int lo = 0;
        int hi = this.suffixes.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = SuffixArray.compare(query, this.suffixes[mid]);
            if (cmp < 0) {
                hi = mid - 1;
                continue;
            }
            if (cmp > 0) {
                lo = mid + 1;
                continue;
            }
            return mid;
        }
        return lo;
    }

    private static int compare(String query, Suffix suffix) {
        int n = Math.min(query.length(), suffix.length());
        for (int i = 0; i < n; ++i) {
            if (query.charAt(i) < suffix.charAt(i)) {
                return -1;
            }
            if (query.charAt(i) <= suffix.charAt(i)) continue;
            return 1;
        }
        return query.length() - suffix.length();
    }

    public static void main(String[] args) {
        String s = StdIn.readAll().replaceAll("\\s+", " ").trim();
        SuffixArray suffix = new SuffixArray(s);
        StdOut.println("  i ind lcp rnk select");
        StdOut.println("---------------------------");
        for (int i = 0; i < s.length(); ++i) {
            int index = suffix.index(i);
            String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
            assert (s.substring(index).equals(suffix.select(i)));
            int rank = suffix.rank(s.substring(index));
            if (i == 0) {
                StdOut.printf("%3d %3d %3s %3d %s\n", i, index, "-", rank, ith);
                continue;
            }
            int lcp = suffix.lcp(i);
            StdOut.printf("%3d %3d %3d %3d %s\n", i, index, lcp, rank, ith);
        }
    }

    private static class Suffix
    implements Comparable<Suffix> {
        private final String text;
        private final int index;

        private Suffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        private int length() {
            return this.text.length() - this.index;
        }

        private char charAt(int i) {
            return this.text.charAt(this.index + i);
        }

        @Override
        public int compareTo(Suffix that) {
            if (this == that) {
                return 0;
            }
            int n = Math.min(this.length(), that.length());
            for (int i = 0; i < n; ++i) {
                if (this.charAt(i) < that.charAt(i)) {
                    return -1;
                }
                if (this.charAt(i) <= that.charAt(i)) continue;
                return 1;
            }
            return this.length() - that.length();
        }

        public String toString() {
            return this.text.substring(this.index);
        }
    }
}

