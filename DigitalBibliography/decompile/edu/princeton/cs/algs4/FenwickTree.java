/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FenwickTree {
    int[] array;

    public FenwickTree(int size) {
        this.array = new int[size + 1];
    }

    public int rsq(int ind) {
        assert (ind > 0);
        int sum = 0;
        while (ind > 0) {
            sum += this.array[ind];
            ind -= ind & -ind;
        }
        return sum;
    }

    public int rsq(int a, int b) {
        assert (b >= a && a > 0 && b > 0);
        return this.rsq(b) - this.rsq(a - 1);
    }

    public void update(int ind, int value) {
        assert (ind > 0);
        while (ind < this.array.length) {
            int n = ind;
            this.array[n] = this.array[n] + value;
            ind += ind & -ind;
        }
    }

    public int size() {
        return this.array.length - 1;
    }

    public static void main(String[] args) {
        String[] line;
        FenwickTree ft = null;
        String cmd = "cmp";
        while (!(line = StdIn.readLine().split(" "))[0].equals("exit")) {
            int i;
            int arg1 = 0;
            int arg2 = 0;
            if (line.length > 1) {
                arg1 = Integer.parseInt(line[1]);
            }
            if (line.length > 2) {
                arg2 = Integer.parseInt(line[2]);
            }
            if (!line[0].equals("set") && !line[0].equals("init") && ft == null) {
                StdOut.println("FenwickTree not initialized");
                continue;
            }
            if (line[0].equals("init")) {
                ft = new FenwickTree(arg1);
                for (i = 1; i <= ft.size(); ++i) {
                    StdOut.print(ft.rsq(i, i) + " ");
                }
                StdOut.println();
                continue;
            }
            if (line[0].equals("set")) {
                ft = new FenwickTree(line.length - 1);
                for (i = 1; i <= line.length - 1; ++i) {
                    ft.update(i, Integer.parseInt(line[i]));
                }
                continue;
            }
            if (line[0].equals("up")) {
                ft.update(arg1, arg2);
                for (i = 1; i <= ft.size(); ++i) {
                    StdOut.print(ft.rsq(i, i) + " ");
                }
                StdOut.println();
                continue;
            }
            if (line[0].equals("rsq")) {
                StdOut.printf("Sum from %d to %d = %d%n", arg1, arg2, ft.rsq(arg1, arg2));
                continue;
            }
            StdOut.println("Invalid command");
        }
    }
}

