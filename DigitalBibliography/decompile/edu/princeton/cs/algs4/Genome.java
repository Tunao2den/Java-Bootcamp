/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class Genome {
    private Genome() {
    }

    public static void compress() {
        Alphabet DNA = Alphabet.DNA;
        String s = BinaryStdIn.readString();
        int n = s.length();
        BinaryStdOut.write(n);
        for (int i = 0; i < n; ++i) {
            int d = DNA.toIndex(s.charAt(i));
            BinaryStdOut.write(d, 2);
        }
        BinaryStdOut.close();
    }

    public static void expand() {
        Alphabet DNA = Alphabet.DNA;
        int n = BinaryStdIn.readInt();
        for (int i = 0; i < n; ++i) {
            char c = BinaryStdIn.readChar(2);
            BinaryStdOut.write(DNA.toChar(c), 8);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            Genome.compress();
        } else if (args[0].equals("+")) {
            Genome.expand();
        } else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }
}

