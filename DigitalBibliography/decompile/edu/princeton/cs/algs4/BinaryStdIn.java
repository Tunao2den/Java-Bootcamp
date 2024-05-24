/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BinaryStdOut;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

public final class BinaryStdIn {
    private static final int EOF = -1;
    private static BufferedInputStream in;
    private static int buffer;
    private static int n;
    private static boolean isInitialized;

    private BinaryStdIn() {
    }

    private static void initialize() {
        in = new BufferedInputStream(System.in);
        buffer = 0;
        n = 0;
        BinaryStdIn.fillBuffer();
        isInitialized = true;
    }

    private static void fillBuffer() {
        try {
            buffer = in.read();
            n = 8;
        } catch (IOException e) {
            System.out.println("EOF");
            buffer = -1;
            n = -1;
        }
    }

    public static void close() {
        if (!isInitialized) {
            BinaryStdIn.initialize();
        }
        try {
            in.close();
            isInitialized = false;
        } catch (IOException ioe) {
            throw new IllegalStateException("Could not close BinaryStdIn", ioe);
        }
    }

    public static boolean isEmpty() {
        if (!isInitialized) {
            BinaryStdIn.initialize();
        }
        return buffer == -1;
    }

    public static boolean readBoolean() {
        boolean bit;
        if (BinaryStdIn.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        boolean bl = bit = (buffer >> --n & 1) == 1;
        if (n == 0) {
            BinaryStdIn.fillBuffer();
        }
        return bit;
    }

    public static char readChar() {
        if (BinaryStdIn.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        if (n == 8) {
            int x = buffer;
            BinaryStdIn.fillBuffer();
            return (char)(x & 0xFF);
        }
        int x = buffer;
        x <<= 8 - n;
        int oldN = n;
        BinaryStdIn.fillBuffer();
        if (BinaryStdIn.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        n = oldN;
        return (char)((x |= buffer >>> n) & 0xFF);
    }

    public static char readChar(int r) {
        if (r < 1 || r > 16) {
            throw new IllegalArgumentException("Illegal value of r = " + r);
        }
        if (r == 8) {
            return BinaryStdIn.readChar();
        }
        char x = '\u0000';
        for (int i = 0; i < r; ++i) {
            x = (char)(x << 1);
            boolean bit = BinaryStdIn.readBoolean();
            if (!bit) continue;
            x = (char)(x | '\u0001');
        }
        return x;
    }

    public static String readString() {
        if (BinaryStdIn.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            sb.append(c);
        }
        return sb.toString();
    }

    public static short readShort() {
        short x = 0;
        for (int i = 0; i < 2; ++i) {
            char c = BinaryStdIn.readChar();
            x = (short)(x << 8);
            x = (short)(x | c);
        }
        return x;
    }

    public static int readInt() {
        int x = 0;
        for (int i = 0; i < 4; ++i) {
            char c = BinaryStdIn.readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

    public static int readInt(int r) {
        if (r < 1 || r > 32) {
            throw new IllegalArgumentException("Illegal value of r = " + r);
        }
        if (r == 32) {
            return BinaryStdIn.readInt();
        }
        int x = 0;
        for (int i = 0; i < r; ++i) {
            x <<= 1;
            boolean bit = BinaryStdIn.readBoolean();
            if (!bit) continue;
            x |= 1;
        }
        return x;
    }

    public static long readLong() {
        long x = 0L;
        for (int i = 0; i < 8; ++i) {
            char c = BinaryStdIn.readChar();
            x <<= 8;
            x |= (long)c;
        }
        return x;
    }

    public static double readDouble() {
        return Double.longBitsToDouble(BinaryStdIn.readLong());
    }

    public static float readFloat() {
        return Float.intBitsToFloat(BinaryStdIn.readInt());
    }

    public static byte readByte() {
        char c = BinaryStdIn.readChar();
        return (byte)(c & 0xFF);
    }

    public static void main(String[] args) {
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
    }
}

