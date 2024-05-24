/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class LookupIndex {
    private LookupIndex() {
    }

    public static void main(String[] args) {
        String filename = args[0];
        String separator = args[1];
        In in = new In(filename);
        ST st = new ST();
        ST ts = new ST();
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] fields = line.split(separator);
            String key = fields[0];
            for (int i = 1; i < fields.length; ++i) {
                String val = fields[i];
                if (!st.contains(key)) {
                    st.put(key, new Queue());
                }
                if (!ts.contains(val)) {
                    ts.put(val, new Queue());
                }
                ((Queue)st.get(key)).enqueue(val);
                ((Queue)ts.get(val)).enqueue(key);
            }
        }
        StdOut.println("Done indexing");
        while (!StdIn.isEmpty()) {
            String query = StdIn.readLine();
            if (st.contains(query)) {
                for (String vals : (Queue)st.get(query)) {
                    StdOut.println("  " + vals);
                }
            }
            if (!ts.contains(query)) continue;
            for (String keys : (Queue)ts.get(query)) {
                StdOut.println("  " + keys);
            }
        }
    }
}

