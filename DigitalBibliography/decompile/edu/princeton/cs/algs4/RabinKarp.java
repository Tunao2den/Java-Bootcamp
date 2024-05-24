/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import java.math.BigInteger;
import java.util.Random;

public class RabinKarp {
    private String pat;
    private long patHash;
    private int m;
    private long q;
    private int R;
    private long RM;

    public RabinKarp(char[] pattern, int R) {
        this.pat = String.valueOf(pattern);
        this.R = R;
        throw new UnsupportedOperationException("Operation not supported yet");
    }

    public RabinKarp(String pat) {
        this.pat = pat;
        this.R = 256;
        this.m = pat.length();
        this.q = RabinKarp.longRandomPrime();
        this.RM = 1L;
        for (int i = 1; i <= this.m - 1; ++i) {
            this.RM = (long)this.R * this.RM % this.q;
        }
        this.patHash = this.hash(pat, this.m);
    }

    private long hash(String key, int m) {
        long h = 0L;
        for (int j = 0; j < m; ++j) {
            h = ((long)this.R * h + (long)key.charAt(j)) % this.q;
        }
        return h;
    }

    private boolean check(String txt, int i) {
        for (int j = 0; j < this.m; ++j) {
            if (this.pat.charAt(j) == txt.charAt(i + j)) continue;
            return false;
        }
        return true;
    }

    public int search(String txt) {
        int n = txt.length();
        if (n < this.m) {
            return n;
        }
        long txtHash = this.hash(txt, this.m);
        if (this.patHash == txtHash && this.check(txt, 0)) {
            return 0;
        }
        for (int i = this.m; i < n; ++i) {
            txtHash = (txtHash + this.q - this.RM * (long)txt.charAt(i - this.m) % this.q) % this.q;
            txtHash = (txtHash * (long)this.R + (long)txt.charAt(i)) % this.q;
            int offset = i - this.m + 1;
            if (this.patHash != txtHash || !this.check(txt, offset)) continue;
            return offset;
        }
        return n;
    }

    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];
        RabinKarp searcher = new RabinKarp(pat);
        int offset = searcher.search(txt);
        StdOut.println("text:    " + txt);
        StdOut.print("pattern: ");
        for (int i = 0; i < offset; ++i) {
            StdOut.print(" ");
        }
        StdOut.println(pat);
    }
}

