/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Particle;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import java.awt.Color;

public class CollisionSystem {
    private static final double HZ = 0.5;
    private MinPQ<Event> pq;
    private double t = 0.0;
    private Particle[] particles;

    public CollisionSystem(Particle[] particles) {
        this.particles = (Particle[])particles.clone();
    }

    private void predict(Particle a, double limit) {
        if (a == null) {
            return;
        }
        for (int i = 0; i < this.particles.length; ++i) {
            double dt = a.timeToHit(this.particles[i]);
            if (!(this.t + dt <= limit)) continue;
            this.pq.insert(new Event(this.t + dt, a, this.particles[i]));
        }
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        if (this.t + dtX <= limit) {
            this.pq.insert(new Event(this.t + dtX, a, null));
        }
        if (this.t + dtY <= limit) {
            this.pq.insert(new Event(this.t + dtY, null, a));
        }
    }

    private void redraw(double limit) {
        StdDraw.clear();
        for (int i = 0; i < this.particles.length; ++i) {
            this.particles[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(20);
        if (this.t < limit) {
            this.pq.insert(new Event(this.t + 2.0, null, null));
        }
    }

    public void simulate(double limit) {
        this.pq = new MinPQ();
        for (int i = 0; i < this.particles.length; ++i) {
            this.predict(this.particles[i], limit);
        }
        this.pq.insert(new Event(0.0, null, null));
        while (!this.pq.isEmpty()) {
            Event e = this.pq.delMin();
            if (!e.isValid()) continue;
            Particle a = e.a;
            Particle b = e.b;
            for (int i = 0; i < this.particles.length; ++i) {
                this.particles[i].move(e.time - this.t);
            }
            this.t = e.time;
            if (a != null && b != null) {
                a.bounceOff(b);
            } else if (a != null && b == null) {
                a.bounceOffVerticalWall();
            } else if (a == null && b != null) {
                b.bounceOffHorizontalWall();
            } else if (a == null && b == null) {
                this.redraw(limit);
            }
            this.predict(a, limit);
            this.predict(b, limit);
        }
    }

    public static void main(String[] args) {
        Particle[] particles;
        StdDraw.setCanvasSize(600, 600);
        StdDraw.enableDoubleBuffering();
        if (args.length == 1) {
            n = Integer.parseInt(args[0]);
            particles = new Particle[n];
            for (int i = 0; i < n; ++i) {
                particles[i] = new Particle();
            }
        } else {
            n = StdIn.readInt();
            particles = new Particle[n];
            for (int i = 0; i < n; ++i) {
                double rx = StdIn.readDouble();
                double ry = StdIn.readDouble();
                double vx = StdIn.readDouble();
                double vy = StdIn.readDouble();
                double radius = StdIn.readDouble();
                double mass = StdIn.readDouble();
                int r = StdIn.readInt();
                int g = StdIn.readInt();
                int b = StdIn.readInt();
                Color color = new Color(r, g, b);
                particles[i] = new Particle(rx, ry, vx, vy, radius, mass, color);
            }
        }
        CollisionSystem system = new CollisionSystem(particles);
        system.simulate(10000.0);
    }

    private static class Event
    implements Comparable<Event> {
        private final double time;
        private final Particle a;
        private final Particle b;
        private final int countA;
        private final int countB;

        public Event(double t, Particle a, Particle b) {
            this.time = t;
            this.a = a;
            this.b = b;
            this.countA = a != null ? a.count() : -1;
            this.countB = b != null ? b.count() : -1;
        }

        @Override
        public int compareTo(Event that) {
            return Double.compare(this.time, that.time);
        }

        public boolean isValid() {
            if (this.a != null && this.a.count() != this.countA) {
                return false;
            }
            return this.b == null || this.b.count() == this.countB;
        }
    }
}

