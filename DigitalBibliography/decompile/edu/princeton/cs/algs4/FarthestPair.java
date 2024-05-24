/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.GrahamScan;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class FarthestPair {
    private Point2D best1;
    private Point2D best2;
    private double bestDistanceSquared = Double.NEGATIVE_INFINITY;

    /*
     * WARNING - void declaration
     */
    public FarthestPair(Point2D[] points) {
        void var5_9;
        if (points == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        for (int i = 0; i < points.length; ++i) {
            if (points[i] != null) continue;
            throw new IllegalArgumentException("array element " + i + " is null");
        }
        GrahamScan graham = new GrahamScan(points);
        if (points.length <= 1) {
            return;
        }
        int m = 0;
        for (Point2D point2D : graham.hull()) {
            ++m;
        }
        Point2D[] hull = new Point2D[m + 1];
        m = 1;
        for (Point2D p : graham.hull()) {
            hull[m++] = p;
        }
        if (--m == 1) {
            return;
        }
        if (m == 2) {
            this.best1 = hull[1];
            this.best2 = hull[2];
            this.bestDistanceSquared = this.best1.distanceSquaredTo(this.best2);
            return;
        }
        int n = 2;
        while (Point2D.area2(hull[m], hull[1], hull[var5_9 + true]) > Point2D.area2(hull[m], hull[1], hull[var5_9])) {
            ++var5_9;
        }
        void j = var5_9;
        for (int i = 1; i <= var5_9 && j <= m; ++i) {
            if (hull[i].distanceSquaredTo(hull[j]) > this.bestDistanceSquared) {
                this.best1 = hull[i];
                this.best2 = hull[j];
                this.bestDistanceSquared = hull[i].distanceSquaredTo(hull[j]);
            }
            while (j < m && Point2D.area2(hull[i], hull[i + 1], hull[j + true]) > Point2D.area2(hull[i], hull[i + 1], hull[j])) {
                double distanceSquared;
                if (!((distanceSquared = hull[i].distanceSquaredTo(hull[++j])) > this.bestDistanceSquared)) continue;
                this.best1 = hull[i];
                this.best2 = hull[j];
                this.bestDistanceSquared = hull[i].distanceSquaredTo(hull[j]);
            }
        }
    }

    public Point2D either() {
        return this.best1;
    }

    public Point2D other() {
        return this.best2;
    }

    public double distance() {
        return Math.sqrt(this.bestDistanceSquared);
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < n; ++i) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point2D(x, y);
        }
        FarthestPair farthest = new FarthestPair(points);
        StdOut.println(farthest.distance() + " from " + farthest.either() + " to " + farthest.other());
    }
}

