/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import java.awt.Color;

public class Particle {
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private double rx;
    private double ry;
    private double vx;
    private double vy;
    private int count;
    private final double radius;
    private final double mass;
    private final Color color;

    public Particle(double rx, double ry, double vx, double vy, double radius, double mass, Color color) {
        this.vx = vx;
        this.vy = vy;
        this.rx = rx;
        this.ry = ry;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
    }

    public Particle() {
        this.rx = StdRandom.uniformDouble(0.0, 1.0);
        this.ry = StdRandom.uniformDouble(0.0, 1.0);
        this.vx = StdRandom.uniformDouble(-0.005, 0.005);
        this.vy = StdRandom.uniformDouble(-0.005, 0.005);
        this.radius = 0.02;
        this.mass = 0.5;
        this.color = Color.BLACK;
    }

    public void move(double dt) {
        this.rx += this.vx * dt;
        this.ry += this.vy * dt;
    }

    public void draw() {
        StdDraw.setPenColor(this.color);
        StdDraw.filledCircle(this.rx, this.ry, this.radius);
    }

    public int count() {
        return this.count;
    }

    public double timeToHit(Particle that) {
        if (this == that) {
            return Double.POSITIVE_INFINITY;
        }
        double dx = that.rx - this.rx;
        double dvx = that.vx - this.vx;
        double dy = that.ry - this.ry;
        double dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;
        if (dvdr > 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        double dvdv = dvx * dvx + dvy * dvy;
        if (dvdv == 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        double drdr = dx * dx + dy * dy;
        double sigma = this.radius + that.radius;
        double d = dvdr * dvdr - dvdv * (drdr - sigma * sigma);
        if (d < 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    public double timeToHitVerticalWall() {
        if (this.vx > 0.0) {
            return (1.0 - this.rx - this.radius) / this.vx;
        }
        if (this.vx < 0.0) {
            return (this.radius - this.rx) / this.vx;
        }
        return Double.POSITIVE_INFINITY;
    }

    public double timeToHitHorizontalWall() {
        if (this.vy > 0.0) {
            return (1.0 - this.ry - this.radius) / this.vy;
        }
        if (this.vy < 0.0) {
            return (this.radius - this.ry) / this.vy;
        }
        return Double.POSITIVE_INFINITY;
    }

    public void bounceOff(Particle that) {
        double dx = that.rx - this.rx;
        double dy = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx * dvx + dy * dvy;
        double dist = this.radius + that.radius;
        double magnitude = 2.0 * this.mass * that.mass * dvdr / ((this.mass + that.mass) * dist);
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;
        this.vx += fx / this.mass;
        this.vy += fy / this.mass;
        that.vx -= fx / that.mass;
        that.vy -= fy / that.mass;
        ++this.count;
        ++that.count;
    }

    public void bounceOffVerticalWall() {
        this.vx = -this.vx;
        ++this.count;
    }

    public void bounceOffHorizontalWall() {
        this.vy = -this.vy;
        ++this.count;
    }

    public double kineticEnergy() {
        return 0.5 * this.mass * (this.vx * this.vx + this.vy * this.vy);
    }
}

