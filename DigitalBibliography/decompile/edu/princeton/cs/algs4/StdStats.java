/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdArrayIO;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public final class StdStats {
    private StdStats() {
    }

    public static double max(double[] a) {
        StdStats.validateNotNull(a);
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < a.length; ++i) {
            if (Double.isNaN(a[i])) {
                return Double.NaN;
            }
            if (!(a[i] > max)) continue;
            max = a[i];
        }
        return max;
    }

    public static double max(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        double max = Double.NEGATIVE_INFINITY;
        for (int i = lo; i < hi; ++i) {
            if (Double.isNaN(a[i])) {
                return Double.NaN;
            }
            if (!(a[i] > max)) continue;
            max = a[i];
        }
        return max;
    }

    public static int max(int[] a) {
        StdStats.validateNotNull(a);
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] <= max) continue;
            max = a[i];
        }
        return max;
    }

    public static double min(double[] a) {
        StdStats.validateNotNull(a);
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < a.length; ++i) {
            if (Double.isNaN(a[i])) {
                return Double.NaN;
            }
            if (!(a[i] < min)) continue;
            min = a[i];
        }
        return min;
    }

    public static double min(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        double min = Double.POSITIVE_INFINITY;
        for (int i = lo; i < hi; ++i) {
            if (Double.isNaN(a[i])) {
                return Double.NaN;
            }
            if (!(a[i] < min)) continue;
            min = a[i];
        }
        return min;
    }

    public static int min(int[] a) {
        StdStats.validateNotNull(a);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] >= min) continue;
            min = a[i];
        }
        return min;
    }

    public static double mean(double[] a) {
        StdStats.validateNotNull(a);
        if (a.length == 0) {
            return Double.NaN;
        }
        double sum = StdStats.sum(a);
        return sum / (double)a.length;
    }

    public static double mean(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        int length = hi - lo;
        if (length == 0) {
            return Double.NaN;
        }
        double sum = StdStats.sum(a, lo, hi);
        return sum / (double)length;
    }

    public static double mean(int[] a) {
        StdStats.validateNotNull(a);
        if (a.length == 0) {
            return Double.NaN;
        }
        int sum = StdStats.sum(a);
        return 1.0 * (double)sum / (double)a.length;
    }

    public static double var(double[] a) {
        StdStats.validateNotNull(a);
        if (a.length == 0) {
            return Double.NaN;
        }
        double avg = StdStats.mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; ++i) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (double)(a.length - 1);
    }

    public static double var(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        int length = hi - lo;
        if (length == 0) {
            return Double.NaN;
        }
        double avg = StdStats.mean(a, lo, hi);
        double sum = 0.0;
        for (int i = lo; i < hi; ++i) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (double)(length - 1);
    }

    public static double var(int[] a) {
        StdStats.validateNotNull(a);
        if (a.length == 0) {
            return Double.NaN;
        }
        double avg = StdStats.mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; ++i) {
            sum += ((double)a[i] - avg) * ((double)a[i] - avg);
        }
        return sum / (double)(a.length - 1);
    }

    public static double varp(double[] a) {
        StdStats.validateNotNull(a);
        if (a.length == 0) {
            return Double.NaN;
        }
        double avg = StdStats.mean(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; ++i) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (double)a.length;
    }

    public static double varp(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        int length = hi - lo;
        if (length == 0) {
            return Double.NaN;
        }
        double avg = StdStats.mean(a, lo, hi);
        double sum = 0.0;
        for (int i = lo; i < hi; ++i) {
            sum += (a[i] - avg) * (a[i] - avg);
        }
        return sum / (double)length;
    }

    public static double stddev(double[] a) {
        StdStats.validateNotNull(a);
        return Math.sqrt(StdStats.var(a));
    }

    public static double stddev(int[] a) {
        StdStats.validateNotNull(a);
        return Math.sqrt(StdStats.var(a));
    }

    public static double stddev(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        return Math.sqrt(StdStats.var(a, lo, hi));
    }

    public static double stddevp(double[] a) {
        StdStats.validateNotNull(a);
        return Math.sqrt(StdStats.varp(a));
    }

    public static double stddevp(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        return Math.sqrt(StdStats.varp(a, lo, hi));
    }

    private static double sum(double[] a) {
        StdStats.validateNotNull(a);
        double sum = 0.0;
        for (int i = 0; i < a.length; ++i) {
            sum += a[i];
        }
        return sum;
    }

    private static double sum(double[] a, int lo, int hi) {
        StdStats.validateNotNull(a);
        StdStats.validateSubarrayIndices(lo, hi, a.length);
        double sum = 0.0;
        for (int i = lo; i < hi; ++i) {
            sum += a[i];
        }
        return sum;
    }

    private static int sum(int[] a) {
        StdStats.validateNotNull(a);
        int sum = 0;
        for (int i = 0; i < a.length; ++i) {
            sum += a[i];
        }
        return sum;
    }

    public static void plotPoints(double[] a) {
        StdStats.validateNotNull(a);
        int n = a.length;
        StdDraw.setXscale(-1.0, n);
        StdDraw.setPenRadius(1.0 / (3.0 * (double)n));
        for (int i = 0; i < n; ++i) {
            StdDraw.point(i, a[i]);
        }
    }

    public static void plotLines(double[] a) {
        StdStats.validateNotNull(a);
        int n = a.length;
        StdDraw.setXscale(-1.0, n);
        StdDraw.setPenRadius();
        for (int i = 1; i < n; ++i) {
            StdDraw.line(i - 1, a[i - 1], i, a[i]);
        }
    }

    public static void plotBars(double[] a) {
        StdStats.validateNotNull(a);
        int n = a.length;
        StdDraw.setXscale(-1.0, n);
        for (int i = 0; i < n; ++i) {
            StdDraw.filledRectangle(i, a[i] / 2.0, 0.25, a[i] / 2.0);
        }
    }

    private static void validateNotNull(Object x) {
        if (x == null) {
            throw new IllegalArgumentException("argument is null");
        }
    }

    private static void validateSubarrayIndices(int lo, int hi, int length) {
        if (lo < 0 || hi > length || lo > hi) {
            throw new IllegalArgumentException("subarray indices out of bounds: [" + lo + ", " + hi + ")");
        }
    }

    public static void main(String[] args) {
        double[] a = StdArrayIO.readDouble1D();
        StdOut.printf("       min %10.3f\n", StdStats.min(a));
        StdOut.printf("      mean %10.3f\n", StdStats.mean(a));
        StdOut.printf("       max %10.3f\n", StdStats.max(a));
        StdOut.printf("    stddev %10.3f\n", StdStats.stddev(a));
        StdOut.printf("       var %10.3f\n", StdStats.var(a));
        StdOut.printf("   stddevp %10.3f\n", StdStats.stddevp(a));
        StdOut.printf("      varp %10.3f\n", StdStats.varp(a));
    }
}

