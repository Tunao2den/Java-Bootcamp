/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.io.File;

public class FileIndex {
    private FileIndex() {
    }

    public static void main(String[] args) {
        ST st = new ST();
        StdOut.println("Indexing files");
        for (String filename : args) {
            StdOut.println("  " + filename);
            File file = new File(filename);
            In in = new In(file);
            while (!in.isEmpty()) {
                String word = in.readString();
                if (!st.contains(word)) {
                    st.put(word, new SET());
                }
                SET set = (SET)st.get(word);
                set.add(file);
            }
        }
        while (!StdIn.isEmpty()) {
            String query = StdIn.readString();
            if (!st.contains(query)) continue;
            SET set = (SET)st.get(query);
            for (File file : set) {
                StdOut.println("  " + file.getName());
            }
        }
    }
}

