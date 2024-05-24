/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class StdDraw
implements ActionListener,
MouseListener,
MouseMotionListener,
KeyListener {
    public static final Color BLACK = Color.BLACK;
    public static final Color BLUE = Color.BLUE;
    public static final Color CYAN = Color.CYAN;
    public static final Color DARK_GRAY = Color.DARK_GRAY;
    public static final Color GRAY = Color.GRAY;
    public static final Color GREEN = Color.GREEN;
    public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
    public static final Color MAGENTA = Color.MAGENTA;
    public static final Color ORANGE = Color.ORANGE;
    public static final Color PINK = Color.PINK;
    public static final Color RED = Color.RED;
    public static final Color WHITE = Color.WHITE;
    public static final Color YELLOW = Color.YELLOW;
    public static final Color BOOK_BLUE = new Color(9, 90, 166);
    public static final Color BOOK_LIGHT_BLUE = new Color(103, 198, 243);
    public static final Color BOOK_RED = new Color(150, 35, 31);
    public static final Color PRINCETON_ORANGE = new Color(245, 128, 37);
    private static final Color DEFAULT_PEN_COLOR = BLACK;
    private static final Color DEFAULT_CLEAR_COLOR = WHITE;
    private static Color penColor;
    private static final String DEFAULT_WINDOW_TITLE = "Standard Draw";
    private static String windowTitle;
    private static final int DEFAULT_SIZE = 512;
    private static int width;
    private static int height;
    private static final double DEFAULT_PEN_RADIUS = 0.002;
    private static double penRadius;
    private static boolean defer;
    private static final double BORDER = 0.0;
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = 1.0;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = 1.0;
    private static double xmin;
    private static double ymin;
    private static double xmax;
    private static double ymax;
    private static final Object MOUSE_LOCK;
    private static final Object KEY_LOCK;
    private static final Font DEFAULT_FONT;
    private static Font font;
    private static BufferedImage offscreenImage;
    private static BufferedImage onscreenImage;
    private static Graphics2D offscreen;
    private static Graphics2D onscreen;
    private static StdDraw std;
    private static JFrame frame;
    private static boolean isMousePressed;
    private static double mouseX;
    private static double mouseY;
    private static LinkedList<Character> keysTyped;
    private static TreeSet<Integer> keysDown;

    private StdDraw() {
    }

    public static void setVisible(boolean isVisible) {
        frame.setVisible(isVisible);
    }

    public static void setCanvasSize() {
        StdDraw.setCanvasSize(512, 512);
    }

    public static void setCanvasSize(int canvasWidth, int canvasHeight) {
        if (canvasWidth <= 0) {
            throw new IllegalArgumentException("width must be positive");
        }
        if (canvasHeight <= 0) {
            throw new IllegalArgumentException("height must be positive");
        }
        width = canvasWidth;
        height = canvasHeight;
        StdDraw.init();
    }

    private static void init() {
        if (frame == null) {
            frame = new JFrame();
            frame.addKeyListener(std);
            frame.setFocusTraversalKeysEnabled(false);
            frame.setResizable(false);
            frame.setDefaultCloseOperation(3);
            frame.setTitle(windowTitle);
            frame.setJMenuBar(StdDraw.createMenuBar());
        }
        offscreenImage = new BufferedImage(2 * width, 2 * height, 2);
        onscreenImage = new BufferedImage(2 * width, 2 * height, 2);
        offscreen = offscreenImage.createGraphics();
        onscreen = onscreenImage.createGraphics();
        offscreen.scale(2.0, 2.0);
        StdDraw.setXscale();
        StdDraw.setYscale();
        offscreen.setColor(DEFAULT_CLEAR_COLOR);
        offscreen.fillRect(0, 0, width, height);
        onscreen.setColor(DEFAULT_CLEAR_COLOR);
        onscreen.fillRect(0, 0, 2 * width, 2 * height);
        StdDraw.setPenColor();
        StdDraw.setPenRadius();
        StdDraw.setFont();
        keysTyped = new LinkedList();
        keysDown = new TreeSet();
        RenderingHints hints = new RenderingHints(null);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        offscreen.addRenderingHints(hints);
        RetinaImageIcon icon = new RetinaImageIcon(onscreenImage);
        JLabel draw = new JLabel(icon);
        draw.addMouseListener(std);
        draw.addMouseMotionListener(std);
        frame.setContentPane(draw);
        frame.pack();
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }

    private static JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem(" Save...   ");
        menuItem1.addActionListener(std);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        menu.add(menuItem1);
        return menuBar;
    }

    private static void validate(double x, String name) {
        if (Double.isNaN(x)) {
            throw new IllegalArgumentException(name + " is NaN");
        }
        if (Double.isInfinite(x)) {
            throw new IllegalArgumentException(name + " is infinite");
        }
    }

    private static void validateNonnegative(double x, String name) {
        if (x < 0.0) {
            throw new IllegalArgumentException(name + " negative");
        }
    }

    private static void validateNotNull(Object x, String name) {
        if (x == null) {
            throw new IllegalArgumentException(name + " is null");
        }
    }

    public static void setTitle(String title) {
        StdDraw.validateNotNull(title, "title");
        frame.setTitle(title);
        windowTitle = title;
    }

    public static void setXscale() {
        StdDraw.setXscale(0.0, 1.0);
    }

    public static void setYscale() {
        StdDraw.setYscale(0.0, 1.0);
    }

    public static void setScale() {
        StdDraw.setXscale();
        StdDraw.setYscale();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setXscale(double min, double max) {
        StdDraw.validate(min, "min");
        StdDraw.validate(max, "max");
        double size = max - min;
        if (size == 0.0) {
            throw new IllegalArgumentException("the min and max are the same");
        }
        Object object = MOUSE_LOCK;
        synchronized (object) {
            xmin = min - 0.0 * size;
            xmax = max + 0.0 * size;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setYscale(double min, double max) {
        StdDraw.validate(min, "min");
        StdDraw.validate(max, "max");
        double size = max - min;
        if (size == 0.0) {
            throw new IllegalArgumentException("the min and max are the same");
        }
        Object object = MOUSE_LOCK;
        synchronized (object) {
            ymin = min - 0.0 * size;
            ymax = max + 0.0 * size;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setScale(double min, double max) {
        StdDraw.validate(min, "min");
        StdDraw.validate(max, "max");
        double size = max - min;
        if (size == 0.0) {
            throw new IllegalArgumentException("the min and max are the same");
        }
        Object object = MOUSE_LOCK;
        synchronized (object) {
            xmin = min - 0.0 * size;
            xmax = max + 0.0 * size;
            ymin = min - 0.0 * size;
            ymax = max + 0.0 * size;
        }
    }

    private static double scaleX(double x) {
        return (double)width * (x - xmin) / (xmax - xmin);
    }

    private static double scaleY(double y) {
        return (double)height * (ymax - y) / (ymax - ymin);
    }

    private static double factorX(double w) {
        return w * (double)width / Math.abs(xmax - xmin);
    }

    private static double factorY(double h) {
        return h * (double)height / Math.abs(ymax - ymin);
    }

    private static double userX(double x) {
        return xmin + x * (xmax - xmin) / (double)width;
    }

    private static double userY(double y) {
        return ymax - y * (ymax - ymin) / (double)height;
    }

    public static void clear() {
        StdDraw.clear(DEFAULT_CLEAR_COLOR);
    }

    public static void clear(Color color) {
        StdDraw.validateNotNull(color, "color");
        offscreen.setColor(color);
        offscreen.fillRect(0, 0, width, height);
        offscreen.setColor(penColor);
        StdDraw.draw();
    }

    public static double getPenRadius() {
        return penRadius;
    }

    public static void setPenRadius() {
        StdDraw.setPenRadius(0.002);
    }

    public static void setPenRadius(double radius) {
        StdDraw.validate(radius, "pen radius");
        StdDraw.validateNonnegative(radius, "pen radius");
        penRadius = radius;
        float scaledPenRadius = (float)(radius * 512.0);
        BasicStroke stroke = new BasicStroke(scaledPenRadius, 1, 1);
        offscreen.setStroke(stroke);
    }

    public static Color getPenColor() {
        return penColor;
    }

    public static void setPenColor() {
        StdDraw.setPenColor(DEFAULT_PEN_COLOR);
    }

    public static void setPenColor(Color color) {
        StdDraw.validateNotNull(color, "color");
        penColor = color;
        offscreen.setColor(penColor);
    }

    public static void setPenColor(int red, int green, int blue) {
        if (red < 0 || red >= 256) {
            throw new IllegalArgumentException("red must be between 0 and 255");
        }
        if (green < 0 || green >= 256) {
            throw new IllegalArgumentException("green must be between 0 and 255");
        }
        if (blue < 0 || blue >= 256) {
            throw new IllegalArgumentException("blue must be between 0 and 255");
        }
        StdDraw.setPenColor(new Color(red, green, blue));
    }

    public static Font getFont() {
        return font;
    }

    public static void setFont() {
        StdDraw.setFont(DEFAULT_FONT);
    }

    public static void setFont(Font font) {
        StdDraw.validateNotNull(font, "font");
        StdDraw.font = font;
    }

    public static void line(double x0, double y0, double x1, double y1) {
        StdDraw.validate(x0, "x0");
        StdDraw.validate(y0, "y0");
        StdDraw.validate(x1, "x1");
        StdDraw.validate(y1, "y1");
        offscreen.draw(new Line2D.Double(StdDraw.scaleX(x0), StdDraw.scaleY(y0), StdDraw.scaleX(x1), StdDraw.scaleY(y1)));
        StdDraw.draw();
    }

    private static void pixel(double x, double y) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        offscreen.fillRect((int)Math.round(StdDraw.scaleX(x)), (int)Math.round(StdDraw.scaleY(y)), 1, 1);
    }

    public static void point(double x, double y) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double r = penRadius;
        float scaledPenRadius = (float)(r * 512.0);
        if (scaledPenRadius <= 1.0f) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.fill(new Ellipse2D.Double(xs - (double)(scaledPenRadius / 2.0f), ys - (double)(scaledPenRadius / 2.0f), scaledPenRadius, scaledPenRadius));
        }
        StdDraw.draw();
    }

    public static void circle(double x, double y, double radius) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(radius, "radius");
        StdDraw.validateNonnegative(radius, "radius");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * radius);
        double hs = StdDraw.factorY(2.0 * radius);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.draw(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void filledCircle(double x, double y, double radius) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(radius, "radius");
        StdDraw.validateNonnegative(radius, "radius");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * radius);
        double hs = StdDraw.factorY(2.0 * radius);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.fill(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(semiMajorAxis, "semimajor axis");
        StdDraw.validate(semiMinorAxis, "semiminor axis");
        StdDraw.validateNonnegative(semiMajorAxis, "semimajor axis");
        StdDraw.validateNonnegative(semiMinorAxis, "semiminor axis");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * semiMajorAxis);
        double hs = StdDraw.factorY(2.0 * semiMinorAxis);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.draw(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(semiMajorAxis, "semimajor axis");
        StdDraw.validate(semiMinorAxis, "semiminor axis");
        StdDraw.validateNonnegative(semiMajorAxis, "semimajor axis");
        StdDraw.validateNonnegative(semiMinorAxis, "semiminor axis");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * semiMajorAxis);
        double hs = StdDraw.factorY(2.0 * semiMinorAxis);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.fill(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void arc(double x, double y, double radius, double angle1, double angle2) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(radius, "arc radius");
        StdDraw.validate(angle1, "angle1");
        StdDraw.validate(angle2, "angle2");
        StdDraw.validateNonnegative(radius, "arc radius");
        while (angle2 < angle1) {
            angle2 += 360.0;
        }
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * radius);
        double hs = StdDraw.factorY(2.0 * radius);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.draw(new Arc2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs, angle1, angle2 - angle1, 0));
        }
        StdDraw.draw();
    }

    public static void square(double x, double y, double halfLength) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(halfLength, "halfLength");
        StdDraw.validateNonnegative(halfLength, "half length");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * halfLength);
        double hs = StdDraw.factorY(2.0 * halfLength);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.draw(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void filledSquare(double x, double y, double halfLength) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(halfLength, "halfLength");
        StdDraw.validateNonnegative(halfLength, "half length");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * halfLength);
        double hs = StdDraw.factorY(2.0 * halfLength);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.fill(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void rectangle(double x, double y, double halfWidth, double halfHeight) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(halfWidth, "halfWidth");
        StdDraw.validate(halfHeight, "halfHeight");
        StdDraw.validateNonnegative(halfWidth, "half width");
        StdDraw.validateNonnegative(halfHeight, "half height");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * halfWidth);
        double hs = StdDraw.factorY(2.0 * halfHeight);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.draw(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(halfWidth, "halfWidth");
        StdDraw.validate(halfHeight, "halfHeight");
        StdDraw.validateNonnegative(halfWidth, "half width");
        StdDraw.validateNonnegative(halfHeight, "half height");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(2.0 * halfWidth);
        double hs = StdDraw.factorY(2.0 * halfHeight);
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.fill(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        StdDraw.draw();
    }

    public static void polygon(double[] x, double[] y) {
        int i;
        StdDraw.validateNotNull(x, "x-coordinate array");
        StdDraw.validateNotNull(y, "y-coordinate array");
        for (i = 0; i < x.length; ++i) {
            StdDraw.validate(x[i], "x[" + i + "]");
        }
        for (i = 0; i < y.length; ++i) {
            StdDraw.validate(y[i], "y[" + i + "]");
        }
        int n1 = x.length;
        int n2 = y.length;
        if (n1 != n2) {
            throw new IllegalArgumentException("arrays must be of the same length");
        }
        int n = n1;
        if (n == 0) {
            return;
        }
        GeneralPath path = new GeneralPath();
        path.moveTo((float)StdDraw.scaleX(x[0]), (float)StdDraw.scaleY(y[0]));
        for (int i2 = 0; i2 < n; ++i2) {
            path.lineTo((float)StdDraw.scaleX(x[i2]), (float)StdDraw.scaleY(y[i2]));
        }
        path.closePath();
        offscreen.draw(path);
        StdDraw.draw();
    }

    public static void filledPolygon(double[] x, double[] y) {
        int i;
        StdDraw.validateNotNull(x, "x-coordinate array");
        StdDraw.validateNotNull(y, "y-coordinate array");
        for (i = 0; i < x.length; ++i) {
            StdDraw.validate(x[i], "x[" + i + "]");
        }
        for (i = 0; i < y.length; ++i) {
            StdDraw.validate(y[i], "y[" + i + "]");
        }
        int n1 = x.length;
        int n2 = y.length;
        if (n1 != n2) {
            throw new IllegalArgumentException("arrays must be of the same length");
        }
        int n = n1;
        if (n == 0) {
            return;
        }
        GeneralPath path = new GeneralPath();
        path.moveTo((float)StdDraw.scaleX(x[0]), (float)StdDraw.scaleY(y[0]));
        for (int i2 = 0; i2 < n; ++i2) {
            path.lineTo((float)StdDraw.scaleX(x[i2]), (float)StdDraw.scaleY(y[i2]));
        }
        path.closePath();
        offscreen.fill(path);
        StdDraw.draw();
    }

    private static Image getImage(String filename) {
        URL url2;
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        ImageIcon icon = new ImageIcon(filename);
        if (icon.getImageLoadStatus() != 8) {
            try {
                url2 = new URL(filename);
                icon = new ImageIcon(url2);
            } catch (MalformedURLException url2) {
                // empty catch block
            }
        }
        if (icon.getImageLoadStatus() != 8 && (url2 = StdDraw.class.getResource(filename)) != null) {
            icon = new ImageIcon(url2);
        }
        if (icon.getImageLoadStatus() != 8) {
            url2 = StdDraw.class.getResource("/" + filename);
            if (url2 == null) {
                throw new IllegalArgumentException("image " + filename + " not found");
            }
            icon = new ImageIcon(url2);
        }
        return icon.getImage();
    }

    public static void picture(double x, double y, String filename) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validateNotNull(filename, "filename");
        Image image = StdDraw.getImage(filename);
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        offscreen.drawImage(image, (int)Math.round(xs - (double)ws / 2.0), (int)Math.round(ys - (double)hs / 2.0), null);
        StdDraw.draw();
    }

    public static void picture(double x, double y, String filename, double degrees) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(degrees, "degrees");
        StdDraw.validateNotNull(filename, "filename");
        Image image = StdDraw.getImage(filename);
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int)Math.round(xs - (double)ws / 2.0), (int)Math.round(ys - (double)hs / 2.0), null);
        offscreen.rotate(Math.toRadians(degrees), xs, ys);
        StdDraw.draw();
    }

    public static void picture(double x, double y, String filename, double scaledWidth, double scaledHeight) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(scaledWidth, "scaled width");
        StdDraw.validate(scaledHeight, "scaled height");
        StdDraw.validateNotNull(filename, "filename");
        StdDraw.validateNonnegative(scaledWidth, "scaled width");
        StdDraw.validateNonnegative(scaledHeight, "scaled height");
        Image image = StdDraw.getImage(filename);
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(scaledWidth);
        double hs = StdDraw.factorY(scaledHeight);
        if (ws < 0.0 || hs < 0.0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        } else {
            offscreen.drawImage(image, (int)Math.round(xs - ws / 2.0), (int)Math.round(ys - hs / 2.0), (int)Math.round(ws), (int)Math.round(hs), null);
        }
        StdDraw.draw();
    }

    public static void picture(double x, double y, String filename, double scaledWidth, double scaledHeight, double degrees) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(scaledWidth, "scaled width");
        StdDraw.validate(scaledHeight, "scaled height");
        StdDraw.validate(degrees, "degrees");
        StdDraw.validateNotNull(filename, "filename");
        StdDraw.validateNonnegative(scaledWidth, "scaled width");
        StdDraw.validateNonnegative(scaledHeight, "scaled height");
        Image image = StdDraw.getImage(filename);
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        double ws = StdDraw.factorX(scaledWidth);
        double hs = StdDraw.factorY(scaledHeight);
        if (ws < 0.0 || hs < 0.0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        if (ws <= 1.0 && hs <= 1.0) {
            StdDraw.pixel(x, y);
        }
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        offscreen.drawImage(image, (int)Math.round(xs - ws / 2.0), (int)Math.round(ys - hs / 2.0), (int)Math.round(ws), (int)Math.round(hs), null);
        offscreen.rotate(Math.toRadians(degrees), xs, ys);
        StdDraw.draw();
    }

    public static void text(double x, double y, String text) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validateNotNull(text, "text");
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float)(xs - (double)ws / 2.0), (float)(ys + (double)hs));
        StdDraw.draw();
    }

    public static void text(double x, double y, String text, double degrees) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validate(degrees, "degrees");
        StdDraw.validateNotNull(text, "text");
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        StdDraw.text(x, y, text);
        offscreen.rotate(Math.toRadians(degrees), xs, ys);
    }

    public static void textLeft(double x, double y, String text) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validateNotNull(text, "text");
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float)xs, (float)(ys + (double)hs));
        StdDraw.draw();
    }

    public static void textRight(double x, double y, String text) {
        StdDraw.validate(x, "x");
        StdDraw.validate(y, "y");
        StdDraw.validateNotNull(text, "text");
        offscreen.setFont(font);
        FontMetrics metrics = offscreen.getFontMetrics();
        double xs = StdDraw.scaleX(x);
        double ys = StdDraw.scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        offscreen.drawString(text, (float)(xs - (double)ws), (float)(ys + (double)hs));
        StdDraw.draw();
    }

    @Deprecated
    public static void show(int t) {
        StdDraw.validateNonnegative(t, "t");
        StdDraw.show();
        StdDraw.pause(t);
        StdDraw.enableDoubleBuffering();
    }

    public static void pause(int t) {
        StdDraw.validateNonnegative(t, "t");
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

    public static void show() {
        onscreen.drawImage((Image)offscreenImage, 0, 0, null);
        frame.repaint();
    }

    private static void draw() {
        if (!defer) {
            StdDraw.show();
        }
    }

    public static void enableDoubleBuffering() {
        defer = true;
    }

    public static void disableDoubleBuffering() {
        defer = false;
    }

    public static void save(String filename) {
        StdDraw.validateNotNull(filename, "filename");
        if (filename.length() == 0) {
            throw new IllegalArgumentException("argument to save() is the empty string");
        }
        File file = new File(filename);
        String suffix = filename.substring(filename.lastIndexOf(46) + 1);
        if (!filename.contains(".") || suffix.length() == 0) {
            System.out.printf("Error: the filename '%s' has no file extension, such as .jpg or .png\n", filename);
            return;
        }
        try {
            if (ImageIO.write((RenderedImage)onscreenImage, suffix, file)) {
                return;
            }
            BufferedImage saveImage = new BufferedImage(2 * width, 2 * height, 1);
            saveImage.createGraphics().drawImage(onscreenImage, 0, 0, Color.WHITE, null);
            if (ImageIO.write((RenderedImage)saveImage, suffix, file)) {
                return;
            }
            System.out.printf("Error: the filetype '%s' is not supported\n", suffix);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog((Frame)frame, "Use a .png or .jpg extension", 1);
        chooser.setVisible(true);
        String selectedDirectory = chooser.getDirectory();
        String selectedFilename = chooser.getFile();
        if (selectedDirectory != null && selectedFilename != null) {
            StdDraw.save(selectedDirectory + selectedFilename);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isMousePressed() {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            return isMousePressed;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public static boolean mousePressed() {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            return isMousePressed;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static double mouseX() {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            return mouseX;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static double mouseY() {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            return mouseY;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            mouseX = StdDraw.userX(e.getX());
            mouseY = StdDraw.userY(e.getY());
            isMousePressed = true;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            isMousePressed = false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            mouseX = StdDraw.userX(e.getX());
            mouseY = StdDraw.userY(e.getY());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        Object object = MOUSE_LOCK;
        synchronized (object) {
            mouseX = StdDraw.userX(e.getX());
            mouseY = StdDraw.userY(e.getY());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasNextKeyTyped() {
        Object object = KEY_LOCK;
        synchronized (object) {
            return !keysTyped.isEmpty();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static char nextKeyTyped() {
        Object object = KEY_LOCK;
        synchronized (object) {
            if (keysTyped.isEmpty()) {
                throw new NoSuchElementException("your program has already processed all keystrokes");
            }
            return keysTyped.remove(keysTyped.size() - 1).charValue();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isKeyPressed(int keycode) {
        Object object = KEY_LOCK;
        synchronized (object) {
            return keysDown.contains(keycode);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        Object object = KEY_LOCK;
        synchronized (object) {
            keysTyped.addFirst(Character.valueOf(e.getKeyChar()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Object object = KEY_LOCK;
        synchronized (object) {
            keysDown.add(e.getKeyCode());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        Object object = KEY_LOCK;
        synchronized (object) {
            keysDown.remove(e.getKeyCode());
        }
    }

    public static void main(String[] args) {
        StdDraw.square(0.2, 0.8, 0.1);
        StdDraw.filledSquare(0.8, 0.8, 0.2);
        StdDraw.circle(0.8, 0.2, 0.2);
        StdDraw.setPenColor(BOOK_RED);
        StdDraw.setPenRadius(0.02);
        StdDraw.arc(0.8, 0.2, 0.1, 200.0, 45.0);
        StdDraw.setPenRadius();
        StdDraw.setPenColor(BOOK_BLUE);
        double[] x = new double[]{0.1, 0.2, 0.3, 0.2};
        double[] y = new double[]{0.2, 0.3, 0.2, 0.1};
        StdDraw.filledPolygon(x, y);
        StdDraw.setPenColor(BLACK);
        StdDraw.text(0.2, 0.5, "black text");
        StdDraw.setPenColor(WHITE);
        StdDraw.text(0.8, 0.8, "white text");
    }

    static {
        windowTitle = DEFAULT_WINDOW_TITLE;
        width = 512;
        height = 512;
        defer = false;
        MOUSE_LOCK = new Object();
        KEY_LOCK = new Object();
        DEFAULT_FONT = new Font("SansSerif", 0, 16);
        std = new StdDraw();
        isMousePressed = false;
        mouseX = 0.0;
        mouseY = 0.0;
        StdDraw.init();
    }

    private static class RetinaImageIcon
    extends ImageIcon {
        public RetinaImageIcon(Image image) {
            super(image);
        }

        @Override
        public int getIconWidth() {
            return super.getIconWidth() / 2;
        }

        @Override
        public int getIconHeight() {
            return super.getIconHeight() / 2;
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.scale(0.5, 0.5);
            super.paintIcon(c, g2, x * 2, y * 2);
            g2.dispose();
        }
    }
}

