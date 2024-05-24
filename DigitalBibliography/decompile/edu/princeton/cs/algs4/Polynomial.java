/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;

public class Polynomial {
    private int[] coef;
    private int degree;

    public Polynomial(int a, int b) {
        if (b < 0) {
            throw new IllegalArgumentException("exponent cannot be negative: " + b);
        }
        this.coef = new int[b + 1];
        this.coef[b] = a;
        this.reduce();
    }

    private void reduce() {
        this.degree = -1;
        for (int i = this.coef.length - 1; i >= 0; --i) {
            if (this.coef[i] == 0) continue;
            this.degree = i;
            return;
        }
    }

    public int degree() {
        return this.degree;
    }

    public Polynomial plus(Polynomial that) {
        int i;
        Polynomial poly = new Polynomial(0, Math.max(this.degree, that.degree));
        for (i = 0; i <= this.degree; ++i) {
            int n = i;
            poly.coef[n] = poly.coef[n] + this.coef[i];
        }
        for (i = 0; i <= that.degree; ++i) {
            int n = i;
            poly.coef[n] = poly.coef[n] + that.coef[i];
        }
        poly.reduce();
        return poly;
    }

    public Polynomial minus(Polynomial that) {
        int i;
        Polynomial poly = new Polynomial(0, Math.max(this.degree, that.degree));
        for (i = 0; i <= this.degree; ++i) {
            int n = i;
            poly.coef[n] = poly.coef[n] + this.coef[i];
        }
        for (i = 0; i <= that.degree; ++i) {
            int n = i;
            poly.coef[n] = poly.coef[n] - that.coef[i];
        }
        poly.reduce();
        return poly;
    }

    public Polynomial times(Polynomial that) {
        Polynomial poly = new Polynomial(0, this.degree + that.degree);
        for (int i = 0; i <= this.degree; ++i) {
            for (int j = 0; j <= that.degree; ++j) {
                int n = i + j;
                poly.coef[n] = poly.coef[n] + this.coef[i] * that.coef[j];
            }
        }
        poly.reduce();
        return poly;
    }

    public Polynomial compose(Polynomial that) {
        Polynomial poly = new Polynomial(0, 0);
        for (int i = this.degree; i >= 0; --i) {
            Polynomial term = new Polynomial(this.coef[i], 0);
            poly = term.plus(that.times(poly));
        }
        return poly;
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
        Polynomial that = (Polynomial)other;
        if (this.degree != that.degree) {
            return false;
        }
        for (int i = this.degree; i >= 0; --i) {
            if (this.coef[i] == that.coef[i]) continue;
            return false;
        }
        return true;
    }

    public Polynomial differentiate() {
        if (this.degree == 0) {
            return new Polynomial(0, 0);
        }
        Polynomial poly = new Polynomial(0, this.degree - 1);
        poly.degree = this.degree - 1;
        for (int i = 0; i < this.degree; ++i) {
            poly.coef[i] = (i + 1) * this.coef[i + 1];
        }
        return poly;
    }

    public int evaluate(int x) {
        int p = 0;
        for (int i = this.degree; i >= 0; --i) {
            p = this.coef[i] + x * p;
        }
        return p;
    }

    public int compareTo(Polynomial that) {
        if (this.degree < that.degree) {
            return -1;
        }
        if (this.degree > that.degree) {
            return 1;
        }
        for (int i = this.degree; i >= 0; --i) {
            if (this.coef[i] < that.coef[i]) {
                return -1;
            }
            if (this.coef[i] <= that.coef[i]) continue;
            return 1;
        }
        return 0;
    }

    public String toString() {
        if (this.degree == -1) {
            return "0";
        }
        if (this.degree == 0) {
            return "" + this.coef[0];
        }
        if (this.degree == 1) {
            return this.coef[1] + "x + " + this.coef[0];
        }
        String s = this.coef[this.degree] + "x^" + this.degree;
        for (int i = this.degree - 1; i >= 0; --i) {
            if (this.coef[i] == 0) continue;
            if (this.coef[i] > 0) {
                s = s + " + " + this.coef[i];
            } else if (this.coef[i] < 0) {
                s = s + " - " + -this.coef[i];
            }
            if (i == 1) {
                s = s + "x";
                continue;
            }
            if (i <= 1) continue;
            s = s + "x^" + i;
        }
        return s;
    }

    public static void main(String[] args) {
        Polynomial zero = new Polynomial(0, 0);
        Polynomial p1 = new Polynomial(4, 3);
        Polynomial p2 = new Polynomial(3, 2);
        Polynomial p3 = new Polynomial(1, 0);
        Polynomial p4 = new Polynomial(2, 1);
        Polynomial p = p1.plus(p2).plus(p3).plus(p4);
        Polynomial q1 = new Polynomial(3, 2);
        Polynomial q2 = new Polynomial(5, 0);
        Polynomial q = q1.plus(q2);
        Polynomial r = p.plus(q);
        Polynomial s = p.times(q);
        Polynomial t = p.compose(q);
        Polynomial u = p.minus(p);
        StdOut.println("zero(x)     = " + zero);
        StdOut.println("p(x)        = " + p);
        StdOut.println("q(x)        = " + q);
        StdOut.println("p(x) + q(x) = " + r);
        StdOut.println("p(x) * q(x) = " + s);
        StdOut.println("p(q(x))     = " + t);
        StdOut.println("p(x) - p(x) = " + u);
        StdOut.println("0 - p(x)    = " + zero.minus(p));
        StdOut.println("p(3)        = " + p.evaluate(3));
        StdOut.println("p'(x)       = " + p.differentiate());
        StdOut.println("p''(x)      = " + p.differentiate().differentiate());
    }
}

