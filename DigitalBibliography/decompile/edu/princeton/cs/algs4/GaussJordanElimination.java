/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class GaussJordanElimination {
    private static final double EPSILON = 1.0E-8;
    private final int n;
    private double[][] a;

    public GaussJordanElimination(double[][] A, double[] b) {
        int i;
        this.n = b.length;
        this.a = new double[this.n][this.n + this.n + 1];
        for (i = 0; i < this.n; ++i) {
            for (int j = 0; j < this.n; ++j) {
                this.a[i][j] = A[i][j];
            }
        }
        for (i = 0; i < this.n; ++i) {
            this.a[i][this.n + i] = 1.0;
        }
        for (i = 0; i < this.n; ++i) {
            this.a[i][this.n + this.n] = b[i];
        }
        this.solve();
        assert (this.certifySolution(A, b));
    }

    private void solve() {
        for (int p = 0; p < this.n; ++p) {
            int max = p;
            for (int i = p + 1; i < this.n; ++i) {
                if (!(Math.abs(this.a[i][p]) > Math.abs(this.a[max][p]))) continue;
                max = i;
            }
            this.swap(p, max);
            if (Math.abs(this.a[p][p]) <= 1.0E-8) continue;
            this.pivot(p, p);
        }
    }

    private void swap(int row1, int row2) {
        double[] temp = this.a[row1];
        this.a[row1] = this.a[row2];
        this.a[row2] = temp;
    }

    private void pivot(int p, int q) {
        int i;
        for (i = 0; i < this.n; ++i) {
            double alpha = this.a[i][q] / this.a[p][q];
            for (int j = 0; j <= this.n + this.n; ++j) {
                if (i == p || j == q) continue;
                double[] dArray = this.a[i];
                int n = j;
                dArray[n] = dArray[n] - alpha * this.a[p][j];
            }
        }
        for (i = 0; i < this.n; ++i) {
            if (i == p) continue;
            this.a[i][q] = 0.0;
        }
        for (int j = 0; j <= this.n + this.n; ++j) {
            if (j == q) continue;
            double[] dArray = this.a[p];
            int n = j;
            dArray[n] = dArray[n] / this.a[p][q];
        }
        this.a[p][q] = 1.0;
    }

    public double[] primal() {
        double[] x = new double[this.n];
        for (int i = 0; i < this.n; ++i) {
            if (Math.abs(this.a[i][i]) > 1.0E-8) {
                x[i] = this.a[i][this.n + this.n] / this.a[i][i];
                continue;
            }
            if (!(Math.abs(this.a[i][this.n + this.n]) > 1.0E-8)) continue;
            return null;
        }
        return x;
    }

    public double[] dual() {
        double[] y = new double[this.n];
        for (int i = 0; i < this.n; ++i) {
            if (!(Math.abs(this.a[i][i]) <= 1.0E-8) || !(Math.abs(this.a[i][this.n + this.n]) > 1.0E-8)) continue;
            for (int j = 0; j < this.n; ++j) {
                y[j] = this.a[i][this.n + j];
            }
            return y;
        }
        return null;
    }

    public boolean isFeasible() {
        return this.primal() != null;
    }

    private void show() {
        for (int i = 0; i < this.n; ++i) {
            int j;
            for (j = 0; j < this.n; ++j) {
                StdOut.printf("%8.3f ", this.a[i][j]);
            }
            StdOut.printf("| ", new Object[0]);
            for (j = this.n; j < this.n + this.n; ++j) {
                StdOut.printf("%8.3f ", this.a[i][j]);
            }
            StdOut.printf("| %8.3f\n", this.a[i][this.n + this.n]);
        }
        StdOut.println();
    }

    private boolean certifySolution(double[][] A, double[] b) {
        if (this.isFeasible()) {
            double[] x = this.primal();
            for (int i = 0; i < this.n; ++i) {
                double sum = 0.0;
                for (int j = 0; j < this.n; ++j) {
                    sum += A[i][j] * x[j];
                }
                if (!(Math.abs(sum - b[i]) > 1.0E-8)) continue;
                StdOut.println("not feasible");
                StdOut.printf("b[%d] = %8.3f, sum = %8.3f\n", i, b[i], sum);
                return false;
            }
            return true;
        }
        double[] y = this.dual();
        for (int j = 0; j < this.n; ++j) {
            double sum = 0.0;
            for (int i = 0; i < this.n; ++i) {
                sum += A[i][j] * y[i];
            }
            if (!(Math.abs(sum) > 1.0E-8)) continue;
            StdOut.println("invalid certificate of infeasibility");
            StdOut.printf("sum = %8.3f\n", sum);
            return false;
        }
        double sum = 0.0;
        for (int i = 0; i < this.n; ++i) {
            sum += y[i] * b[i];
        }
        if (Math.abs(sum) < 1.0E-8) {
            StdOut.println("invalid certificate of infeasibility");
            StdOut.printf("yb  = %8.3f\n", sum);
            return false;
        }
        return true;
    }

    private static void test(String name, double[][] A, double[] b) {
        StdOut.println("----------------------------------------------------");
        StdOut.println(name);
        StdOut.println("----------------------------------------------------");
        GaussJordanElimination gaussian = new GaussJordanElimination(A, b);
        if (gaussian.isFeasible()) {
            StdOut.println("Solution to Ax = b");
            double[] x = gaussian.primal();
            for (int i = 0; i < x.length; ++i) {
                StdOut.printf("%10.6f\n", x[i]);
            }
        } else {
            StdOut.println("Certificate of infeasibility");
            double[] y = gaussian.dual();
            for (int j = 0; j < y.length; ++j) {
                StdOut.printf("%10.6f\n", y[j]);
            }
        }
        StdOut.println();
        StdOut.println();
    }

    private static void test1() {
        double[][] A = new double[][]{{0.0, 1.0, 1.0}, {2.0, 4.0, -2.0}, {0.0, 3.0, 15.0}};
        double[] b = new double[]{4.0, 2.0, 36.0};
        GaussJordanElimination.test("test 1", A, b);
    }

    private static void test2() {
        double[][] A = new double[][]{{1.0, -3.0, 1.0}, {2.0, -8.0, 8.0}, {-6.0, 3.0, -15.0}};
        double[] b = new double[]{4.0, -2.0, 9.0};
        GaussJordanElimination.test("test 2", A, b);
    }

    private static void test3() {
        double[][] A = new double[][]{{2.0, -3.0, -1.0, 2.0, 3.0}, {4.0, -4.0, -1.0, 4.0, 11.0}, {2.0, -5.0, -2.0, 2.0, -1.0}, {0.0, 2.0, 1.0, 0.0, 4.0}, {-4.0, 6.0, 0.0, 0.0, 7.0}};
        double[] b = new double[]{4.0, 4.0, 9.0, -6.0, 5.0};
        GaussJordanElimination.test("test 3", A, b);
    }

    private static void test4() {
        double[][] A = new double[][]{{2.0, -3.0, -1.0, 2.0, 3.0}, {4.0, -4.0, -1.0, 4.0, 11.0}, {2.0, -5.0, -2.0, 2.0, -1.0}, {0.0, 2.0, 1.0, 0.0, 4.0}, {-4.0, 6.0, 0.0, 0.0, 7.0}};
        double[] b = new double[]{4.0, 4.0, 9.0, -5.0, 5.0};
        GaussJordanElimination.test("test 4", A, b);
    }

    private static void test5() {
        double[][] A = new double[][]{{2.0, -1.0, 1.0}, {3.0, 2.0, -4.0}, {-6.0, 3.0, -3.0}};
        double[] b = new double[]{1.0, 4.0, 2.0};
        GaussJordanElimination.test("test 5", A, b);
    }

    private static void test6() {
        double[][] A = new double[][]{{1.0, -1.0, 2.0}, {4.0, 4.0, -2.0}, {-2.0, 2.0, -4.0}};
        double[] b = new double[]{-3.0, 1.0, 6.0};
        GaussJordanElimination.test("test 6 (infinitely many solutions)", A, b);
    }

    public static void main(String[] args) {
        int i;
        GaussJordanElimination.test1();
        GaussJordanElimination.test2();
        GaussJordanElimination.test3();
        GaussJordanElimination.test4();
        GaussJordanElimination.test5();
        GaussJordanElimination.test6();
        int n = Integer.parseInt(args[0]);
        double[][] A = new double[n][n];
        for (int i2 = 0; i2 < n; ++i2) {
            for (int j = 0; j < n; ++j) {
                A[i2][j] = StdRandom.uniformInt(1000);
            }
        }
        double[] b = new double[n];
        for (i = 0; i < n; ++i) {
            b[i] = StdRandom.uniformInt(1000);
        }
        GaussJordanElimination.test("random " + n + "-by-" + n + " (likely full rank)", A, b);
        A = new double[n][n];
        for (i = 0; i < n - 1; ++i) {
            for (int j = 0; j < n; ++j) {
                A[i][j] = StdRandom.uniformInt(1000);
            }
        }
        for (i = 0; i < n - 1; ++i) {
            double alpha = StdRandom.uniformInt(-5, 5);
            for (int j = 0; j < n; ++j) {
                double[] dArray = A[n - 1];
                int n2 = j;
                dArray[n2] = dArray[n2] + alpha * A[i][j];
            }
        }
        b = new double[n];
        for (i = 0; i < n; ++i) {
            b[i] = StdRandom.uniformInt(1000);
        }
        GaussJordanElimination.test("random " + n + "-by-" + n + " (likely infeasible)", A, b);
    }
}

