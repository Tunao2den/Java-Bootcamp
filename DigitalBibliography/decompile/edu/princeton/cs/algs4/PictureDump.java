/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class PictureDump {
    private PictureDump() {
    }

    public static void main(String[] args) {
        int width = Integer.parseInt(args[0]);
        int height = Integer.parseInt(args[1]);
        Picture picture = new Picture(width, height);
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                if (!BinaryStdIn.isEmpty()) {
                    boolean bit = BinaryStdIn.readBoolean();
                    if (bit) {
                        picture.set(col, row, Color.BLACK);
                        continue;
                    }
                    picture.set(col, row, Color.WHITE);
                    continue;
                }
                picture.set(col, row, Color.RED);
            }
        }
        picture.show();
    }
}

