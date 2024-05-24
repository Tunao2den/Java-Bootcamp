/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.TST;

public class LZW {
    private static final int R = 256;
    private static final int L = 4096;
    private static final int W = 12;

    private LZW() {
    }

    public static void compress() {
        String input = BinaryStdIn.readString();
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < 256; ++i) {
            st.put("" + (char)i, i);
        }
        int code = 257;
        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);
            BinaryStdOut.write((Integer)st.get(s), 12);
            int t = s.length();
            if (t < input.length() && code < 4096) {
                st.put(input.substring(0, t + 1), code++);
            }
            input = input.substring(t);
        }
        BinaryStdOut.write(256, 12);
        BinaryStdOut.close();
    }

    public static void expand() {
        int i;
        String[] st = new String[4096];
        for (i = 0; i < 256; ++i) {
            st[i] = "" + (char)i;
        }
        st[i++] = "";
        int codeword = BinaryStdIn.readInt(12);
        if (codeword == 256) {
            return;
        }
        String val = st[codeword];
        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(12);
            if (codeword == 256) break;
            Object s = st[codeword];
            if (i == codeword) {
                s = val + val.charAt(0);
            }
            if (i < 4096) {
                st[i++] = val + ((String)s).charAt(0);
            }
            val = s;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            LZW.compress();
        } else if (args[0].equals("+")) {
            LZW.expand();
        } else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }
}

