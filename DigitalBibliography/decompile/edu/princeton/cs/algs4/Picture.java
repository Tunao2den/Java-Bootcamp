/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public final class Picture
implements ActionListener {
    private BufferedImage image;
    private JFrame jframe;
    private String title;
    private boolean isOriginUpperLeft = true;
    private boolean isVisible = false;
    private boolean isDisposed = false;
    private final int width;
    private final int height;

    public Picture(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        if (height <= 0) {
            throw new IllegalArgumentException("height must be positive");
        }
        this.width = width;
        this.height = height;
        this.title = width + "-by-" + height;
        this.image = new BufferedImage(width, height, 2);
    }

    public Picture(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        this.width = picture.width();
        this.height = picture.height();
        this.image = new BufferedImage(this.width, this.height, 2);
        this.title = picture.title;
        this.isOriginUpperLeft = picture.isOriginUpperLeft;
        for (int col = 0; col < this.width(); ++col) {
            for (int row = 0; row < this.height(); ++row) {
                this.image.setRGB(col, row, picture.image.getRGB(col, row));
            }
        }
    }

    public Picture(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        if (filename.length() == 0) {
            throw new IllegalArgumentException("constructor argument is the empty string");
        }
        this.title = filename;
        try {
            File file = new File(filename);
            if (file.isFile()) {
                this.title = file.getName();
                this.image = ImageIO.read(file);
            } else {
                URL url = this.getClass().getResource(filename);
                if (url == null) {
                    url = this.getClass().getClassLoader().getResource(filename);
                }
                if (url == null) {
                    url = new URL(filename);
                }
                this.image = ImageIO.read(url);
            }
            if (this.image == null) {
                throw new IllegalArgumentException("could not read image: " + filename);
            }
            this.width = this.image.getWidth(null);
            this.height = this.image.getHeight(null);
            if (this.image.getType() != 2) {
                BufferedImage imageARGB = new BufferedImage(this.width, this.height, 2);
                imageARGB.createGraphics().drawImage((Image)this.image, 0, 0, null);
                this.image = imageARGB;
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("could not open image: " + filename, ioe);
        }
    }

    public Picture(File file) {
        if (file == null) {
            throw new IllegalArgumentException("constructor argument is null");
        }
        try {
            BufferedImage image = ImageIO.read(file);
            this.width = image.getWidth(null);
            this.height = image.getHeight(null);
            this.title = file.getName();
            if (image.getType() != 1) {
                BufferedImage imageARGB = new BufferedImage(this.width, this.height, 2);
                imageARGB.createGraphics().drawImage((Image)image, 0, 0, null);
                image = imageARGB;
            }
        } catch (IOException ioe) {
            throw new IllegalArgumentException("could not open file: " + file, ioe);
        }
    }

    private JFrame createGUI() {
        JFrame frame = new JFrame();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem(" Save...   ");
        menuItem1.addActionListener(this);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(menuItem1);
        frame.setJMenuBar(menuBar);
        frame.setContentPane(this.getJLabel());
        frame.setDefaultCloseOperation(2);
        frame.setTitle(this.title);
        frame.setResizable(false);
        frame.pack();
        frame.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e) {
                Picture.this.isVisible = false;
                Picture.this.isDisposed = true;
                super.windowClosing(e);
            }
        });
        return frame;
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
        if (this.jframe == null && !this.isDisposed) {
            this.jframe = this.createGUI();
            this.isVisible = true;
            this.jframe.setVisible(true);
            this.jframe.repaint();
        }
        if (this.jframe != null && !this.isDisposed) {
            this.isVisible = true;
            this.jframe.setVisible(true);
            this.jframe.repaint();
        }
    }

    public void hide() {
        if (this.jframe != null) {
            this.isVisible = false;
            this.jframe.setVisible(false);
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

    public Color get(int col, int row) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
        int argb = this.getRGB(col, row);
        return new Color(argb, true);
    }

    public int getRGB(int col, int row) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
        if (this.isOriginUpperLeft) {
            return this.image.getRGB(col, row);
        }
        return this.image.getRGB(col, this.height - row - 1);
    }

    public void set(int col, int row, Color color) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
        if (color == null) {
            throw new IllegalArgumentException("color argument is null");
        }
        int rgb = color.getRGB();
        this.setRGB(col, row, rgb);
    }

    public void setRGB(int col, int row, int rgb) {
        this.validateColumnIndex(col);
        this.validateRowIndex(row);
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
        Picture that = (Picture)other;
        if (this.width() != that.width()) {
            return false;
        }
        if (this.height() != that.height()) {
            return false;
        }
        for (int col = 0; col < this.width(); ++col) {
            for (int row = 0; row < this.height(); ++row) {
                if (this.getRGB(col, row) == that.getRGB(col, row)) continue;
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.width + "-by-" + this.height + " picture (RGB values given in hex)\n");
        for (int row = 0; row < this.height; ++row) {
            for (int col = 0; col < this.width; ++col) {
                int rgb = this.isOriginUpperLeft ? this.image.getRGB(col, row) : this.image.getRGB(col, this.height - row - 1);
                sb.append(String.format("#%06X ", rgb & 0xFFFFFF));
            }
            sb.append("\n");
        }
        return sb.toString().trim();
    }

    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("title is null");
        }
        this.title = title;
    }

    private boolean hasAlpha() {
        for (int col = 0; col < this.width; ++col) {
            for (int row = 0; row < this.height; ++row) {
                int argb = this.image.getRGB(col, row);
                int alpha = argb >> 24 & 0xFF;
                if (alpha == 255) continue;
                return true;
            }
        }
        return false;
    }

    public void save(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("argument to save() is null");
        }
        if (filename.length() == 0) {
            throw new IllegalArgumentException("argument to save() is the empty string");
        }
        File file = new File(filename);
        this.save(file);
    }

    public void save(File file) {
        if (file == null) {
            throw new IllegalArgumentException("argument to save() is null");
        }
        this.title = file.getName();
        String suffix = this.title.substring(this.title.lastIndexOf(46) + 1);
        if (!this.title.contains(".") || suffix.length() == 0) {
            System.out.printf("Error: the filename '%s' has no file extension, such as .jpg or .png\n", this.title);
            return;
        }
        try {
            if (ImageIO.write((RenderedImage)this.image, suffix, file)) {
                return;
            }
            BufferedImage imageRGB = new BufferedImage(this.width, this.height, 1);
            imageRGB.createGraphics().drawImage(this.image, 0, 0, Color.WHITE, null);
            if (ImageIO.write((RenderedImage)imageRGB, suffix, file)) {
                return;
            }
            System.out.printf("Error: the filetype '%s' is not supported\n", suffix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog((Frame)this.jframe, "The filetype extension must be either .jpg or .png", 1);
        chooser.setVisible(true);
        String selectedDirectory = chooser.getDirectory();
        String selectedFilename = chooser.getFile();
        if (selectedDirectory != null && selectedFilename != null) {
            this.save(selectedDirectory + selectedFilename);
        }
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        System.out.printf("%d-by-%d\n", picture.width(), picture.height());
        picture.show();
    }
}

