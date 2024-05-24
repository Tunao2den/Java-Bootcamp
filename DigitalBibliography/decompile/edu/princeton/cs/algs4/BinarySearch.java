/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class BinarySearch {
    private BinarySearch() {
    }

    public static int indexOf(int[] a, int key) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < a[mid]) {
                hi = mid - 1;
                continue;
            }
            if (key > a[mid]) {
                lo = mid + 1;
                continue;
            }
            return mid;
        }
        return -1;
    }

    @Deprecated
    public static int rank(int key, int[] a) {
        return BinarySearch.indexOf(a, key);
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] allowlist = in.readAllInts();
        Arrays.sort(allowlist);
        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (BinarySearch.indexOf(allowlist, key) != -1) continue;
            StdOut.println(key);
        }
    }
}

