/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BinaryOut;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.NoSuchElementException;

public final class BinaryIn {
    private static final int EOF = -1;
    private BufferedInputStream in;
    private int buffer;
    private int n;

    public BinaryIn() {
        this.in = new BufferedInputStream(System.in);
        this.fillBuffer();
    }

    public BinaryIn(InputStream is) {
        this.in = new BufferedInputStream(is);
        this.fillBuffer();
    }

    public BinaryIn(Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            this.in = new BufferedInputStream(is);
            this.fillBuffer();
        } catch (IOException ioe) {
            System.err.println("Could not open " + socket);
        }
    }

    public BinaryIn(URL url) {
        try {
            URLConnection site = url.openConnection();
            InputStream is = site.getInputStream();
            this.in = new BufferedInputStream(is);
            this.fillBuffer();
        } catch (IOException ioe) {
            System.err.println("Could not open " + url);
        }
    }

    public BinaryIn(String name) {
        try {
            File file = new File(name);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                this.in = new BufferedInputStream(fis);
                this.fillBuffer();
                return;
            }
            URL url = this.getClass().getResource(name);
            if (url == null) {
                url = new URL(name);
            }
            URLConnection site = url.openConnection();
            InputStream is = site.getInputStream();
            this.in = new BufferedInputStream(is);
            this.fillBuffer();
        } catch (IOException ioe) {
            System.err.println("Could not open " + name);
        }
    }

    private void fillBuffer() {
        try {
            this.buffer = this.in.read();
            this.n = 8;
        } catch (IOException e) {
            System.err.println("EOF");
            this.buffer = -1;
            this.n = -1;
        }
    }

    public boolean exists() {
        return this.in != null;
    }

    public boolean isEmpty() {
        return this.buffer == -1;
    }

    public boolean readBoolean() {
        boolean bit;
        if (this.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        --this.n;
        boolean bl = bit = (this.buffer >> this.n & 1) == 1;
        if (this.n == 0) {
            this.fillBuffer();
        }
        return bit;
    }

    public char readChar() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        if (this.n == 8) {
            int x = this.buffer;
            this.fillBuffer();
            return (char)(x & 0xFF);
        }
        int x = this.buffer;
        x <<= 8 - this.n;
        int oldN = this.n;
        this.fillBuffer();
        if (this.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        this.n = oldN;
        return (char)((x |= this.buffer >>> this.n) & 0xFF);
    }

    public char readChar(int r) {
        if (r < 1 || r > 16) {
            throw new IllegalArgumentException("Illegal value of r = " + r);
        }
        if (r == 8) {
            return this.readChar();
        }
        char x = '\u0000';
        for (int i = 0; i < r; ++i) {
            x = (char)(x << 1);
            boolean bit = this.readBoolean();
            if (!bit) continue;
            x = (char)(x | '\u0001');
        }
        return x;
    }

    public String readString() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Reading from empty input stream");
        }
        StringBuilder sb = new StringBuilder();
        while (!this.isEmpty()) {
            char c = this.readChar();
            sb.append(c);
        }
        return sb.toString();
    }

    public short readShort() {
        short x = 0;
        for (int i = 0; i < 2; ++i) {
            char c = this.readChar();
            x = (short)(x << 8);
            x = (short)(x | c);
        }
        return x;
    }

    public int readInt() {
        int x = 0;
        for (int i = 0; i < 4; ++i) {
            char c = this.readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

    public int readInt(int r) {
        if (r < 1 || r > 32) {
            throw new IllegalArgumentException("Illegal value of r = " + r);
        }
        if (r == 32) {
            return this.readInt();
        }
        int x = 0;
        for (int i = 0; i < r; ++i) {
            x <<= 1;
            boolean bit = this.readBoolean();
            if (!bit) continue;
            x |= 1;
        }
        return x;
    }

    public long readLong() {
        long x = 0L;
        for (int i = 0; i < 8; ++i) {
            char c = this.readChar();
            x <<= 8;
            x |= (long)c;
        }
        return x;
    }

    public double readDouble() {
        return Double.longBitsToDouble(this.readLong());
    }

    public float readFloat() {
        return Float.intBitsToFloat(this.readInt());
    }

    public byte readByte() {
        char c = this.readChar();
        return (byte)(c & 0xFF);
    }

    public static void main(String[] args) {
        BinaryIn in = new BinaryIn(args[0]);
        BinaryOut out = new BinaryOut(args[1]);
        while (!in.isEmpty()) {
            char c = in.readChar();
            out.write(c);
        }
        out.flush();
    }
}

