/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class GrahamScan {
    private Stack<Point2D> hull = new Stack();

    public GrahamScan(Point2D[] points) {
        int k2;
        int k1;
        if (points == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (points.length == 0) {
            throw new IllegalArgumentException("array is of length 0");
        }
        int n = points.length;
        Object[] a = new Point2D[n];
        for (int i = 0; i < n; ++i) {
            if (points[i] == null) {
                throw new IllegalArgumentException("points[" + i + "] is null");
            }
            a[i] = points[i];
        }
        Arrays.sort(a);
        Arrays.sort(a, 1, n, ((Point2D)a[0]).polarOrder());
        this.hull.push((Point2D)a[0]);
        for (k1 = 1; k1 < n && ((Point2D)a[0]).equals(a[k1]); ++k1) {
        }
        if (k1 == n) {
            return;
        }
        for (k2 = k1 + 1; k2 < n && Point2D.ccw((Point2D)a[0], (Point2D)a[k1], (Point2D)a[k2]) == 0; ++k2) {
        }
        this.hull.push((Point2D)a[k2 - 1]);
        for (int i = k2; i < n; ++i) {
            Point2D top = this.hull.pop();
            while (Point2D.ccw(this.hull.peek(), top, (Point2D)a[i]) <= 0) {
                top = this.hull.pop();
            }
            this.hull.push(top);
            this.hull.push((Point2D)a[i]);
        }
        assert (this.isConvex());
    }

    public Iterable<Point2D> hull() {
        Stack<Point2D> s = new Stack<Point2D>();
        for (Point2D p : this.hull) {
            s.push(p);
        }
        return s;
    }

    private boolean isConvex() {
        int n = this.hull.size();
        if (n <= 2) {
            return true;
        }
        Point2D[] points = new Point2D[n];
        int k = 0;
        for (Point2D p : this.hull()) {
            points[k++] = p;
        }
        for (int i = 0; i < n; ++i) {
            if (Point2D.ccw(points[i], points[(i + 1) % n], points[(i + 2) % n]) > 0) continue;
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        Point2D[] points = new Point2D[n];
        for (int i = 0; i < n; ++i) {
            int x = StdIn.readInt();
            int y = StdIn.readInt();
            points[i] = new Point2D(x, y);
        }
        GrahamScan graham = new GrahamScan(points);
        for (Point2D p : graham.hull()) {
            StdOut.println(p);
        }
    }
}

