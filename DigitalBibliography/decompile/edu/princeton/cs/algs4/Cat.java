/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class Cat {
    private Cat() {
    }

    public static void main(String[] args) {
        Out out = new Out(args[args.length - 1]);
        for (int i = 0; i < args.length - 1; ++i) {
            In in = new In(args[i]);
            String s = in.readAll();
            out.println(s);
            in.close();
        }
        out.close();
    }
}

