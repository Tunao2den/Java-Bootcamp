/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class RunLength {
    private static final int R = 256;
    private static final int LG_R = 8;

    private RunLength() {
    }

    public static void expand() {
        boolean b = false;
        while (!BinaryStdIn.isEmpty()) {
            int run = BinaryStdIn.readInt(8);
            for (int i = 0; i < run; ++i) {
                BinaryStdOut.write(b);
            }
            b = !b;
        }
        BinaryStdOut.close();
    }

    public static void compress() {
        char run = '\u0000';
        boolean old = false;
        while (!BinaryStdIn.isEmpty()) {
            boolean b = BinaryStdIn.readBoolean();
            if (b != old) {
                BinaryStdOut.write(run, 8);
                run = '\u0001';
                old = !old;
                continue;
            }
            if (run == '\u00ff') {
                BinaryStdOut.write(run, 8);
                run = '\u0000';
                BinaryStdOut.write(run, 8);
            }
            run = (char)(run + '\u0001');
        }
        BinaryStdOut.write(run, 8);
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            RunLength.compress();
        } else if (args[0].equals("+")) {
            RunLength.expand();
        } else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }
}

