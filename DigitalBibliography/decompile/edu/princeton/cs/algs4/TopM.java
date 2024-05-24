/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Transaction;

public class TopM {
    private TopM() {
    }

    public static void main(String[] args) {
        int m = Integer.parseInt(args[0]);
        MinPQ<Transaction> pq = new MinPQ<Transaction>(m + 1);
        while (StdIn.hasNextLine()) {
            String line = StdIn.readLine();
            Transaction transaction = new Transaction(line);
            pq.insert(transaction);
            if (pq.size() <= m) continue;
            pq.delMin();
        }
        Stack<Transaction> stack = new Stack<Transaction>();
        for (Transaction transaction : pq) {
            stack.push(transaction);
        }
        for (Transaction transaction : stack) {
            StdOut.println(transaction);
        }
    }
}

