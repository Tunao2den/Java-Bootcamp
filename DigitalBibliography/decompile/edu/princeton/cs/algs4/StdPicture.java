/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Picture;

public final class StdPicture {
    private static final int DEFAULT_SIZE = 512;
    private static Picture picture = new Picture(512, 512);

    private StdPicture() {
    }

    public static void init(int width, int height) {
        if (picture.isVisible()) {
            StdPicture.hide();
            picture = new Picture(width, height);
            StdPicture.show();
        } else {
            picture = new Picture(width, height);
        }
    }

    @Deprecated
    public static void create(int width, int height) {
        StdPicture.init(width, height);
    }

    public static void read(String filename) {
        Picture newPicture = new Picture(filename);
        if (newPicture.width() == picture.width() && newPicture.height() == newPicture.height()) {
            for (int col = 0; col < picture.width(); ++col) {
                for (int row = 0; row < picture.height(); ++row) {
                    picture.setRGB(col, row, newPicture.getRGB(col, row));
                }
            }
        } else if (picture.isVisible()) {
            StdPicture.hide();
            picture = newPicture;
            StdPicture.show();
        } else {
            picture = newPicture;
        }
    }

    @Deprecated
    public static void create(String filename) {
        StdPicture.read(filename);
    }

    public static void show() {
        picture.show();
    }

    public static void hide() {
        picture.hide();
    }

    public static void pause(int t) {
        if (t < 0) {
            throw new IllegalArgumentException("argument must be non-negative");
        }
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

    public static int height() {
        return picture.height();
    }

    public static int width() {
        return picture.width();
    }

    public static int getAlpha(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return rgb >> 24 & 0xFF;
    }

    public static int getRed(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return rgb >> 16 & 0xFF;
    }

    public static int getGreen(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return rgb >> 8 & 0xFF;
    }

    public static int getBlue(int col, int row) {
        int rgb = picture.getRGB(col, row);
        return rgb >> 0 & 0xFF;
    }

    public static void setRGB(int col, int row, int r, int g, int b) {
        int a = 255;
        int rgb = a << 24 | r << 16 | g << 8 | b << 0;
        picture.setRGB(col, row, rgb);
    }

    public static void setARGB(int col, int row, int a, int r, int g, int b) {
        int rgb = a << 24 | r << 16 | g << 8 | b << 0;
        picture.setRGB(col, row, rgb);
    }

    public static void setTitle(String title) {
        picture.setTitle(title);
    }

    public static void save(String filename) {
        picture.save(filename);
    }

    public static void main(String[] args) {
        StdPicture.read(args[0]);
        System.out.printf("%d-by-%d\n", picture.width(), picture.height());
        StdPicture.show();
    }
}

