/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.io.BufferedOutputStream;
import java.io.IOException;

public final class BinaryStdOut {
    private static BufferedOutputStream out;
    private static int buffer;
    private static int n;
    private static boolean isInitialized;

    private BinaryStdOut() {
    }

    private static void initialize() {
        out = new BufferedOutputStream(System.out);
        buffer = 0;
        n = 0;
        isInitialized = true;
    }

    private static void writeBit(boolean bit) {
        if (!isInitialized) {
            BinaryStdOut.initialize();
        }
        buffer <<= 1;
        if (bit) {
            buffer |= 1;
        }
        if (++n == 8) {
            BinaryStdOut.clearBuffer();
        }
    }

    private static void writeByte(int x) {
        if (!isInitialized) {
            BinaryStdOut.initialize();
        }
        assert (x >= 0 && x < 256);
        if (n == 0) {
            try {
                out.write(x);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        for (int i = 0; i < 8; ++i) {
            boolean bit = (x >>> 8 - i - 1 & 1) == 1;
            BinaryStdOut.writeBit(bit);
        }
    }

    private static void clearBuffer() {
        if (!isInitialized) {
            BinaryStdOut.initialize();
        }
        if (n == 0) {
            return;
        }
        if (n > 0) {
            buffer <<= 8 - n;
        }
        try {
            out.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        n = 0;
        buffer = 0;
    }

    public static void flush() {
        BinaryStdOut.clearBuffer();
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        BinaryStdOut.flush();
        try {
            out.close();
            isInitialized = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(boolean x) {
        BinaryStdOut.writeBit(x);
    }

    public static void write(byte x) {
        BinaryStdOut.writeByte(x & 0xFF);
    }

    public static void write(int x) {
        BinaryStdOut.writeByte(x >>> 24 & 0xFF);
        BinaryStdOut.writeByte(x >>> 16 & 0xFF);
        BinaryStdOut.writeByte(x >>> 8 & 0xFF);
        BinaryStdOut.writeByte(x >>> 0 & 0xFF);
    }

    public static void write(int x, int r) {
        if (r == 32) {
            BinaryStdOut.write(x);
            return;
        }
        if (r < 1 || r > 32) {
            throw new IllegalArgumentException("Illegal value for r = " + r);
        }
        if (x < 0 || x >= 1 << r) {
            throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        }
        for (int i = 0; i < r; ++i) {
            boolean bit = (x >>> r - i - 1 & 1) == 1;
            BinaryStdOut.writeBit(bit);
        }
    }

    public static void write(double x) {
        BinaryStdOut.write(Double.doubleToRawLongBits(x));
    }

    public static void write(long x) {
        BinaryStdOut.writeByte((int)(x >>> 56 & 0xFFL));
        BinaryStdOut.writeByte((int)(x >>> 48 & 0xFFL));
        BinaryStdOut.writeByte((int)(x >>> 40 & 0xFFL));
        BinaryStdOut.writeByte((int)(x >>> 32 & 0xFFL));
        BinaryStdOut.writeByte((int)(x >>> 24 & 0xFFL));
        BinaryStdOut.writeByte((int)(x >>> 16 & 0xFFL));
        BinaryStdOut.writeByte((int)(x >>> 8 & 0xFFL));
        BinaryStdOut.writeByte((int)(x >>> 0 & 0xFFL));
    }

    public static void write(float x) {
        BinaryStdOut.write(Float.floatToRawIntBits(x));
    }

    public static void write(short x) {
        BinaryStdOut.writeByte(x >>> 8 & 0xFF);
        BinaryStdOut.writeByte(x >>> 0 & 0xFF);
    }

    public static void write(char x) {
        if (x < '\u0000' || x >= '\u0100') {
            throw new IllegalArgumentException("Illegal 8-bit char = " + x);
        }
        BinaryStdOut.writeByte(x);
    }

    public static void write(char x, int r) {
        if (r == 8) {
            BinaryStdOut.write(x);
            return;
        }
        if (r < 1 || r > 16) {
            throw new IllegalArgumentException("Illegal value for r = " + r);
        }
        if (x >= 1 << r) {
            throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        }
        for (int i = 0; i < r; ++i) {
            boolean bit = (x >>> r - i - 1 & 1) == 1;
            BinaryStdOut.writeBit(bit);
        }
    }

    public static void write(String s) {
        for (int i = 0; i < s.length(); ++i) {
            BinaryStdOut.write(s.charAt(i));
        }
    }

    public static void write(String s, int r) {
        for (int i = 0; i < s.length(); ++i) {
            BinaryStdOut.write(s.charAt(i), r);
        }
    }

    public static void main(String[] args) {
        int m = Integer.parseInt(args[0]);
        for (int i = 0; i < m; ++i) {
            BinaryStdOut.write(i);
        }
        BinaryStdOut.flush();
    }
}

