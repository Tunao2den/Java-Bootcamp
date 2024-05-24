/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SuffixArray;

public class SuffixArrayX {
    private static final int CUTOFF = 5;
    private final char[] text;
    private final int[] index;
    private final int n;

    public SuffixArrayX(String text) {
        this.n = ((String)text).length();
        text = (String)text + "\u0000";
        this.text = ((String)text).toCharArray();
        this.index = new int[this.n];
        for (int i = 0; i < this.n; ++i) {
            this.index[i] = i;
        }
        this.sort(0, this.n - 1, 0);
    }

    private void sort(int lo, int hi, int d) {
        if (hi <= lo + 5) {
            this.insertion(lo, hi, d);
            return;
        }
        int lt = lo;
        int gt = hi;
        char v = this.text[this.index[lo] + d];
        int i = lo + 1;
        while (i <= gt) {
            char t = this.text[this.index[i] + d];
            if (t < v) {
                this.exch(lt++, i++);
                continue;
            }
            if (t > v) {
                this.exch(i, gt--);
                continue;
            }
            ++i;
        }
        this.sort(lo, lt - 1, d);
        if (v > '\u0000') {
            this.sort(lt, gt, d + 1);
        }
        this.sort(gt + 1, hi, d);
    }

    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; ++i) {
            for (int j = i; j > lo && this.less(this.index[j], this.index[j - 1], d); --j) {
                this.exch(j, j - 1);
            }
        }
    }

    private boolean less(int i, int j, int d) {
        if (i == j) {
            return false;
        }
        i += d;
        j += d;
        while (i < this.n && j < this.n) {
            if (this.text[i] < this.text[j]) {
                return true;
            }
            if (this.text[i] > this.text[j]) {
                return false;
            }
            ++i;
            ++j;
        }
        return i > j;
    }

    private void exch(int i, int j) {
        int swap = this.index[i];
        this.index[i] = this.index[j];
        this.index[j] = swap;
    }

    public int length() {
        return this.n;
    }

    public int index(int i) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        return this.index[i];
    }

    public int lcp(int i) {
        if (i < 1 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        return this.lcp(this.index[i], this.index[i - 1]);
    }

    private int lcp(int i, int j) {
        int length = 0;
        while (i < this.n && j < this.n) {
            if (this.text[i] != this.text[j]) {
                return length;
            }
            ++i;
            ++j;
            ++length;
        }
        return length;
    }

    public String select(int i) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException();
        }
        return new String(this.text, this.index[i], this.n - this.index[i]);
    }

    public int rank(String query) {
        int lo = 0;
        int hi = this.n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = this.compare(query, this.index[mid]);
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

    private int compare(String query, int i) {
        int j;
        int m = query.length();
        for (j = 0; i < this.n && j < m; ++i, ++j) {
            if (query.charAt(j) == this.text[i]) continue;
            return query.charAt(j) - this.text[i];
        }
        if (i < this.n) {
            return -1;
        }
        if (j < m) {
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        int i;
        String s = StdIn.readAll().replaceAll("\n", " ").trim();
        SuffixArrayX suffix1 = new SuffixArrayX(s);
        SuffixArray suffix2 = new SuffixArray(s);
        boolean check = true;
        for (i = 0; check && i < s.length(); ++i) {
            if (suffix1.index(i) == suffix2.index(i)) continue;
            StdOut.println("suffix1(" + i + ") = " + suffix1.index(i));
            StdOut.println("suffix2(" + i + ") = " + suffix2.index(i));
            String ith = "\"" + s.substring(suffix1.index(i), Math.min(suffix1.index(i) + 50, s.length())) + "\"";
            String jth = "\"" + s.substring(suffix2.index(i), Math.min(suffix2.index(i) + 50, s.length())) + "\"";
            StdOut.println(ith);
            StdOut.println(jth);
            check = false;
        }
        StdOut.println("  i ind lcp rnk  select");
        StdOut.println("---------------------------");
        for (i = 0; i < s.length(); ++i) {
            int index = suffix2.index(i);
            String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
            int rank = suffix2.rank(s.substring(index));
            assert (s.substring(index).equals(suffix2.select(i)));
            if (i == 0) {
                StdOut.printf("%3d %3d %3s %3d  %s\n", i, index, "-", rank, ith);
                continue;
            }
            int lcp = suffix2.lcp(i);
            StdOut.printf("%3d %3d %3d %3d  %s\n", i, index, lcp, rank, ith);
        }
    }
}

