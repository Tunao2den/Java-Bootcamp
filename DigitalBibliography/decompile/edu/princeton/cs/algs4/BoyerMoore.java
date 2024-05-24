/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;

public class BoyerMoore {
    private final int R;
    private int[] right;
    private char[] pattern;
    private String pat;

    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;
        this.right = new int[this.R];
        for (int c = 0; c < this.R; ++c) {
            this.right[c] = -1;
        }
        for (int j = 0; j < pat.length(); ++j) {
            this.right[pat.charAt((int)j)] = j;
        }
    }

    public BoyerMoore(char[] pattern, int R) {
        int j;
        this.R = R;
        this.pattern = new char[pattern.length];
        for (j = 0; j < pattern.length; ++j) {
            this.pattern[j] = pattern[j];
        }
        this.right = new int[R];
        for (int c = 0; c < R; ++c) {
            this.right[c] = -1;
        }
        for (j = 0; j < pattern.length; ++j) {
            this.right[pattern[j]] = j;
        }
    }

    public int search(String txt) {
        int skip;
        int m = this.pat.length();
        int n = txt.length();
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; --j) {
                if (this.pat.charAt(j) == txt.charAt(i + j)) continue;
                skip = Math.max(1, j - this.right[txt.charAt(i + j)]);
                break;
            }
            if (skip != 0) continue;
            return i;
        }
        return n;
    }

    public int search(char[] text) {
        int skip;
        int m = this.pattern.length;
        int n = text.length;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; --j) {
                if (this.pattern[j] == text[i + j]) continue;
                skip = Math.max(1, j - this.right[text[i + j]]);
                break;
            }
            if (skip != 0) continue;
            return i;
        }
        return n;
    }

    public static void main(String[] args) {
        int i;
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text = txt.toCharArray();
        BoyerMoore boyermoore1 = new BoyerMoore(pat);
        BoyerMoore boyermoore2 = new BoyerMoore(pattern, 256);
        int offset1 = boyermoore1.search(txt);
        int offset2 = boyermoore2.search(text);
        StdOut.println("text:    " + txt);
        StdOut.print("pattern: ");
        for (i = 0; i < offset1; ++i) {
            StdOut.print(" ");
        }
        StdOut.println(pat);
        StdOut.print("pattern: ");
        for (i = 0; i < offset2; ++i) {
            StdOut.print(" ");
        }
        StdOut.println(pat);
    }
}

