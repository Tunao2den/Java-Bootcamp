/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class LinearProgramming {
    private static final double EPSILON = 1.0E-10;
    private double[][] a;
    private int m;
    private int n;
    private int[] basis;

    public LinearProgramming(double[][] A, double[] b, double[] c) {
        int i;
        this.m = b.length;
        this.n = c.length;
        for (i = 0; i < this.m; ++i) {
            if (b[i] >= 0.0) continue;
            throw new IllegalArgumentException("RHS must be nonnegative");
        }
        this.a = new double[this.m + 1][this.n + this.m + 1];
        for (i = 0; i < this.m; ++i) {
            for (int j = 0; j < this.n; ++j) {
                this.a[i][j] = A[i][j];
            }
        }
        for (i = 0; i < this.m; ++i) {
            this.a[i][this.n + i] = 1.0;
        }
        for (int j = 0; j < this.n; ++j) {
            this.a[this.m][j] = c[j];
        }
        for (i = 0; i < this.m; ++i) {
            this.a[i][this.m + this.n] = b[i];
        }
        this.basis = new int[this.m];
        for (i = 0; i < this.m; ++i) {
            this.basis[i] = this.n + i;
        }
        this.solve();
        assert (this.check(A, b, c));
    }

    private void solve() {
        int q;
        while ((q = this.bland()) != -1) {
            int p = this.minRatioRule(q);
            if (p == -1) {
                throw new ArithmeticException("Linear program is unbounded");
            }
            this.pivot(p, q);
            this.basis[p] = q;
        }
    }

    private int bland() {
        for (int j = 0; j < this.m + this.n; ++j) {
            if (!(this.a[this.m][j] > 0.0)) continue;
            return j;
        }
        return -1;
    }

    private int dantzig() {
        int q = 0;
        for (int j = 1; j < this.m + this.n; ++j) {
            if (!(this.a[this.m][j] > this.a[this.m][q])) continue;
            q = j;
        }
        if (this.a[this.m][q] <= 0.0) {
            return -1;
        }
        return q;
    }

    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < this.m; ++i) {
            if (this.a[i][q] <= 1.0E-10) continue;
            if (p == -1) {
                p = i;
                continue;
            }
            if (!(this.a[i][this.m + this.n] / this.a[i][q] < this.a[p][this.m + this.n] / this.a[p][q])) continue;
            p = i;
        }
        return p;
    }

    private void pivot(int p, int q) {
        int i;
        for (i = 0; i <= this.m; ++i) {
            for (int j = 0; j <= this.m + this.n; ++j) {
                if (i == p || j == q) continue;
                double[] dArray = this.a[i];
                int n = j;
                dArray[n] = dArray[n] - this.a[p][j] * (this.a[i][q] / this.a[p][q]);
            }
        }
        for (i = 0; i <= this.m; ++i) {
            if (i == p) continue;
            this.a[i][q] = 0.0;
        }
        for (int j = 0; j <= this.m + this.n; ++j) {
            if (j == q) continue;
            double[] dArray = this.a[p];
            int n = j;
            dArray[n] = dArray[n] / this.a[p][q];
        }
        this.a[p][q] = 1.0;
    }

    public double value() {
        return -this.a[this.m][this.m + this.n];
    }

    public double[] primal() {
        double[] x = new double[this.n];
        for (int i = 0; i < this.m; ++i) {
            if (this.basis[i] >= this.n) continue;
            x[this.basis[i]] = this.a[i][this.m + this.n];
        }
        return x;
    }

    public double[] dual() {
        double[] y = new double[this.m];
        for (int i = 0; i < this.m; ++i) {
            y[i] = -this.a[this.m][this.n + i];
            if (y[i] != -0.0) continue;
            y[i] = 0.0;
        }
        return y;
    }

    private boolean isPrimalFeasible(double[][] A, double[] b) {
        double[] x = this.primal();
        for (int j = 0; j < x.length; ++j) {
            if (!(x[j] < -1.0E-10)) continue;
            StdOut.println("x[" + j + "] = " + x[j] + " is negative");
            return false;
        }
        for (int i = 0; i < this.m; ++i) {
            double sum = 0.0;
            for (int j = 0; j < this.n; ++j) {
                sum += A[i][j] * x[j];
            }
            if (!(sum > b[i] + 1.0E-10)) continue;
            StdOut.println("not primal feasible");
            StdOut.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
            return false;
        }
        return true;
    }

    private boolean isDualFeasible(double[][] A, double[] c) {
        double[] y = this.dual();
        for (int i = 0; i < y.length; ++i) {
            if (!(y[i] < -1.0E-10)) continue;
            StdOut.println("y[" + i + "] = " + y[i] + " is negative");
            return false;
        }
        for (int j = 0; j < this.n; ++j) {
            double sum = 0.0;
            for (int i = 0; i < this.m; ++i) {
                sum += A[i][j] * y[i];
            }
            if (!(sum < c[j] - 1.0E-10)) continue;
            StdOut.println("not dual feasible");
            StdOut.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
            return false;
        }
        return true;
    }

    private boolean isOptimal(double[] b, double[] c) {
        double[] x = this.primal();
        double[] y = this.dual();
        double value = this.value();
        double value1 = 0.0;
        for (int j = 0; j < x.length; ++j) {
            value1 += c[j] * x[j];
        }
        double value2 = 0.0;
        for (int i = 0; i < y.length; ++i) {
            value2 += y[i] * b[i];
        }
        if (Math.abs(value - value1) > 1.0E-10 || Math.abs(value - value2) > 1.0E-10) {
            StdOut.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
            return false;
        }
        return true;
    }

    private boolean check(double[][] A, double[] b, double[] c) {
        return this.isPrimalFeasible(A, b) && this.isDualFeasible(A, c) && this.isOptimal(b, c);
    }

    private void show() {
        int i;
        StdOut.println("m = " + this.m);
        StdOut.println("n = " + this.n);
        for (i = 0; i <= this.m; ++i) {
            for (int j = 0; j <= this.m + this.n; ++j) {
                StdOut.printf("%7.2f ", this.a[i][j]);
            }
            StdOut.println();
        }
        StdOut.println("value = " + this.value());
        for (i = 0; i < this.m; ++i) {
            if (this.basis[i] >= this.n) continue;
            StdOut.println("x_" + this.basis[i] + " = " + this.a[i][this.m + this.n]);
        }
        StdOut.println();
    }

    private static void test(double[][] A, double[] b, double[] c) {
        LinearProgramming lp;
        try {
            lp = new LinearProgramming(A, b, c);
        } catch (ArithmeticException e) {
            System.out.println(e);
            return;
        }
        StdOut.println("value = " + lp.value());
        double[] x = lp.primal();
        for (int i = 0; i < x.length; ++i) {
            StdOut.println("x[" + i + "] = " + x[i]);
        }
        double[] y = lp.dual();
        for (int j = 0; j < y.length; ++j) {
            StdOut.println("y[" + j + "] = " + y[j]);
        }
    }

    private static void test1() {
        double[][] A = new double[][]{{-1.0, 1.0, 0.0}, {1.0, 4.0, 0.0}, {2.0, 1.0, 0.0}, {3.0, -4.0, 0.0}, {0.0, 0.0, 1.0}};
        double[] c = new double[]{1.0, 1.0, 1.0};
        double[] b = new double[]{5.0, 45.0, 27.0, 24.0, 4.0};
        LinearProgramming.test(A, b, c);
    }

    private static void test2() {
        double[] c = new double[]{13.0, 23.0};
        double[] b = new double[]{480.0, 160.0, 1190.0};
        double[][] A = new double[][]{{5.0, 15.0}, {4.0, 4.0}, {35.0, 20.0}};
        LinearProgramming.test(A, b, c);
    }

    private static void test3() {
        double[] c = new double[]{2.0, 3.0, -1.0, -12.0};
        double[] b = new double[]{3.0, 2.0};
        double[][] A = new double[][]{{-2.0, -9.0, 1.0, 9.0}, {1.0, 1.0, -1.0, -2.0}};
        LinearProgramming.test(A, b, c);
    }

    private static void test4() {
        double[] c = new double[]{10.0, -57.0, -9.0, -24.0};
        double[] b = new double[]{0.0, 0.0, 1.0};
        double[][] A = new double[][]{{0.5, -5.5, -2.5, 9.0}, {0.5, -1.5, -0.5, 1.0}, {1.0, 0.0, 0.0, 0.0}};
        LinearProgramming.test(A, b, c);
    }

    public static void main(String[] args) {
        int i;
        StdOut.println("----- test 1 --------------------");
        LinearProgramming.test1();
        StdOut.println();
        StdOut.println("----- test 2 --------------------");
        LinearProgramming.test2();
        StdOut.println();
        StdOut.println("----- test 3 --------------------");
        LinearProgramming.test3();
        StdOut.println();
        StdOut.println("----- test 4 --------------------");
        LinearProgramming.test4();
        StdOut.println();
        StdOut.println("----- test random ---------------");
        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        double[] c = new double[n];
        double[] b = new double[m];
        double[][] A = new double[m][n];
        for (int j = 0; j < n; ++j) {
            c[j] = StdRandom.uniformInt(1000);
        }
        for (i = 0; i < m; ++i) {
            b[i] = StdRandom.uniformInt(1000);
        }
        for (i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                A[i][j] = StdRandom.uniformInt(100);
            }
        }
        LinearProgramming lp = new LinearProgramming(A, b, c);
        LinearProgramming.test(A, b, c);
    }
}

