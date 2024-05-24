/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

public interface DrawListener {
    default public void mousePressed(double x, double y) {
    }

    default public void mouseDragged(double x, double y) {
    }

    default public void mouseReleased(double x, double y) {
    }

    default public void mouseClicked(double x, double y) {
    }

    default public void keyTyped(char c) {
    }

    default public void keyPressed(int keycode) {
    }

    default public void keyReleased(int keycode) {
    }

    default public void update() {
    }
}

