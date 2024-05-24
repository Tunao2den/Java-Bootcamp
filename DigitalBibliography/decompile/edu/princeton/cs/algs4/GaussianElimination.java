/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class GaussianElimination {
    private static final double EPSILON = 1.0E-8;
    private final int m;
    private final int n;
    private double[][] a;

    public GaussianElimination(double[][] A, double[] b) {
        int i;
        this.m = A.length;
        this.n = A[0].length;
        if (b.length != this.m) {
            throw new IllegalArgumentException("Dimensions disagree");
        }
        this.a = new double[this.m][this.n + 1];
        for (i = 0; i < this.m; ++i) {
            for (int j = 0; j < this.n; ++j) {
                this.a[i][j] = A[i][j];
            }
        }
        for (i = 0; i < this.m; ++i) {
            this.a[i][this.n] = b[i];
        }
        this.forwardElimination();
        assert (this.certifySolution(A, b));
    }

    private void forwardElimination() {
        for (int p = 0; p < Math.min(this.m, this.n); ++p) {
            int max = p;
            for (int i = p + 1; i < this.m; ++i) {
                if (!(Math.abs(this.a[i][p]) > Math.abs(this.a[max][p]))) continue;
                max = i;
            }
            this.swap(p, max);
            if (Math.abs(this.a[p][p]) <= 1.0E-8) continue;
            this.pivot(p);
        }
    }

    private void swap(int row1, int row2) {
        double[] temp = this.a[row1];
        this.a[row1] = this.a[row2];
        this.a[row2] = temp;
    }

    private void pivot(int p) {
        for (int i = p + 1; i < this.m; ++i) {
            double alpha = this.a[i][p] / this.a[p][p];
            for (int j = p; j <= this.n; ++j) {
                double[] dArray = this.a[i];
                int n = j;
                dArray[n] = dArray[n] - alpha * this.a[p][j];
            }
        }
    }

    public double[] primal() {
        int j;
        double sum;
        int i;
        double[] x = new double[this.n];
        for (i = Math.min(this.n - 1, this.m - 1); i >= 0; --i) {
            sum = 0.0;
            for (j = i + 1; j < this.n; ++j) {
                sum += this.a[i][j] * x[j];
            }
            if (Math.abs(this.a[i][i]) > 1.0E-8) {
                x[i] = (this.a[i][this.n] - sum) / this.a[i][i];
                continue;
            }
            if (!(Math.abs(this.a[i][this.n] - sum) > 1.0E-8)) continue;
            return null;
        }
        for (i = this.n; i < this.m; ++i) {
            sum = 0.0;
            for (j = 0; j < this.n; ++j) {
                sum += this.a[i][j] * x[j];
            }
            if (!(Math.abs(this.a[i][this.n] - sum) > 1.0E-8)) continue;
            return null;
        }
        return x;
    }

    public boolean isFeasible() {
        return this.primal() != null;
    }

    private boolean certifySolution(double[][] A, double[] b) {
        if (!this.isFeasible()) {
            return true;
        }
        double[] x = this.primal();
        for (int i = 0; i < this.m; ++i) {
            double sum = 0.0;
            for (int j = 0; j < this.n; ++j) {
                sum += A[i][j] * x[j];
            }
            if (!(Math.abs(sum - b[i]) > 1.0E-8)) continue;
            StdOut.println("not feasible");
            StdOut.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
            return false;
        }
        return true;
    }

    private static void test(String name, double[][] A, double[] b) {
        StdOut.println("----------------------------------------------------");
        StdOut.println(name);
        StdOut.println("----------------------------------------------------");
        GaussianElimination gaussian = new GaussianElimination(A, b);
        double[] x = gaussian.primal();
        if (gaussian.isFeasible()) {
            for (int i = 0; i < x.length; ++i) {
                StdOut.printf("%.6f\n", x[i]);
            }
        } else {
            StdOut.println("System is infeasible");
        }
        StdOut.println();
        StdOut.println();
    }

    private static void test1() {
        double[][] A = new double[][]{{0.0, 1.0, 1.0}, {2.0, 4.0, -2.0}, {0.0, 3.0, 15.0}};
        double[] b = new double[]{4.0, 2.0, 36.0};
        GaussianElimination.test("test 1 (3-by-3 system, nonsingular)", A, b);
    }

    private static void test2() {
        double[][] A = new double[][]{{1.0, -3.0, 1.0}, {2.0, -8.0, 8.0}, {-6.0, 3.0, -15.0}};
        double[] b = new double[]{4.0, -2.0, 9.0};
        GaussianElimination.test("test 2 (3-by-3 system, nonsingular)", A, b);
    }

    private static void test3() {
        double[][] A = new double[][]{{2.0, -3.0, -1.0, 2.0, 3.0}, {4.0, -4.0, -1.0, 4.0, 11.0}, {2.0, -5.0, -2.0, 2.0, -1.0}, {0.0, 2.0, 1.0, 0.0, 4.0}, {-4.0, 6.0, 0.0, 0.0, 7.0}};
        double[] b = new double[]{4.0, 4.0, 9.0, -6.0, 5.0};
        GaussianElimination.test("test 3 (5-by-5 system, no solutions)", A, b);
    }

    private static void test4() {
        double[][] A = new double[][]{{2.0, -3.0, -1.0, 2.0, 3.0}, {4.0, -4.0, -1.0, 4.0, 11.0}, {2.0, -5.0, -2.0, 2.0, -1.0}, {0.0, 2.0, 1.0, 0.0, 4.0}, {-4.0, 6.0, 0.0, 0.0, 7.0}};
        double[] b = new double[]{4.0, 4.0, 9.0, -5.0, 5.0};
        GaussianElimination.test("test 4 (5-by-5 system, infinitely many solutions)", A, b);
    }

    private static void test5() {
        double[][] A = new double[][]{{2.0, -1.0, 1.0}, {3.0, 2.0, -4.0}, {-6.0, 3.0, -3.0}};
        double[] b = new double[]{1.0, 4.0, 2.0};
        GaussianElimination.test("test 5 (3-by-3 system, no solutions)", A, b);
    }

    private static void test6() {
        double[][] A = new double[][]{{1.0, -1.0, 2.0}, {4.0, 4.0, -2.0}, {-2.0, 2.0, -4.0}};
        double[] b = new double[]{-3.0, 1.0, 6.0};
        GaussianElimination.test("test 6 (3-by-3 system, infinitely many solutions)", A, b);
    }

    private static void test7() {
        double[][] A = new double[][]{{0.0, 1.0, 1.0}, {2.0, 4.0, -2.0}, {0.0, 3.0, 15.0}, {2.0, 8.0, 14.0}};
        double[] b = new double[]{4.0, 2.0, 36.0, 42.0};
        GaussianElimination.test("test 7 (4-by-3 system, full rank)", A, b);
    }

    private static void test8() {
        double[][] A = new double[][]{{0.0, 1.0, 1.0}, {2.0, 4.0, -2.0}, {0.0, 3.0, 15.0}, {2.0, 8.0, 14.0}};
        double[] b = new double[]{4.0, 2.0, 36.0, 40.0};
        GaussianElimination.test("test 8 (4-by-3 system, no solution)", A, b);
    }

    private static void test9() {
        double[][] A = new double[][]{{1.0, -3.0, 1.0, 1.0}, {2.0, -8.0, 8.0, 2.0}, {-6.0, 3.0, -15.0, 3.0}};
        double[] b = new double[]{4.0, -2.0, 9.0};
        GaussianElimination.test("test 9 (3-by-4 system, full rank)", A, b);
    }

    public static void main(String[] args) {
        GaussianElimination.test1();
        GaussianElimination.test2();
        GaussianElimination.test3();
        GaussianElimination.test4();
        GaussianElimination.test5();
        GaussianElimination.test6();
        GaussianElimination.test7();
        GaussianElimination.test8();
        GaussianElimination.test9();
        int n = Integer.parseInt(args[0]);
        double[][] A = new double[n][n];
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                A[i][j] = StdRandom.uniformInt(1000);
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; ++i) {
            b[i] = StdRandom.uniformInt(1000);
        }
        GaussianElimination.test(n + "-by-" + n + " (probably nonsingular)", A, b);
    }
}

