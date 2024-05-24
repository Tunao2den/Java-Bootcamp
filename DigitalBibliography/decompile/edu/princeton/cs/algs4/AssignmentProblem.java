/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class AssignmentProblem {
    private static final double FLOATING_POINT_EPSILON = 1.0E-14;
    private static final int UNMATCHED = -1;
    private int n;
    private double[][] weight;
    private double minWeight;
    private double[] px;
    private double[] py;
    private int[] xy;
    private int[] yx;

    public AssignmentProblem(double[][] weight) {
        int i;
        if (weight == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        this.n = weight.length;
        this.weight = new double[this.n][this.n];
        for (i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (Double.isNaN(weight[i][j])) {
                    throw new IllegalArgumentException("weight " + i + "-" + j + " is NaN");
                }
                if (weight[i][j] < this.minWeight) {
                    this.minWeight = weight[i][j];
                }
                this.weight[i][j] = weight[i][j];
            }
        }
        this.px = new double[this.n];
        this.py = new double[this.n];
        this.xy = new int[this.n];
        this.yx = new int[this.n];
        for (i = 0; i < this.n; ++i) {
            this.xy[i] = -1;
        }
        for (int j = 0; j < this.n; ++j) {
            this.yx[j] = -1;
        }
        for (int k = 0; k < this.n; ++k) {
            assert (this.isDualFeasible());
            assert (this.isComplementarySlack());
            this.augment();
        }
        assert (this.certifySolution());
    }

    private void augment() {
        int i;
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2 * this.n + 2);
        int s = 2 * this.n;
        int t = 2 * this.n + 1;
        for (i = 0; i < this.n; ++i) {
            if (this.xy[i] != -1) continue;
            G.addEdge(new DirectedEdge(s, i, 0.0));
        }
        for (int j = 0; j < this.n; ++j) {
            if (this.yx[j] != -1) continue;
            G.addEdge(new DirectedEdge(this.n + j, t, this.py[j]));
        }
        for (i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (this.xy[i] == j) {
                    G.addEdge(new DirectedEdge(this.n + j, i, 0.0));
                    continue;
                }
                G.addEdge(new DirectedEdge(i, this.n + j, this.reducedCost(i, j)));
            }
        }
        DijkstraSP spt = new DijkstraSP(G, s);
        for (DirectedEdge e : spt.pathTo(t)) {
            int i2 = e.from();
            int j = e.to() - this.n;
            if (i2 >= this.n) continue;
            this.xy[i2] = j;
            this.yx[j] = i2;
        }
        for (int i3 = 0; i3 < this.n; ++i3) {
            int n = i3;
            this.px[n] = this.px[n] + spt.distTo(i3);
        }
        for (int j = 0; j < this.n; ++j) {
            int n = j;
            this.py[n] = this.py[n] + spt.distTo(this.n + j);
        }
    }

    private double reducedCost(int i, int j) {
        double reducedCost = this.weight[i][j] - this.minWeight + this.px[i] - this.py[j];
        double magnitude = Math.abs(this.weight[i][j]) + Math.abs(this.px[i]) + Math.abs(this.py[j]);
        if (Math.abs(reducedCost) <= 1.0E-14 * magnitude) {
            return 0.0;
        }
        assert (reducedCost >= 0.0);
        return reducedCost;
    }

    public double dualRow(int i) {
        this.validate(i);
        return this.px[i];
    }

    public double dualCol(int j) {
        this.validate(j);
        return this.py[j];
    }

    public int sol(int i) {
        this.validate(i);
        return this.xy[i];
    }

    public double weight() {
        double total = 0.0;
        for (int i = 0; i < this.n; ++i) {
            if (this.xy[i] == -1) continue;
            total += this.weight[i][this.xy[i]];
        }
        return total;
    }

    private void validate(int i) {
        if (i < 0 || i >= this.n) {
            throw new IllegalArgumentException("index is not between 0 and " + (this.n - 1) + ": " + i);
        }
    }

    private boolean isDualFeasible() {
        for (int i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                if (!(this.reducedCost(i, j) < 0.0)) continue;
                StdOut.println("Dual variables are not feasible");
                return false;
            }
        }
        return true;
    }

    private boolean isComplementarySlack() {
        for (int i = 0; i < this.n; ++i) {
            if (this.xy[i] == -1 || this.reducedCost(i, this.xy[i]) == 0.0) continue;
            StdOut.println("Primal and dual variables are not complementary slack");
            return false;
        }
        return true;
    }

    private boolean isPerfectMatching() {
        int i;
        boolean[] perm = new boolean[this.n];
        for (i = 0; i < this.n; ++i) {
            if (perm[this.xy[i]]) {
                StdOut.println("Not a perfect matching");
                return false;
            }
            perm[this.xy[i]] = true;
        }
        for (int j = 0; j < this.n; ++j) {
            if (this.xy[this.yx[j]] == j) continue;
            StdOut.println("xy[] and yx[] are not inverses");
            return false;
        }
        for (i = 0; i < this.n; ++i) {
            if (this.yx[this.xy[i]] == i) continue;
            StdOut.println("xy[] and yx[] are not inverses");
            return false;
        }
        return true;
    }

    private boolean certifySolution() {
        return this.isPerfectMatching() && this.isDualFeasible() && this.isComplementarySlack();
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        double[][] weight = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                weight[i][j] = StdRandom.uniformInt(900) + 100;
            }
        }
        AssignmentProblem assignment = new AssignmentProblem(weight);
        StdOut.printf("weight = %.0f\n", assignment.weight());
        StdOut.println();
        if (n >= 20) {
            return;
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                if (j == assignment.sol(i)) {
                    StdOut.printf("*%.0f ", weight[i][j]);
                    continue;
                }
                StdOut.printf(" %.0f ", weight[i][j]);
            }
            StdOut.println();
        }
    }
}

