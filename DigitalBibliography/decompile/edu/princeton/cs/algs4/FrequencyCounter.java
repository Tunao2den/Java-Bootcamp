/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FrequencyCounter {
    private FrequencyCounter() {
    }

    public static void main(String[] args) {
        int distinct = 0;
        int words = 0;
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();
        while (!StdIn.isEmpty()) {
            String key = StdIn.readString();
            if (key.length() < minlen) continue;
            ++words;
            if (st.contains(key)) {
                st.put(key, (Integer)st.get(key) + 1);
                continue;
            }
            st.put(key, 1);
            ++distinct;
        }
        String max = "";
        st.put(max, 0);
        for (String word : st.keys()) {
            if ((Integer)st.get(word) <= (Integer)st.get(max)) continue;
            max = word;
        }
        StdOut.println(max + " " + st.get(max));
        StdOut.println("distinct = " + distinct);
        StdOut.println("words    = " + words);
    }
}

