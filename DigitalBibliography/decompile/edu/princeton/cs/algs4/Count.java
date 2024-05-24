/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Count {
    private Count() {
    }

    public static void main(String[] args) {
        int c;
        Alphabet alphabet = new Alphabet(args[0]);
        int R = alphabet.radix();
        int[] count = new int[R];
        while (StdIn.hasNextChar()) {
            c = StdIn.readChar();
            if (!alphabet.contains((char)c)) continue;
            int n = alphabet.toIndex((char)c);
            count[n] = count[n] + 1;
        }
        for (c = 0; c < R; ++c) {
            StdOut.println(alphabet.toChar(c) + " " + count[c]);
        }
    }
}

