/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Locale;

public class Out {
    private static final String CHARSET_NAME = "UTF-8";
    private static final Locale LOCALE = Locale.US;
    private PrintWriter out;

    public Out(OutputStream os) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            this.out = new PrintWriter(osw, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Out() {
        this(System.out);
    }

    public Out(Socket socket) {
        try {
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, CHARSET_NAME);
            this.out = new PrintWriter(osw, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Out(String filename) {
        try {
            FileOutputStream os = new FileOutputStream(filename);
            OutputStreamWriter osw = new OutputStreamWriter((OutputStream)os, CHARSET_NAME);
            this.out = new PrintWriter(osw, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.out.close();
    }

    public void println() {
        this.out.println();
    }

    public void println(Object x) {
        this.out.println(x);
    }

    public void println(boolean x) {
        this.out.println(x);
    }

    public void println(char x) {
        this.out.println(x);
    }

    public void println(double x) {
        this.out.println(x);
    }

    public void println(float x) {
        this.out.println(x);
    }

    public void println(int x) {
        this.out.println(x);
    }

    public void println(long x) {
        this.out.println(x);
    }

    public void println(byte x) {
        this.out.println(x);
    }

    public void print() {
        this.out.flush();
    }

    public void print(Object x) {
        this.out.print(x);
        this.out.flush();
    }

    public void print(boolean x) {
        this.out.print(x);
        this.out.flush();
    }

    public void print(char x) {
        this.out.print(x);
        this.out.flush();
    }

    public void print(double x) {
        this.out.print(x);
        this.out.flush();
    }

    public void print(float x) {
        this.out.print(x);
        this.out.flush();
    }

    public void print(int x) {
        this.out.print(x);
        this.out.flush();
    }

    public void print(long x) {
        this.out.print(x);
        this.out.flush();
    }

    public void print(byte x) {
        this.out.print(x);
        this.out.flush();
    }

    public void printf(String format, Object ... args) {
        this.out.printf(LOCALE, format, args);
        this.out.flush();
    }

    public void printf(Locale locale, String format, Object ... args) {
        this.out.printf(locale, format, args);
        this.out.flush();
    }

    public static void main(String[] args) {
        Out out = new Out();
        out.println("Test 1");
        out.close();
        out = new Out("test.txt");
        out.println("Test 2");
        out.close();
    }
}

