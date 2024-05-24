/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

public class LinearRegression {
    private final double intercept;
    private final double slope;
    private final double r2;
    private final double svar0;
    private final double svar1;

    public LinearRegression(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        int n = x.length;
        double sumx = 0.0;
        double sumy = 0.0;
        double sumx2 = 0.0;
        for (int i = 0; i < n; ++i) {
            sumx += x[i];
            sumx2 += x[i] * x[i];
            sumy += y[i];
        }
        double xbar = sumx / (double)n;
        double ybar = sumy / (double)n;
        double xxbar = 0.0;
        double yybar = 0.0;
        double xybar = 0.0;
        for (int i = 0; i < n; ++i) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        this.slope = xybar / xxbar;
        this.intercept = ybar - this.slope * xbar;
        double rss = 0.0;
        double ssr = 0.0;
        for (int i = 0; i < n; ++i) {
            double fit = this.slope * x[i] + this.intercept;
            rss += (fit - y[i]) * (fit - y[i]);
            ssr += (fit - ybar) * (fit - ybar);
        }
        int degreesOfFreedom = n - 2;
        this.r2 = ssr / yybar;
        double svar = rss / (double)degreesOfFreedom;
        this.svar1 = svar / xxbar;
        this.svar0 = svar / (double)n + xbar * xbar * this.svar1;
    }

    public double intercept() {
        return this.intercept;
    }

    public double slope() {
        return this.slope;
    }

    public double R2() {
        return this.r2;
    }

    public double interceptStdErr() {
        return Math.sqrt(this.svar0);
    }

    public double slopeStdErr() {
        return Math.sqrt(this.svar1);
    }

    public double predict(double x) {
        return this.slope * x + this.intercept;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%.2f n + %.2f", this.slope(), this.intercept()));
        s.append("  (R^2 = " + String.format("%.3f", this.R2()) + ")");
        return s.toString();
    }
}

