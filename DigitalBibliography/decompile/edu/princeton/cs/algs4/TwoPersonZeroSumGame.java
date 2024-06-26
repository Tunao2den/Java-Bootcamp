/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.LinearProgramming;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class TwoPersonZeroSumGame {
    private static final double EPSILON = 1.0E-8;
    private final int m;
    private final int n;
    private LinearProgramming lp;
    private double constant;

    public TwoPersonZeroSumGame(double[][] payoff) {
        int j;
        int i;
        this.m = payoff.length;
        this.n = payoff[0].length;
        double[] c = new double[this.n];
        double[] b = new double[this.m];
        double[][] A = new double[this.m][this.n];
        for (i = 0; i < this.m; ++i) {
            b[i] = 1.0;
        }
        for (int j2 = 0; j2 < this.n; ++j2) {
            c[j2] = 1.0;
        }
        this.constant = Double.POSITIVE_INFINITY;
        for (i = 0; i < this.m; ++i) {
            for (j = 0; j < this.n; ++j) {
                if (!(payoff[i][j] < this.constant)) continue;
                this.constant = payoff[i][j];
            }
        }
        this.constant = this.constant <= 0.0 ? -this.constant + 1.0 : 0.0;
        for (i = 0; i < this.m; ++i) {
            for (j = 0; j < this.n; ++j) {
                A[i][j] = payoff[i][j] + this.constant;
            }
        }
        this.lp = new LinearProgramming(A, b, c);
        assert (this.certifySolution(payoff));
    }

    public double value() {
        return 1.0 / this.scale() - this.constant;
    }

    private double scale() {
        double[] x = this.lp.primal();
        double sum = 0.0;
        for (int j = 0; j < this.n; ++j) {
            sum += x[j];
        }
        return sum;
    }

    public double[] row() {
        double scale = this.scale();
        double[] x = this.lp.primal();
        int j = 0;
        while (j < this.n) {
            int n = j++;
            x[n] = x[n] / scale;
        }
        return x;
    }

    public double[] column() {
        double scale = this.scale();
        double[] y = this.lp.dual();
        int i = 0;
        while (i < this.m) {
            int n = i++;
            y[n] = y[n] / scale;
        }
        return y;
    }

    private boolean isPrimalFeasible() {
        double[] x = this.row();
        double sum = 0.0;
        for (int j = 0; j < this.n; ++j) {
            if (x[j] < 0.0) {
                StdOut.println("row vector not a probability distribution");
                StdOut.printf("    x[%d] = %f\n", j, x[j]);
                return false;
            }
            sum += x[j];
        }
        if (Math.abs(sum - 1.0) > 1.0E-8) {
            StdOut.println("row vector x[] is not a probability distribution");
            StdOut.println("    sum = " + sum);
            return false;
        }
        return true;
    }

    private boolean isDualFeasible() {
        double[] y = this.column();
        double sum = 0.0;
        for (int i = 0; i < this.m; ++i) {
            if (y[i] < 0.0) {
                StdOut.println("column vector y[] is not a probability distribution");
                StdOut.printf("    y[%d] = %f\n", i, y[i]);
                return false;
            }
            sum += y[i];
        }
        if (Math.abs(sum - 1.0) > 1.0E-8) {
            StdOut.println("column vector not a probability distribution");
            StdOut.println("    sum = " + sum);
            return false;
        }
        return true;
    }

    private boolean isNashEquilibrium(double[][] payoff) {
        double[] x = this.row();
        double[] y = this.column();
        double value = this.value();
        double opt1 = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < this.m; ++i) {
            double sum = 0.0;
            for (int j = 0; j < this.n; ++j) {
                sum += payoff[i][j] * x[j];
            }
            if (!(sum > opt1)) continue;
            opt1 = sum;
        }
        if (Math.abs(opt1 - value) > 1.0E-8) {
            StdOut.println("Optimal value = " + value);
            StdOut.println("Optimal best response for column player = " + opt1);
            return false;
        }
        double opt2 = Double.POSITIVE_INFINITY;
        for (int j = 0; j < this.n; ++j) {
            double sum = 0.0;
            for (int i = 0; i < this.m; ++i) {
                sum += payoff[i][j] * y[i];
            }
            if (!(sum < opt2)) continue;
            opt2 = sum;
        }
        if (Math.abs(opt2 - value) > 1.0E-8) {
            StdOut.println("Optimal value = " + value);
            StdOut.println("Optimal best response for row player = " + opt2);
            return false;
        }
        return true;
    }

    private boolean certifySolution(double[][] payoff) {
        return this.isPrimalFeasible() && this.isDualFeasible() && this.isNashEquilibrium(payoff);
    }

    private static void test(String description, double[][] payoff) {
        StdOut.println();
        StdOut.println(description);
        StdOut.println("------------------------------------");
        int m = payoff.length;
        int n = payoff[0].length;
        TwoPersonZeroSumGame zerosum = new TwoPersonZeroSumGame(payoff);
        double[] x = zerosum.row();
        double[] y = zerosum.column();
        StdOut.print("x[] = [");
        for (int j = 0; j < n - 1; ++j) {
            StdOut.printf("%8.4f, ", x[j]);
        }
        StdOut.printf("%8.4f]\n", x[n - 1]);
        StdOut.print("y[] = [");
        for (int i = 0; i < m - 1; ++i) {
            StdOut.printf("%8.4f, ", y[i]);
        }
        StdOut.printf("%8.4f]\n", y[m - 1]);
        StdOut.println("value =  " + zerosum.value());
    }

    private static void test1() {
        double[][] payoff = new double[][]{{30.0, -10.0, 20.0}, {10.0, 20.0, -20.0}};
        TwoPersonZeroSumGame.test("wikipedia", payoff);
    }

    private static void test2() {
        double[][] payoff = new double[][]{{0.0, 2.0, -3.0, 0.0}, {-2.0, 0.0, 0.0, 3.0}, {3.0, 0.0, 0.0, -4.0}, {0.0, -3.0, 4.0, 0.0}};
        TwoPersonZeroSumGame.test("Chvatal, p. 230", payoff);
    }

    private static void test3() {
        double[][] payoff = new double[][]{{0.0, 2.0, -3.0, 0.0}, {-2.0, 0.0, 0.0, 3.0}, {3.0, 0.0, 0.0, -4.0}, {0.0, -3.0, 4.0, 0.0}, {0.0, 0.0, -3.0, 3.0}, {-2.0, 2.0, 0.0, 0.0}, {3.0, -3.0, 0.0, 0.0}, {0.0, 0.0, 4.0, -4.0}};
        TwoPersonZeroSumGame.test("Chvatal, p. 234", payoff);
    }

    private static void test4() {
        double[][] payoff = new double[][]{{0.0, 2.0, -1.0, -1.0}, {0.0, 1.0, -2.0, -1.0}, {-1.0, -1.0, 1.0, 1.0}, {-1.0, 0.0, 0.0, 1.0}, {1.0, -2.0, 0.0, -3.0}, {1.0, -1.0, -1.0, -3.0}, {0.0, -3.0, 2.0, -1.0}, {0.0, -2.0, 1.0, -1.0}};
        TwoPersonZeroSumGame.test("Chvatal p. 236", payoff);
    }

    private static void test5() {
        double[][] payoff = new double[][]{{0.0, -1.0, 1.0}, {1.0, 0.0, -1.0}, {-1.0, 1.0, 0.0}};
        TwoPersonZeroSumGame.test("rock, paper, scisssors", payoff);
    }

    public static void main(String[] args) {
        TwoPersonZeroSumGame.test1();
        TwoPersonZeroSumGame.test2();
        TwoPersonZeroSumGame.test3();
        TwoPersonZeroSumGame.test4();
        TwoPersonZeroSumGame.test5();
        int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        double[][] payoff = new double[m][n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                payoff[i][j] = StdRandom.uniformDouble(-0.5, 0.5);
            }
        }
        TwoPersonZeroSumGame.test("random " + m + "-by-" + n, payoff);
    }
}

