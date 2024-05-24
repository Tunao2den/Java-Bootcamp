/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class GrayscalePicture
implements ActionListener {
    private BufferedImage image;
    private JFrame frame;
    private String filename;
    private boolean isOriginUpperLeft = true;
    private boolean isVisible = false;
    private final int width;
    private final int height;

    public GrayscalePicture(int width, int height) {
        if (width < 0) {
            throw new IllegalArgumentException("width must be non-negative");
        }
        if (height < 0) {
            throw new IllegalArgumentException("height must be non-negative");
        }
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, 1);
    }

    public GrayscalePicture(GrayscalePicture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        this.width = picture.width();
        this.height = picture.height();
        this.image = new BufferedImage(this.width, this.height, 1);
        this.filename = picture.filename;
        this.isOriginUpperLeft = picture.isOriginUpperLeft;
        for (int col = 0; col < this.width(); ++col) {
            for (int row = 0; row < this.height(); ++row) {
                this.image.setRGB(col, row, picture.image.getRGB(col, row));
            }
        }
    }

    public GrayscalePicture(String name) {
        if (name == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        this.filename = name;
        try {
            File file = new File(name);
            if (file.isFile()) {
                this.image = ImageIO.read(file);
            } else {
                URL url = this.getClass().getResource(name);
                if (url == null) {
                    url = this.getClass().getClassLoader().getResource(name);
                }
                if (url == null) {
                    url = new URL(name);
                }
                this.image = ImageIO.read(url);
            }
            if (this.image == null) {
                throw new IllegalArgumentException("could not read image: " + name);
            }
            this.width = this.image.getWidth(null);
            this.height = this.image.getHeight(null);
            for (int col = 0; col < this.width; ++col) {
                for (int row = 0; row < this.height; ++row) {
                    Color color = new Color(this.image.getRGB(col, row));
                    Color gray = GrayscalePicture.toGray(color);
                    this.image.setRGB(col, row, gray.getRGB());
                }
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("could not open image: " + name, ioe);
        }
    }

    private static Color toGray(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int y = (int)Math.round(0.299 * (double)r + 0.587 * (double)g + 0.114 * (double)b);
        return new Color(y, y, y);
    }

    public JLabel getJLabel() {
        if (this.image == null) {
            return null;
        }
        ImageIcon icon = new ImageIcon(this.image);
        return new JLabel(icon);
    }

    public void setOriginUpperLeft() {
        this.isOriginUpperLeft = true;
    }

    public void setOriginLowerLeft() {
        this.isOriginUpperLeft = false;
    }

    public void show() {
        if (this.frame == null) {
            this.frame = new JFrame();
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(this);
            menuItem1.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
            menu.add(menuItem1);
            this.frame.setJMenuBar(menuBar);
            this.frame.setContentPane(this.getJLabel());
            this.frame.setDefaultCloseOperation(2);
            if (this.filename == null) {
                this.frame.setTitle(this.width + "-by-" + this.height);
            } else {
                this.frame.setTitle(this.filename);
            }
            this.frame.setResizable(false);
            this.frame.pack();
        }
        this.frame.setVisible(true);
        this.isVisible = true;
        this.frame.repaint();
    }

    public void hide() {
        if (this.frame != null) {
            this.isVisible = false;
            this.frame.setVisible(false);
        }
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    public int height() {
        return this.height;
    }

    public int width() {
        return this.width;
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= this.height()) {
            throw new IllegalArgumentException("row index must be between 0 and " + (this.height() - 1) + ": " + row);
        }
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= this.width()) {
            throw new IllegalArgumentException("column index must be between 0 and " + (this.width() - 1) + ": " + col);
        }
    }

    private void validateGrayscaleValue(int gray) {
        if (gray < 0 || gray >= 256) {
            throw new IllegalArgumentException("grayscale value must be between 0 and 255");
        }
    }

    public Color get(int col, int row) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
        Color color = new Color(this.image.getRGB(col, row));
        return GrayscalePicture.toGray(color);
    }

    public int getGrayscale(int col, int row) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
        if (this.isOriginUpperLeft) {
            return this.image.getRGB(col, row) & 0xFF;
        }
        return this.image.getRGB(col, this.height - row - 1) & 0xFF;
    }

    public void set(int col, int row, Color color) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
        if (color == null) {
            throw new IllegalArgumentException("color argument is null");
        }
        Color gray = GrayscalePicture.toGray(color);
        this.image.setRGB(col, row, gray.getRGB());
    }

    public void setGrayscale(int col, int row, int gray) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
        this.validateGrayscaleValue(gray);
        int rgb = gray | gray << 8 | gray << 16;
        if (this.isOriginUpperLeft) {
            this.image.setRGB(col, row, rgb);
        } else {
            this.image.setRGB(col, this.height - row - 1, rgb);
        }
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
        GrayscalePicture that = (GrayscalePicture)other;
        if (this.width() != that.width()) {
            return false;
        }
        if (this.height() != that.height()) {
            return false;
        }
        for (int col = 0; col < this.width(); ++col) {
            for (int row = 0; row < this.height(); ++row) {
                if (this.getGrayscale(col, row) == that.getGrayscale(col, row)) continue;
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.width + "-by-" + this.height + " grayscale picture (grayscale values given in hex)\n");
        for (int row = 0; row < this.height; ++row) {
            for (int col = 0; col < this.width; ++col) {
                int gray = this.isOriginUpperLeft ? 0xFF & this.image.getRGB(col, row) : 0xFF & this.image.getRGB(col, this.height - row - 1);
                sb.append(String.format("%3d ", gray));
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
    }

    public void save(String name) {
        if (name == null) {
            throw new IllegalArgumentException("argument to save() is null");
        }
        this.save(new File(name));
        this.filename = name;
    }

    public void save(File file) {
        if (file == null) {
            throw new IllegalArgumentException("argument to save() is null");
        }
        this.filename = file.getName();
        if (this.frame != null) {
            this.frame.setTitle(this.filename);
        }
        String suffix = this.filename.substring(this.filename.lastIndexOf(46) + 1);
        if (!this.filename.contains(".") || suffix.length() == 0) {
            System.out.printf("Error: the filename '%s' has no file extension, such as .jpg or .png\n", this.filename);
            return;
        }
        if ("jpg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix)) {
            try {
                ImageIO.write((RenderedImage)this.image, suffix, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog((Frame)this.frame, "Use a .png or .jpg extension", 1);
        chooser.setVisible(true);
        String selectedDirectory = chooser.getDirectory();
        String selectedFilename = chooser.getFile();
        if (selectedDirectory != null && selectedFilename != null) {
            this.save(selectedDirectory + selectedFilename);
        }
    }

    public static void main(String[] args) {
        GrayscalePicture picture = new GrayscalePicture(args[0]);
        StdOut.printf("%d-by-%d\n", picture.width(), picture.height());
        GrayscalePicture copy = new GrayscalePicture(picture);
        picture.show();
        copy.show();
        while (!StdIn.isEmpty()) {
            int row = StdIn.readInt();
            int col = StdIn.readInt();
            int gray = StdIn.readInt();
            picture.setGrayscale(row, col, gray);
            StdOut.println(picture.get(row, col));
            StdOut.println(picture.getGrayscale(row, col));
        }
    }
}

