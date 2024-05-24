/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.DrawListener;
import edu.princeton.cs.algs4.StdDraw;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class Draw
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
    private static final double BORDER = 0.0;
    private static final double DEFAULT_XMIN = 0.0;
    private static final double DEFAULT_XMAX = 1.0;
    private static final double DEFAULT_YMIN = 0.0;
    private static final double DEFAULT_YMAX = 1.0;
    private static final int DEFAULT_SIZE = 512;
    private static final double DEFAULT_PEN_RADIUS = 0.002;
    private static final Font DEFAULT_FONT = new Font("SansSerif", 0, 16);
    private static final String DEFAULT_WINDOW_TITLE = "Draw";
    private Color penColor;
    private String windowTitle = "Draw";
    private int width = 512;
    private int height = 512;
    private double penRadius;
    private boolean defer = false;
    private double xmin;
    private double ymin;
    private double xmax;
    private double ymax;
    private final Object mouseLock = new Object();
    private final Object keyLock = new Object();
    private Font font;
    private JLabel draw;
    private BufferedImage offscreenImage;
    private BufferedImage onscreenImage;
    private Graphics2D offscreen;
    private Graphics2D onscreen;
    private JFrame frame = new JFrame();
    private boolean isMousePressed = false;
    private double mouseX = 0.0;
    private double mouseY = 0.0;
    private final LinkedList<Character> keysTyped = new LinkedList();
    private final TreeSet<Integer> keysDown = new TreeSet();
    private final ArrayList<DrawListener> listeners = new ArrayList();
    private Timer timer;

    public Draw() {
        this.init();
    }

    private void init() {
        if (this.frame == null) {
            this.frame = new JFrame();
            this.frame.addKeyListener(this);
            this.frame.setFocusTraversalKeysEnabled(false);
            this.frame.setResizable(false);
            this.frame.setDefaultCloseOperation(2);
            this.frame.setTitle(this.windowTitle);
            this.frame.setJMenuBar(this.createMenuBar());
        }
        this.offscreenImage = new BufferedImage(2 * this.width, 2 * this.height, 2);
        this.onscreenImage = new BufferedImage(2 * this.width, 2 * this.height, 2);
        this.offscreen = this.offscreenImage.createGraphics();
        this.onscreen = this.onscreenImage.createGraphics();
        this.offscreen.scale(2.0, 2.0);
        this.setXscale();
        this.setYscale();
        this.offscreen.setColor(DEFAULT_CLEAR_COLOR);
        this.offscreen.fillRect(0, 0, this.width, this.height);
        this.onscreen.setColor(DEFAULT_CLEAR_COLOR);
        this.onscreen.fillRect(0, 0, 2 * this.width, 2 * this.height);
        this.setPenColor();
        this.setPenRadius();
        this.setFont();
        RenderingHints hints = new RenderingHints(null);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        this.offscreen.addRenderingHints(hints);
        RetinaImageIcon icon = new RetinaImageIcon(this.onscreenImage);
        this.draw = new JLabel(icon);
        this.draw.addMouseListener(this);
        this.draw.addMouseMotionListener(this);
        this.frame.setContentPane(this.draw);
        this.frame.pack();
        this.frame.requestFocusInWindow();
        this.frame.setVisible(true);
    }

    public void setVisible(boolean isVisible) {
        this.frame.setVisible(isVisible);
    }

    public void setLocationOnScreen(int x, int y) {
        if (x <= 0 || y <= 0) {
            throw new IllegalArgumentException();
        }
        this.frame.setLocation(x, y);
    }

    public void setDefaultCloseOperation(int value) {
        this.frame.setDefaultCloseOperation(value);
    }

    public void setCanvasSize(int canvasWidth, int canvasHeight) {
        if (canvasWidth < 1 || canvasHeight < 1) {
            throw new IllegalArgumentException("width and height must be positive");
        }
        this.width = canvasWidth;
        this.height = canvasHeight;
        this.init();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        JMenuItem menuItem1 = new JMenuItem(" Save...   ");
        menuItem1.addActionListener(this);
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

    public void setTitle(String windowTitle) {
        Draw.validateNotNull(windowTitle, "title");
        this.windowTitle = windowTitle;
        this.frame.setTitle(windowTitle);
    }

    public void setXscale() {
        this.setXscale(0.0, 1.0);
    }

    public void setYscale() {
        this.setYscale(0.0, 1.0);
    }

    public void setXscale(double min, double max) {
        Draw.validate(min, "min");
        Draw.validate(max, "max");
        double size = max - min;
        if (size == 0.0) {
            throw new IllegalArgumentException("the min and max are the same");
        }
        this.xmin = min - 0.0 * size;
        this.xmax = max + 0.0 * size;
    }

    public void setYscale(double min, double max) {
        Draw.validate(min, "min");
        Draw.validate(max, "max");
        double size = max - min;
        if (size == 0.0) {
            throw new IllegalArgumentException("the min and max are the same");
        }
        this.ymin = min - 0.0 * size;
        this.ymax = max + 0.0 * size;
    }

    public void setScale() {
        this.setXscale();
        this.setYscale();
    }

    public void setScale(double min, double max) {
        this.setXscale(min, max);
        this.setYscale(min, max);
    }

    private double scaleX(double x) {
        return (double)this.width * (x - this.xmin) / (this.xmax - this.xmin);
    }

    private double scaleY(double y) {
        return (double)this.height * (this.ymax - y) / (this.ymax - this.ymin);
    }

    private double factorX(double w) {
        return w * (double)this.width / Math.abs(this.xmax - this.xmin);
    }

    private double factorY(double h) {
        return h * (double)this.height / Math.abs(this.ymax - this.ymin);
    }

    private double userX(double x) {
        return this.xmin + x * (this.xmax - this.xmin) / (double)this.width;
    }

    private double userY(double y) {
        return this.ymax - y * (this.ymax - this.ymin) / (double)this.height;
    }

    public void clear() {
        this.clear(DEFAULT_CLEAR_COLOR);
    }

    public void clear(Color color) {
        Draw.validateNotNull(color, "color");
        this.offscreen.setColor(color);
        this.offscreen.fillRect(0, 0, this.width, this.height);
        this.offscreen.setColor(this.penColor);
        this.draw();
    }

    public double getPenRadius() {
        return this.penRadius;
    }

    public void setPenRadius() {
        this.setPenRadius(0.002);
    }

    public void setPenRadius(double radius) {
        Draw.validate(radius, "pen radius");
        Draw.validateNonnegative(radius, "pen radius");
        this.penRadius = radius * 512.0;
        BasicStroke stroke = new BasicStroke((float)this.penRadius, 1, 1);
        this.offscreen.setStroke(stroke);
    }

    public Color getPenColor() {
        return this.penColor;
    }

    public void setPenColor() {
        this.setPenColor(DEFAULT_PEN_COLOR);
    }

    public void setPenColor(Color color) {
        Draw.validateNotNull(color, "color");
        this.penColor = color;
        this.offscreen.setColor(this.penColor);
    }

    public void setPenColor(int red, int green, int blue) {
        if (red < 0 || red >= 256) {
            throw new IllegalArgumentException("red must be between 0 and 255");
        }
        if (green < 0 || green >= 256) {
            throw new IllegalArgumentException("green must be between 0 and 255");
        }
        if (blue < 0 || blue >= 256) {
            throw new IllegalArgumentException("blue must be between 0 and 255");
        }
        this.setPenColor(new Color(red, green, blue));
    }

    public void xorOn() {
        this.offscreen.setXORMode(DEFAULT_CLEAR_COLOR);
    }

    public void xorOff() {
        this.offscreen.setPaintMode();
    }

    public JLabel getJLabel() {
        return this.draw;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont() {
        this.setFont(DEFAULT_FONT);
    }

    public void setFont(Font font) {
        Draw.validateNotNull(font, "font");
        this.font = font;
    }

    public void line(double x0, double y0, double x1, double y1) {
        Draw.validate(x0, "x0");
        Draw.validate(y0, "y0");
        Draw.validate(x1, "x1");
        Draw.validate(y1, "y1");
        this.offscreen.draw(new Line2D.Double(this.scaleX(x0), this.scaleY(y0), this.scaleX(x1), this.scaleY(y1)));
        this.draw();
    }

    private void pixel(double x, double y) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        this.offscreen.fillRect((int)Math.round(this.scaleX(x)), (int)Math.round(this.scaleY(y)), 1, 1);
    }

    public void point(double x, double y) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double r = this.penRadius;
        if (r <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.fill(new Ellipse2D.Double(xs - r / 2.0, ys - r / 2.0, r, r));
        }
        this.draw();
    }

    public void circle(double x, double y, double radius) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(radius, "radius");
        Draw.validateNonnegative(radius, "radius");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * radius);
        double hs = this.factorY(2.0 * radius);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.draw(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void filledCircle(double x, double y, double radius) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(radius, "radius");
        Draw.validateNonnegative(radius, "radius");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * radius);
        double hs = this.factorY(2.0 * radius);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.fill(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void ellipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(semiMajorAxis, "semimajor axis");
        Draw.validate(semiMinorAxis, "semiminor axis");
        Draw.validateNonnegative(semiMajorAxis, "semimajor axis");
        Draw.validateNonnegative(semiMinorAxis, "semiminor axis");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * semiMajorAxis);
        double hs = this.factorY(2.0 * semiMinorAxis);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.draw(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void filledEllipse(double x, double y, double semiMajorAxis, double semiMinorAxis) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(semiMajorAxis, "semimajor axis");
        Draw.validate(semiMinorAxis, "semiminor axis");
        Draw.validateNonnegative(semiMajorAxis, "semimajor axis");
        Draw.validateNonnegative(semiMinorAxis, "semiminor axis");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * semiMajorAxis);
        double hs = this.factorY(2.0 * semiMinorAxis);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.fill(new Ellipse2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void arc(double x, double y, double radius, double angle1, double angle2) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(radius, "arc radius");
        Draw.validate(angle1, "angle1");
        Draw.validate(angle2, "angle2");
        Draw.validateNonnegative(radius, "arc radius");
        while (angle2 < angle1) {
            angle2 += 360.0;
        }
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * radius);
        double hs = this.factorY(2.0 * radius);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.draw(new Arc2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs, angle1, angle2 - angle1, 0));
        }
        this.draw();
    }

    public void square(double x, double y, double halfLength) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(halfLength, "halfLength");
        Draw.validateNonnegative(halfLength, "half length");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * halfLength);
        double hs = this.factorY(2.0 * halfLength);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.draw(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void filledSquare(double x, double y, double halfLength) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(halfLength, "halfLength");
        Draw.validateNonnegative(halfLength, "half length");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * halfLength);
        double hs = this.factorY(2.0 * halfLength);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.fill(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void rectangle(double x, double y, double halfWidth, double halfHeight) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(halfWidth, "halfWidth");
        Draw.validate(halfHeight, "halfHeight");
        Draw.validateNonnegative(halfWidth, "half width");
        Draw.validateNonnegative(halfHeight, "half height");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * halfWidth);
        double hs = this.factorY(2.0 * halfHeight);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.draw(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void filledRectangle(double x, double y, double halfWidth, double halfHeight) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(halfWidth, "halfWidth");
        Draw.validate(halfHeight, "halfHeight");
        Draw.validateNonnegative(halfWidth, "half width");
        Draw.validateNonnegative(halfHeight, "half height");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(2.0 * halfWidth);
        double hs = this.factorY(2.0 * halfHeight);
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.fill(new Rectangle2D.Double(xs - ws / 2.0, ys - hs / 2.0, ws, hs));
        }
        this.draw();
    }

    public void polygon(double[] x, double[] y) {
        int i;
        Draw.validateNotNull(x, "x-coordinate array");
        Draw.validateNotNull(y, "y-coordinate array");
        for (i = 0; i < x.length; ++i) {
            Draw.validate(x[i], "x[" + i + "]");
        }
        for (i = 0; i < y.length; ++i) {
            Draw.validate(y[i], "y[" + i + "]");
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
        path.moveTo((float)this.scaleX(x[0]), (float)this.scaleY(y[0]));
        for (int i2 = 0; i2 < n; ++i2) {
            path.lineTo((float)this.scaleX(x[i2]), (float)this.scaleY(y[i2]));
        }
        path.closePath();
        this.offscreen.draw(path);
        this.draw();
    }

    public void filledPolygon(double[] x, double[] y) {
        int i;
        Draw.validateNotNull(x, "x-coordinate array");
        Draw.validateNotNull(y, "y-coordinate array");
        for (i = 0; i < x.length; ++i) {
            Draw.validate(x[i], "x[" + i + "]");
        }
        for (i = 0; i < y.length; ++i) {
            Draw.validate(y[i], "y[" + i + "]");
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
        path.moveTo((float)this.scaleX(x[0]), (float)this.scaleY(y[0]));
        for (int i2 = 0; i2 < n; ++i2) {
            path.lineTo((float)this.scaleX(x[i2]), (float)this.scaleY(y[i2]));
        }
        path.closePath();
        this.offscreen.fill(path);
        this.draw();
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
            url2 = Draw.class.getResource("/" + filename);
            if (url2 == null) {
                throw new IllegalArgumentException("image " + filename + " not found");
            }
            icon = new ImageIcon(url2);
        }
        return icon.getImage();
    }

    public void picture(double x, double y, String filename) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validateNotNull(filename, "filename");
        Image image = Draw.getImage(filename);
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        this.offscreen.drawImage(image, (int)Math.round(xs - (double)ws / 2.0), (int)Math.round(ys - (double)hs / 2.0), null);
        this.draw();
    }

    public void picture(double x, double y, String filename, double degrees) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(degrees, "degrees");
        Draw.validateNotNull(filename, "filename");
        Image image = Draw.getImage(filename);
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        if (ws < 0 || hs < 0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        this.offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        this.offscreen.drawImage(image, (int)Math.round(xs - (double)ws / 2.0), (int)Math.round(ys - (double)hs / 2.0), null);
        this.offscreen.rotate(Math.toRadians(degrees), xs, ys);
        this.draw();
    }

    public void picture(double x, double y, String filename, double scaledWidth, double scaledHeight) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(scaledWidth, "scaled width");
        Draw.validate(scaledHeight, "scaled height");
        Draw.validateNotNull(filename, "filename");
        Draw.validateNonnegative(scaledWidth, "scaled width");
        Draw.validateNonnegative(scaledHeight, "scaled height");
        Image image = Draw.getImage(filename);
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(scaledWidth);
        double hs = this.factorY(scaledHeight);
        if (ws < 0.0 || hs < 0.0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        } else {
            this.offscreen.drawImage(image, (int)Math.round(xs - ws / 2.0), (int)Math.round(ys - hs / 2.0), (int)Math.round(ws), (int)Math.round(hs), null);
        }
        this.draw();
    }

    public void picture(double x, double y, String filename, double scaledWidth, double scaledHeight, double degrees) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(scaledWidth, "scaled width");
        Draw.validate(scaledHeight, "scaled height");
        Draw.validate(degrees, "degrees");
        Draw.validateNotNull(filename, "filename");
        Draw.validateNonnegative(scaledWidth, "scaled width");
        Draw.validateNonnegative(scaledHeight, "scaled height");
        Image image = Draw.getImage(filename);
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        double ws = this.factorX(scaledWidth);
        double hs = this.factorY(scaledHeight);
        if (ws < 0.0 || hs < 0.0) {
            throw new IllegalArgumentException("image " + filename + " is corrupt");
        }
        if (ws <= 1.0 && hs <= 1.0) {
            this.pixel(x, y);
        }
        this.offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        this.offscreen.drawImage(image, (int)Math.round(xs - ws / 2.0), (int)Math.round(ys - hs / 2.0), (int)Math.round(ws), (int)Math.round(hs), null);
        this.offscreen.rotate(Math.toRadians(degrees), xs, ys);
        this.draw();
    }

    public void text(double x, double y, String text) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validateNotNull(text, "text");
        this.offscreen.setFont(this.font);
        FontMetrics metrics = this.offscreen.getFontMetrics();
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        this.offscreen.drawString(text, (float)(xs - (double)ws / 2.0), (float)(ys + (double)hs));
        this.draw();
    }

    public void text(double x, double y, String text, double degrees) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validate(degrees, "degrees");
        Draw.validateNotNull(text, "text");
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        this.offscreen.rotate(Math.toRadians(-degrees), xs, ys);
        this.text(x, y, text);
        this.offscreen.rotate(Math.toRadians(degrees), xs, ys);
    }

    public void textLeft(double x, double y, String text) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validateNotNull(text, "text");
        this.offscreen.setFont(this.font);
        FontMetrics metrics = this.offscreen.getFontMetrics();
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        int hs = metrics.getDescent();
        this.offscreen.drawString(text, (float)xs, (float)(ys + (double)hs));
        this.draw();
    }

    public void textRight(double x, double y, String text) {
        Draw.validate(x, "x");
        Draw.validate(y, "y");
        Draw.validateNotNull(text, "text");
        this.offscreen.setFont(this.font);
        FontMetrics metrics = this.offscreen.getFontMetrics();
        double xs = this.scaleX(x);
        double ys = this.scaleY(y);
        int ws = metrics.stringWidth(text);
        int hs = metrics.getDescent();
        this.offscreen.drawString(text, (float)(xs - (double)ws), (float)(ys + (double)hs));
        this.draw();
    }

    @Deprecated
    public void show(int t) {
        this.show();
        this.pause(t);
        this.enableDoubleBuffering();
    }

    public void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

    public void show() {
        this.onscreen.drawImage((Image)this.offscreenImage, 0, 0, null);
        this.frame.repaint();
    }

    private void draw() {
        if (!this.defer) {
            this.show();
        }
    }

    public void enableDoubleBuffering() {
        this.defer = true;
    }

    public void disableDoubleBuffering() {
        this.defer = false;
    }

    public void save(String filename) {
        Draw.validateNotNull(filename, "filename");
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
            if (ImageIO.write((RenderedImage)this.onscreenImage, suffix, file)) {
                return;
            }
            BufferedImage saveImage = new BufferedImage(2 * this.width, 2 * this.height, 1);
            saveImage.createGraphics().drawImage(this.onscreenImage, 0, 0, Color.WHITE, null);
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
        FileDialog chooser = new FileDialog((Frame)this.frame, "Use a .png or .jpg extension", 1);
        chooser.setVisible(true);
        String selectedDirectory = chooser.getDirectory();
        String selectedFilename = chooser.getFile();
        if (selectedDirectory != null && selectedFilename != null) {
            StdDraw.save(selectedDirectory + selectedFilename);
        }
    }

    public void addListener(DrawListener listener) {
        this.show();
        this.listeners.add(listener);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isMousePressed() {
        Object object = this.mouseLock;
        synchronized (object) {
            return this.isMousePressed;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public boolean mousePressed() {
        Object object = this.mouseLock;
        synchronized (object) {
            return this.isMousePressed;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public double mouseX() {
        Object object = this.mouseLock;
        synchronized (object) {
            return this.mouseX;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public double mouseY() {
        Object object = this.mouseLock;
        synchronized (object) {
            return this.mouseY;
        }
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
        Iterator<DrawListener> iterator = this.mouseLock;
        synchronized (iterator) {
            this.mouseX = this.userX(e.getX());
            this.mouseY = this.userY(e.getY());
            this.isMousePressed = true;
        }
        if (e.getButton() == 1) {
            for (DrawListener listener : this.listeners) {
                listener.mousePressed(this.userX(e.getX()), this.userY(e.getY()));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        Iterator<DrawListener> iterator = this.mouseLock;
        synchronized (iterator) {
            this.isMousePressed = false;
        }
        if (e.getButton() == 1) {
            for (DrawListener listener : this.listeners) {
                listener.mouseReleased(this.userX(e.getX()), this.userY(e.getY()));
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1) {
            for (DrawListener listener : this.listeners) {
                listener.mouseClicked(this.userX(e.getX()), this.userY(e.getY()));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Iterator<DrawListener> iterator = this.mouseLock;
        synchronized (iterator) {
            this.mouseX = this.userX(e.getX());
            this.mouseY = this.userY(e.getY());
        }
        for (DrawListener listener : this.listeners) {
            listener.mouseDragged(this.userX(e.getX()), this.userY(e.getY()));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        Object object = this.mouseLock;
        synchronized (object) {
            this.mouseX = this.userX(e.getX());
            this.mouseY = this.userY(e.getY());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean hasNextKeyTyped() {
        Object object = this.keyLock;
        synchronized (object) {
            return !this.keysTyped.isEmpty();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public char nextKeyTyped() {
        Object object = this.keyLock;
        synchronized (object) {
            return this.keysTyped.removeLast().charValue();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isKeyPressed(int keycode) {
        Object object = this.keyLock;
        synchronized (object) {
            return this.keysDown.contains(keycode);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        Iterator<DrawListener> iterator = this.keyLock;
        synchronized (iterator) {
            this.keysTyped.addFirst(Character.valueOf(e.getKeyChar()));
        }
        for (DrawListener listener : this.listeners) {
            listener.keyTyped(e.getKeyChar());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        Iterator<DrawListener> iterator = this.keyLock;
        synchronized (iterator) {
            this.keysDown.add(e.getKeyCode());
        }
        for (DrawListener listener : this.listeners) {
            listener.keyPressed(e.getKeyCode());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        Iterator<DrawListener> iterator = this.keyLock;
        synchronized (iterator) {
            this.keysDown.remove(e.getKeyCode());
        }
        for (DrawListener listener : this.listeners) {
            listener.keyReleased(e.getKeyCode());
        }
    }

    public void enableTimer(int callsPerSecond) {
        this.disableTimer();
        this.timer = new Timer();
        this.timer.schedule((TimerTask)new MyTimerTask(), 0L, (long)((int)Math.round(1000.0 / (double)callsPerSecond)));
    }

    public void disableTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    public static void main(String[] args) {
        Draw draw1 = new Draw();
        draw1.setTitle("Test client 1");
        draw1.square(0.2, 0.8, 0.1);
        draw1.filledSquare(0.8, 0.8, 0.2);
        draw1.circle(0.8, 0.2, 0.2);
        draw1.setPenColor(MAGENTA);
        draw1.setPenRadius(0.02);
        draw1.arc(0.8, 0.2, 0.1, 200.0, 45.0);
        Draw draw2 = new Draw();
        draw2.setCanvasSize(900, 200);
        draw2.setTitle("Test client 2");
        draw2.setPenRadius();
        draw2.setPenColor(BLUE);
        double[] x = new double[]{0.1, 0.2, 0.3, 0.2};
        double[] y = new double[]{0.2, 0.3, 0.2, 0.1};
        draw2.filledPolygon(x, y);
        draw2.setPenColor(BLACK);
        draw2.text(0.2, 0.5, "bdfdfdfdlack text");
        draw2.setPenColor(WHITE);
        draw2.text(0.8, 0.8, "white text");
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

    private class MyTimerTask
    extends TimerTask {
        private MyTimerTask() {
        }

        @Override
        public void run() {
            for (DrawListener listener : Draw.this.listeners) {
                listener.update();
            }
        }
    }
}

