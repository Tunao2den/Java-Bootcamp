/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;

public class KMP {
    private final int R;
    private final int m;
    private int[][] dfa;

    public KMP(String pat) {
        this.R = 256;
        this.m = pat.length();
        this.dfa = new int[this.R][this.m];
        this.dfa[pat.charAt((int)0)][0] = 1;
        int x = 0;
        for (int j = 1; j < this.m; ++j) {
            for (int c = 0; c < this.R; ++c) {
                this.dfa[c][j] = this.dfa[c][x];
            }
            this.dfa[pat.charAt((int)j)][j] = j + 1;
            x = this.dfa[pat.charAt(j)][x];
        }
    }

    public KMP(char[] pattern, int R) {
        this.R = R;
        this.m = pattern.length;
        int m = pattern.length;
        this.dfa = new int[R][m];
        this.dfa[pattern[0]][0] = 1;
        int x = 0;
        for (int j = 1; j < m; ++j) {
            for (int c = 0; c < R; ++c) {
                this.dfa[c][j] = this.dfa[c][x];
            }
            this.dfa[pattern[j]][j] = j + 1;
            x = this.dfa[pattern[j]][x];
        }
    }

    public int search(String txt) {
        int i;
        int n = txt.length();
        int j = 0;
        for (i = 0; i < n && j < this.m; ++i) {
            j = this.dfa[txt.charAt(i)][j];
        }
        if (j == this.m) {
            return i - this.m;
        }
        return n;
    }

    public int search(char[] text) {
        int i;
        int n = text.length;
        int j = 0;
        for (i = 0; i < n && j < this.m; ++i) {
            j = this.dfa[text[i]][j];
        }
        if (j == this.m) {
            return i - this.m;
        }
        return n;
    }

    public static void main(String[] args) {
        int i;
        String pat = args[0];
        String txt = args[1];
        char[] pattern = pat.toCharArray();
        char[] text = txt.toCharArray();
        KMP kmp1 = new KMP(pat);
        int offset1 = kmp1.search(txt);
        KMP kmp2 = new KMP(pattern, 256);
        int offset2 = kmp2.search(text);
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

