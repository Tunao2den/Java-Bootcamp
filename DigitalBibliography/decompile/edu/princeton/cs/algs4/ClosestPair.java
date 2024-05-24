/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class ClosestPair {
    private Point2D best1;
    private Point2D best2;
    private double bestDistance = Double.POSITIVE_INFINITY;

    public ClosestPair(Point2D[] points) {
        int i;
        if (points == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        for (int i2 = 0; i2 < points.length; ++i2) {
            if (points[i2] != null) continue;
            throw new IllegalArgumentException("array element " + i2 + " is null");
        }
        int n = points.length;
        if (n <= 1) {
            return;
        }
        Point2D[] pointsByX = new Point2D[n];
        for (i = 0; i < n; ++i) {
            pointsByX[i] = points[i];
        }
        Arrays.sort(pointsByX, Point2D.Y_ORDER);
        Arrays.sort(pointsByX, Point2D.X_ORDER);
        for (i = 0; i < n - 1; ++i) {
            if (!pointsByX[i].equals(pointsByX[i + 1])) continue;
            this.bestDistance = 0.0;
            this.best1 = pointsByX[i];
            this.best2 = pointsByX[i + 1];
            return;
        }
        Point2D[] pointsByY = new Point2D[n];
        for (int i3 = 0; i3 < n; ++i3) {
            pointsByY[i3] = pointsByX[i3];
        }
        Point2D[] aux = new Point2D[n];
        this.closest(pointsByX, pointsByY, aux, 0, n - 1);
    }

    private double closest(Point2D[] pointsByX, Point2D[] pointsByY, Point2D[] aux, int lo, int hi) {
        int i;
        if (hi <= lo) {
            return Double.POSITIVE_INFINITY;
        }
        int mid = lo + (hi - lo) / 2;
        Point2D median = pointsByX[mid];
        double delta1 = this.closest(pointsByX, pointsByY, aux, lo, mid);
        double delta2 = this.closest(pointsByX, pointsByY, aux, mid + 1, hi);
        double delta = Math.min(delta1, delta2);
        ClosestPair.merge(pointsByY, aux, lo, mid, hi);
        int m = 0;
        for (i = lo; i <= hi; ++i) {
            if (!(Math.abs(pointsByY[i].x() - median.x()) < delta)) continue;
            aux[m++] = pointsByY[i];
        }
        for (i = 0; i < m; ++i) {
            for (int j = i + 1; j < m && aux[j].y() - aux[i].y() < delta; ++j) {
                double distance = aux[i].distanceTo(aux[j]);
                if (!(distance < delta)) continue;
                delta = distance;
                if (!(distance < this.bestDistance)) continue;
                this.bestDistance = delta;
                this.best1 = aux[i];
                this.best2 = aux[j];
            }
        }
        return delta;
    }

    public Point2D either() {
        return this.best1;
    }

    public Point2D other() {
        return this.best2;
    }

    public double distance() {
        return this.bestDistance;
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; ++k) {
            aux[k] = a[k];
        }
        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; ++k) {
            a[k] = i > mid ? aux[j++] : (j > hi ? aux[i++] : (ClosestPair.less(aux[j], aux[i]) ? aux[j++] : aux[i++]));
        }
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < n; ++i) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            points[i] = new Point2D(x, y);
        }
        ClosestPair closest = new ClosestPair(points);
        StdOut.println(closest.distance() + " from " + closest.either() + " to " + closest.other());
    }
}

