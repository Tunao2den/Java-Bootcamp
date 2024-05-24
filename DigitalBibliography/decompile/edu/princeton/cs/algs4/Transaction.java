/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Date;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

public class Transaction
implements Comparable<Transaction> {
    private final String who;
    private final Date when;
    private final double amount;

    public Transaction(String who, Date when, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
        }
        this.who = who;
        this.when = when;
        this.amount = amount;
    }

    public Transaction(String transaction) {
        String[] a = transaction.split("\\s+");
        this.who = a[0];
        this.when = new Date(a[1]);
        this.amount = Double.parseDouble(a[2]);
        if (Double.isNaN(this.amount) || Double.isInfinite(this.amount)) {
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
        }
    }

    public String who() {
        return this.who;
    }

    public Date when() {
        return this.when;
    }

    public double amount() {
        return this.amount;
    }

    public String toString() {
        return String.format("%-10s %10s %8.2f", this.who, this.when, this.amount);
    }

    @Override
    public int compareTo(Transaction that) {
        return Double.compare(this.amount, that.amount);
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Transaction that = (Transaction)other;
        return this.amount == that.amount && this.who.equals(that.who) && this.when.equals(that.when);
    }

    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + this.who.hashCode();
        hash = 31 * hash + this.when.hashCode();
        hash = 31 * hash + Double.valueOf(this.amount).hashCode();
        return hash;
    }

    public static void main(String[] args) {
        int i;
        Transaction[] a = new Transaction[]{new Transaction("Turing   6/17/1990  644.08"), new Transaction("Tarjan   3/26/2002 4121.85"), new Transaction("Knuth    6/14/1999  288.34"), new Transaction("Dijkstra 8/22/2007 2678.40")};
        StdOut.println("Unsorted");
        for (i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
        StdOut.println();
        StdOut.println("Sort by date");
        Arrays.sort(a, new WhenOrder());
        for (i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
        StdOut.println();
        StdOut.println("Sort by customer");
        Arrays.sort(a, new WhoOrder());
        for (i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
        StdOut.println();
        StdOut.println("Sort by amount");
        Arrays.sort(a, new HowMuchOrder());
        for (i = 0; i < a.length; ++i) {
            StdOut.println(a[i]);
        }
        StdOut.println();
    }

    public static class WhenOrder
    implements Comparator<Transaction> {
        @Override
        public int compare(Transaction v, Transaction w) {
            return v.when.compareTo(w.when);
        }
    }

    public static class WhoOrder
    implements Comparator<Transaction> {
        @Override
        public int compare(Transaction v, Transaction w) {
            return v.who.compareTo(w.who);
        }
    }

    public static class HowMuchOrder
    implements Comparator<Transaction> {
        @Override
        public int compare(Transaction v, Transaction w) {
            return Double.compare(v.amount, w.amount);
        }
    }
}

