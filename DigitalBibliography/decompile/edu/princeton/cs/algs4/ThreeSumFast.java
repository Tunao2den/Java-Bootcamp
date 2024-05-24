/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class ThreeSumFast {
    private ThreeSumFast() {
    }

    private static boolean containsDuplicates(int[] a) {
        for (int i = 1; i < a.length; ++i) {
            if (a[i] != a[i - 1]) continue;
            return true;
        }
        return false;
    }

    public static void printAll(int[] a) {
        int n = a.length;
        Arrays.sort(a);
        if (ThreeSumFast.containsDuplicates(a)) {
            throw new IllegalArgumentException("array contains duplicate integers");
        }
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                int k = Arrays.binarySearch(a, -(a[i] + a[j]));
                if (k <= j) continue;
                StdOut.println(a[i] + " " + a[j] + " " + a[k]);
            }
        }
    }

    public static int count(int[] a) {
        int n = a.length;
        Arrays.sort(a);
        if (ThreeSumFast.containsDuplicates(a)) {
            throw new IllegalArgumentException("array contains duplicate integers");
        }
        int count = 0;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                int k = Arrays.binarySearch(a, -(a[i] + a[j]));
                if (k <= j) continue;
                ++count;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] a = in.readAllInts();
        int count = ThreeSumFast.count(a);
        StdOut.println(count);
    }
}

