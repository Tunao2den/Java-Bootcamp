/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Multiway {
    private Multiway() {
    }

    private static void merge(In[] streams) {
        int i;
        int n = streams.length;
        IndexMinPQ<String> pq = new IndexMinPQ<String>(n);
        for (i = 0; i < n; ++i) {
            if (streams[i].isEmpty()) continue;
            pq.insert(i, streams[i].readString());
        }
        while (!pq.isEmpty()) {
            StdOut.print((String)pq.minKey() + " ");
            i = pq.delMin();
            if (streams[i].isEmpty()) continue;
            pq.insert(i, streams[i].readString());
        }
        StdOut.println();
    }

    public static void main(String[] args) {
        int n = args.length;
        In[] streams = new In[n];
        for (int i = 0; i < n; ++i) {
            streams[i] = new In(args[i]);
        }
        Multiway.merge(streams);
    }
}

