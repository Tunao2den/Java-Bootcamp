/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

public class Interval1D {
    public static final Comparator<Interval1D> MIN_ENDPOINT_ORDER = new MinEndpointComparator();
    public static final Comparator<Interval1D> MAX_ENDPOINT_ORDER = new MaxEndpointComparator();
    public static final Comparator<Interval1D> LENGTH_ORDER = new LengthComparator();
    private final double min;
    private final double max;

    public Interval1D(double min, double max) {
        if (Double.isInfinite(min) || Double.isInfinite(max)) {
            throw new IllegalArgumentException("Endpoints must be finite");
        }
        if (Double.isNaN(min) || Double.isNaN(max)) {
            throw new IllegalArgumentException("Endpoints cannot be NaN");
        }
        if (min == 0.0) {
            min = 0.0;
        }
        if (max == 0.0) {
            max = 0.0;
        }
        if (!(min <= max)) {
            throw new IllegalArgumentException("Illegal interval");
        }
        this.min = min;
        this.max = max;
    }

    @Deprecated
    public double left() {
        return this.min;
    }

    @Deprecated
    public double right() {
        return this.max;
    }

    public double min() {
        return this.min;
    }

    public double max() {
        return this.max;
    }

    public boolean intersects(Interval1D that) {
        if (this.max < that.min) {
            return false;
        }
        return !(that.max < this.min);
    }

    public boolean contains(double x) {
        return this.min <= x && x <= this.max;
    }

    public double length() {
        return this.max - this.min;
    }

    public String toString() {
        return "[" + this.min + ", " + this.max + "]";
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
        Interval1D that = (Interval1D)other;
        return this.min == that.min && this.max == that.max;
    }

    public int hashCode() {
        int hash1 = Double.valueOf(this.min).hashCode();
        int hash2 = Double.valueOf(this.max).hashCode();
        return 31 * hash1 + hash2;
    }

    public static void main(String[] args) {
        int i;
        Interval1D[] intervals = new Interval1D[]{new Interval1D(15.0, 33.0), new Interval1D(45.0, 60.0), new Interval1D(20.0, 70.0), new Interval1D(46.0, 55.0)};
        StdOut.println("Unsorted");
        for (i = 0; i < intervals.length; ++i) {
            StdOut.println(intervals[i]);
        }
        StdOut.println();
        StdOut.println("Sort by min endpoint");
        Arrays.sort(intervals, MIN_ENDPOINT_ORDER);
        for (i = 0; i < intervals.length; ++i) {
            StdOut.println(intervals[i]);
        }
        StdOut.println();
        StdOut.println("Sort by max endpoint");
        Arrays.sort(intervals, MAX_ENDPOINT_ORDER);
        for (i = 0; i < intervals.length; ++i) {
            StdOut.println(intervals[i]);
        }
        StdOut.println();
        StdOut.println("Sort by length");
        Arrays.sort(intervals, LENGTH_ORDER);
        for (i = 0; i < intervals.length; ++i) {
            StdOut.println(intervals[i]);
        }
        StdOut.println();
    }

    private static class MinEndpointComparator
    implements Comparator<Interval1D> {
        private MinEndpointComparator() {
        }

        @Override
        public int compare(Interval1D a, Interval1D b) {
            if (a.min < b.min) {
                return -1;
            }
            if (a.min > b.min) {
                return 1;
            }
            if (a.max < b.max) {
                return -1;
            }
            if (a.max > b.max) {
                return 1;
            }
            return 0;
        }
    }

    private static class MaxEndpointComparator
    implements Comparator<Interval1D> {
        private MaxEndpointComparator() {
        }

        @Override
        public int compare(Interval1D a, Interval1D b) {
            if (a.max < b.max) {
                return -1;
            }
            if (a.max > b.max) {
                return 1;
            }
            if (a.min < b.min) {
                return -1;
            }
            if (a.min > b.min) {
                return 1;
            }
            return 0;
        }
    }

    private static class LengthComparator
    implements Comparator<Interval1D> {
        private LengthComparator() {
        }

        @Override
        public int compare(Interval1D a, Interval1D b) {
            double blen;
            double alen = a.length();
            if (alen < (blen = b.length())) {
                return -1;
            }
            if (alen > blen) {
                return 1;
            }
            return 0;
        }
    }
}

