/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;

public class Vector {
    private int d;
    private double[] data;

    public Vector(int d) {
        this.d = d;
        this.data = new double[d];
    }

    public Vector(double ... a) {
        this.d = a.length;
        this.data = new double[this.d];
        for (int i = 0; i < this.d; ++i) {
            this.data[i] = a[i];
        }
    }

    @Deprecated
    public int length() {
        return this.d;
    }

    public int dimension() {
        return this.d;
    }

    public double dot(Vector that) {
        if (this.d != that.d) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }
        double sum = 0.0;
        for (int i = 0; i < this.d; ++i) {
            sum += this.data[i] * that.data[i];
        }
        return sum;
    }

    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    public double distanceTo(Vector that) {
        if (this.d != that.d) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }
        return this.minus(that).magnitude();
    }

    public Vector plus(Vector that) {
        if (this.d != that.d) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }
        Vector c = new Vector(this.d);
        for (int i = 0; i < this.d; ++i) {
            c.data[i] = this.data[i] + that.data[i];
        }
        return c;
    }

    public Vector minus(Vector that) {
        if (this.d != that.d) {
            throw new IllegalArgumentException("Dimensions don't agree");
        }
        Vector c = new Vector(this.d);
        for (int i = 0; i < this.d; ++i) {
            c.data[i] = this.data[i] - that.data[i];
        }
        return c;
    }

    public double cartesian(int i) {
        return this.data[i];
    }

    @Deprecated
    public Vector times(double alpha) {
        Vector c = new Vector(this.d);
        for (int i = 0; i < this.d; ++i) {
            c.data[i] = alpha * this.data[i];
        }
        return c;
    }

    public Vector scale(double alpha) {
        Vector c = new Vector(this.d);
        for (int i = 0; i < this.d; ++i) {
            c.data[i] = alpha * this.data[i];
        }
        return c;
    }

    public Vector direction() {
        if (this.magnitude() == 0.0) {
            throw new ArithmeticException("Zero-vector has no direction");
        }
        return this.times(1.0 / this.magnitude());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.d; ++i) {
            s.append(this.data[i] + " ");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        double[] xdata = new double[]{1.0, 2.0, 3.0, 4.0};
        double[] ydata = new double[]{5.0, 2.0, 4.0, 1.0};
        Vector x = new Vector(xdata);
        Vector y = new Vector(ydata);
        StdOut.println("   x       = " + x);
        StdOut.println("   y       = " + y);
        Vector z = x.plus(y);
        StdOut.println("   z       = " + z);
        z = z.times(10.0);
        StdOut.println(" 10z       = " + z);
        StdOut.println("  |x|      = " + x.magnitude());
        StdOut.println(" <x, y>    = " + x.dot(y));
        StdOut.println("dist(x, y) = " + x.distanceTo(y));
        StdOut.println("dir(x)     = " + x.direction());
    }
}

