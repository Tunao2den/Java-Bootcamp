/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class LSD {
    private static final int BITS_PER_BYTE = 8;

    private LSD() {
    }

    public static void sort(String[] a, int w) {
        int n = a.length;
        int R = 256;
        String[] aux = new String[n];
        for (int d = w - 1; d >= 0; --d) {
            int i;
            int[] count = new int[R + 1];
            for (i = 0; i < n; ++i) {
                int n2 = a[i].charAt(d) + '\u0001';
                count[n2] = count[n2] + 1;
            }
            for (int r = 0; r < R; ++r) {
                int n3 = r + 1;
                count[n3] = count[n3] + count[r];
            }
            for (i = 0; i < n; ++i) {
                char c = a[i].charAt(d);
                int n4 = count[c];
                count[c] = n4 + 1;
                aux[n4] = a[i];
            }
            for (i = 0; i < n; ++i) {
                a[i] = aux[i];
            }
        }
    }

    public static void sort(int[] a) {
        int BITS = 32;
        int R = 256;
        int MASK = 255;
        int w = 4;
        int n = a.length;
        int[] aux = new int[n];
        for (int d = 0; d < 4; ++d) {
            int c;
            int i;
            int[] count = new int[257];
            for (i = 0; i < n; ++i) {
                c = a[i] >> 8 * d & 0xFF;
                int n2 = c + 1;
                count[n2] = count[n2] + 1;
            }
            for (int r = 0; r < 256; ++r) {
                int n3 = r + 1;
                count[n3] = count[n3] + count[r];
            }
            if (d == 3) {
                int shift1 = count[256] - count[128];
                int shift2 = count[128];
                int r = 0;
                while (r < 128) {
                    int n4 = r++;
                    count[n4] = count[n4] + shift1;
                }
                r = 128;
                while (r < 256) {
                    int n5 = r++;
                    count[n5] = count[n5] - shift2;
                }
            }
            for (i = 0; i < n; ++i) {
                int n6 = c = a[i] >> 8 * d & 0xFF;
                int n7 = count[n6];
                count[n6] = n7 + 1;
                aux[n7] = a[i];
            }
            int[] temp = a;
            a = aux;
            aux = temp;
        }
    }

    public static void main(String[] args) {
        int i;
        String[] a = StdIn.readAllStrings();
        int n = a.length;
        int w = a[0].length();
        for (i = 0; i < n; ++i) {
            assert (a[i].length() == w) : "Strings must have fixed length";
        }
        LSD.sort(a, w);
        for (i = 0; i < n; ++i) {
            StdOut.println(a[i]);
        }
    }
}

