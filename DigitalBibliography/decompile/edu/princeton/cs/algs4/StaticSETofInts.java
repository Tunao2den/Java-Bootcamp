/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.util.Arrays;

public class StaticSETofInts {
    private int[] a;

    public StaticSETofInts(int[] keys) {
        int i;
        this.a = new int[keys.length];
        for (i = 0; i < keys.length; ++i) {
            this.a[i] = keys[i];
        }
        Arrays.sort(this.a);
        for (i = 1; i < this.a.length; ++i) {
            if (this.a[i] != this.a[i - 1]) continue;
            throw new IllegalArgumentException("Argument arrays contains duplicate keys.");
        }
    }

    public boolean contains(int key) {
        return this.rank(key) != -1;
    }

    public int rank(int key) {
        int lo = 0;
        int hi = this.a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (key < this.a[mid]) {
                hi = mid - 1;
                continue;
            }
            if (key > this.a[mid]) {
                lo = mid + 1;
                continue;
            }
            return mid;
        }
        return -1;
    }
}

