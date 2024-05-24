/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Arrays;
import java.util.Comparator;

public final class Point2D
implements Comparable<Point2D> {
    public static final Comparator<Point2D> X_ORDER = new XOrder();
    public static final Comparator<Point2D> Y_ORDER = new YOrder();
    public static final Comparator<Point2D> R_ORDER = new ROrder();
    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        if (Double.isInfinite(x) || Double.isInfinite(y)) {
            throw new IllegalArgumentException("Coordinates must be finite");
        }
        if (Double.isNaN(x) || Double.isNaN(y)) {
            throw new IllegalArgumentException("Coordinates cannot be NaN");
        }
        this.x = x == 0.0 ? 0.0 : x;
        this.y = y == 0.0 ? 0.0 : y;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public double r() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double theta() {
        return Math.atan2(this.y, this.x);
    }

    private double angleTo(Point2D that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        return Math.atan2(dy, dx);
    }

    public static int ccw(Point2D a, Point2D b, Point2D c) {
        double area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
        if (area2 < 0.0) {
            return -1;
        }
        if (area2 > 0.0) {
            return 1;
        }
        return 0;
    }

    public static double area2(Point2D a, Point2D b, Point2D c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    public double distanceTo(Point2D that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceSquaredTo(Point2D that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return dx * dx + dy * dy;
    }

    @Override
    public int compareTo(Point2D that) {
        if (this.y < that.y) {
            return -1;
        }
        if (this.y > that.y) {
            return 1;
        }
        if (this.x < that.x) {
            return -1;
        }
        if (this.x > that.x) {
            return 1;
        }
        return 0;
    }

    public Comparator<Point2D> polarOrder() {
        return new PolarOrder();
    }

    public Comparator<Point2D> atan2Order() {
        return new Atan2Order();
    }

    public Comparator<Point2D> distanceToOrder() {
        return new DistanceToOrder();
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
        Point2D that = (Point2D)other;
        return this.x == that.x && this.y == that.y;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public int hashCode() {
        int hashX = Double.valueOf(this.x).hashCode();
        int hashY = Double.valueOf(this.y).hashCode();
        return 31 * hashX + hashY;
    }

    public void draw() {
        StdDraw.point(this.x, this.y);
    }

    public void drawTo(Point2D that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public static void main(String[] args) {
        int x0 = Integer.parseInt(args[0]);
        int y0 = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0.0, 100.0);
        StdDraw.setYscale(0.0, 100.0);
        StdDraw.setPenRadius(0.005);
        StdDraw.enableDoubleBuffering();
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < n; ++i) {
            int x = StdRandom.uniformInt(100);
            int y = StdRandom.uniformInt(100);
            points[i] = new Point2D(x, y);
            points[i].draw();
        }
        Point2D p = new Point2D(x0, y0);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.02);
        p.draw();
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);
        Arrays.sort(points, p.polarOrder());
        for (int i = 0; i < n; ++i) {
            p.drawTo(points[i]);
            StdDraw.show();
            StdDraw.pause(100);
        }
    }

    private class PolarOrder
    implements Comparator<Point2D> {
        private PolarOrder() {
        }

        @Override
        public int compare(Point2D q1, Point2D q2) {
            double dx1 = q1.x - Point2D.this.x;
            double dy1 = q1.y - Point2D.this.y;
            double dx2 = q2.x - Point2D.this.x;
            double dy2 = q2.y - Point2D.this.y;
            if (dy1 >= 0.0 && dy2 < 0.0) {
                return -1;
            }
            if (dy2 >= 0.0 && dy1 < 0.0) {
                return 1;
            }
            if (dy1 == 0.0 && dy2 == 0.0) {
                if (dx1 >= 0.0 && dx2 < 0.0) {
                    return -1;
                }
                if (dx2 >= 0.0 && dx1 < 0.0) {
                    return 1;
                }
                return 0;
            }
            return -Point2D.ccw(Point2D.this, q1, q2);
        }
    }

    private class Atan2Order
    implements Comparator<Point2D> {
        private Atan2Order() {
        }

        @Override
        public int compare(Point2D q1, Point2D q2) {
            double angle1 = Point2D.this.angleTo(q1);
            double angle2 = Point2D.this.angleTo(q2);
            return Double.compare(angle1, angle2);
        }
    }

    private class DistanceToOrder
    implements Comparator<Point2D> {
        private DistanceToOrder() {
        }

        @Override
        public int compare(Point2D p, Point2D q) {
            double dist1 = Point2D.this.distanceSquaredTo(p);
            double dist2 = Point2D.this.distanceSquaredTo(q);
            return Double.compare(dist1, dist2);
        }
    }

    private static class XOrder
    implements Comparator<Point2D> {
        private XOrder() {
        }

        @Override
        public int compare(Point2D p, Point2D q) {
            return Double.compare(p.x, q.x);
        }
    }

    private static class YOrder
    implements Comparator<Point2D> {
        private YOrder() {
        }

        @Override
        public int compare(Point2D p, Point2D q) {
            return Double.compare(p.y, q.y);
        }
    }

    private static class ROrder
    implements Comparator<Point2D> {
        private ROrder() {
        }

        @Override
        public int compare(Point2D p, Point2D q) {
            double delta = p.x * p.x + p.y * p.y - (q.x * q.x + q.y * q.y);
            return Double.compare(delta, 0.0);
        }
    }
}

