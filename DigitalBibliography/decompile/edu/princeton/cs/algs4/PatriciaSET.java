/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class PatriciaSET
implements Iterable<String> {
    private Node head;
    private int count;

    public PatriciaSET() {
        this.head.left = this.head = new Node("", 0);
        this.head.right = this.head;
        this.count = 0;
    }

    public void add(String key) {
        Node p;
        if (key == null) {
            throw new IllegalArgumentException("called add(null)");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("invalid key");
        }
        Node x = this.head;
        do {
            p = x;
            x = PatriciaSET.safeBitTest(key, x.b) ? x.right : x.left;
        } while (p.b < x.b);
        if (!x.key.equals(key)) {
            int b = PatriciaSET.firstDifferingBit(x.key, key);
            x = this.head;
            do {
                p = x;
                x = PatriciaSET.safeBitTest(key, x.b) ? x.right : x.left;
            } while (p.b < x.b && x.b < b);
            Node t = new Node(key, b);
            if (PatriciaSET.safeBitTest(key, b)) {
                t.left = x;
                t.right = t;
            } else {
                t.left = t;
                t.right = x;
            }
            if (PatriciaSET.safeBitTest(key, p.b)) {
                p.right = t;
            } else {
                p.left = t;
            }
            ++this.count;
        }
    }

    public boolean contains(String key) {
        Node p;
        if (key == null) {
            throw new IllegalArgumentException("called contains(null)");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("invalid key");
        }
        Node x = this.head;
        do {
            p = x;
            x = PatriciaSET.safeBitTest(key, x.b) ? x.right : x.left;
        } while (p.b < x.b);
        return x.key.equals(key);
    }

    public void delete(String key) {
        Node g;
        if (key == null) {
            throw new IllegalArgumentException("called delete(null)");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("invalid key");
        }
        Node p = this.head;
        Node x = this.head;
        do {
            g = p;
            p = x;
            x = PatriciaSET.safeBitTest(key, x.b) ? x.right : x.left;
        } while (p.b < x.b);
        if (x.key.equals(key)) {
            Node z;
            Node y = this.head;
            do {
                z = y;
            } while ((y = PatriciaSET.safeBitTest(key, y.b) ? y.right : y.left) != x);
            if (x == p) {
                Node c = PatriciaSET.safeBitTest(key, x.b) ? x.left : x.right;
                if (PatriciaSET.safeBitTest(key, z.b)) {
                    z.right = c;
                } else {
                    z.left = c;
                }
            } else {
                Node c = PatriciaSET.safeBitTest(key, p.b) ? p.left : p.right;
                if (PatriciaSET.safeBitTest(key, g.b)) {
                    g.right = c;
                } else {
                    g.left = c;
                }
                if (PatriciaSET.safeBitTest(key, z.b)) {
                    z.right = p;
                } else {
                    z.left = p;
                }
                p.left = x.left;
                p.right = x.right;
                p.b = x.b;
            }
            --this.count;
        }
    }

    boolean isEmpty() {
        return this.count == 0;
    }

    int size() {
        return this.count;
    }

    @Override
    public Iterator<String> iterator() {
        Queue<String> queue = new Queue<String>();
        if (this.head.left != this.head) {
            this.collect(this.head.left, 0, queue);
        }
        if (this.head.right != this.head) {
            this.collect(this.head.right, 0, queue);
        }
        return queue.iterator();
    }

    private void collect(Node x, int b, Queue<String> queue) {
        if (x.b > b) {
            this.collect(x.left, x.b, queue);
            queue.enqueue(x.key);
            this.collect(x.right, x.b, queue);
        }
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String key : this) {
            s.append(key + " ");
        }
        if (s.length() > 0) {
            s.deleteCharAt(s.length() - 1);
        }
        return s.toString();
    }

    private static boolean safeBitTest(String key, int b) {
        if (b < key.length() * 16) {
            return PatriciaSET.bitTest(key, b) != 0;
        }
        return b <= key.length() * 16 + 15;
    }

    private static int bitTest(String key, int b) {
        return key.charAt(b >>> 4) >>> (b & 0xF) & 1;
    }

    private static int safeCharAt(String key, int i) {
        if (i < key.length()) {
            return key.charAt(i);
        }
        if (i > key.length()) {
            return 0;
        }
        return 65535;
    }

    private static int firstDifferingBit(String k1, String k2) {
        int c2;
        int i = 0;
        int c1 = PatriciaSET.safeCharAt(k1, 0) & 0xFFFFFFFE;
        if (c1 == (c2 = PatriciaSET.safeCharAt(k2, 0) & 0xFFFFFFFE)) {
            i = 1;
            while (PatriciaSET.safeCharAt(k1, i) == PatriciaSET.safeCharAt(k2, i)) {
                ++i;
            }
            c1 = PatriciaSET.safeCharAt(k1, i);
            c2 = PatriciaSET.safeCharAt(k2, i);
        }
        int b = 0;
        while ((c1 >>> b & 1) == (c2 >>> b & 1)) {
            ++b;
        }
        return i * 16 + b;
    }

    public static void main(String[] args) {
        PatriciaSET set = new PatriciaSET();
        int limitItem = 1000000;
        int limitPass = 1;
        int countPass = 0;
        boolean ok = true;
        if (args.length > 0) {
            limitItem = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            limitPass = Integer.parseInt(args[1]);
        }
        do {
            int i;
            int i2;
            Object[] a = new String[limitItem];
            StdOut.printf("Creating dataset (%d items)...\n", limitItem);
            for (i2 = 0; i2 < limitItem; ++i2) {
                a[i2] = Integer.toString(i2, 16);
            }
            StdOut.printf("Shuffling...\n", new Object[0]);
            StdRandom.shuffle(a);
            StdOut.printf("Adding (%d items)...\n", limitItem);
            for (i2 = 0; i2 < limitItem; ++i2) {
                set.add((String)a[i2]);
            }
            int countItems = 0;
            StdOut.printf("Iterating...\n", new Object[0]);
            for (String key : set) {
                ++countItems;
            }
            StdOut.printf("%d items iterated\n", countItems);
            if (countItems != limitItem) {
                ok = false;
            }
            if (countItems != set.size()) {
                ok = false;
            }
            StdOut.printf("Shuffling...\n", new Object[0]);
            StdRandom.shuffle(a);
            int limitDelete = limitItem / 2;
            StdOut.printf("Deleting (%d items)...\n", limitDelete);
            for (int i3 = 0; i3 < limitDelete; ++i3) {
                set.delete((String)a[i3]);
            }
            countItems = 0;
            StdOut.printf("Iterating...\n", new Object[0]);
            for (String key : set) {
                ++countItems;
            }
            StdOut.printf("%d items iterated\n", countItems);
            if (countItems != limitItem - limitDelete) {
                ok = false;
            }
            if (countItems != set.size()) {
                ok = false;
            }
            int countDelete = 0;
            int countRemain = 0;
            StdOut.printf("Checking...\n", new Object[0]);
            for (i = 0; i < limitItem; ++i) {
                if (i < limitDelete) {
                    if (set.contains((String)a[i])) continue;
                    ++countDelete;
                    continue;
                }
                if (!set.contains((String)a[i])) continue;
                ++countRemain;
            }
            StdOut.printf("%d items found and %d (deleted) items missing\n", countRemain, countDelete);
            if (countRemain + countDelete != limitItem) {
                ok = false;
            }
            if (countRemain != set.size()) {
                ok = false;
            }
            if (set.isEmpty()) {
                ok = false;
            }
            StdOut.printf("Deleting the rest (%d items)...\n", limitItem - countDelete);
            for (i = countDelete; i < limitItem; ++i) {
                set.delete((String)a[i]);
            }
            if (!set.isEmpty()) {
                ok = false;
            }
            ++countPass;
            if (ok) {
                StdOut.printf("PASS %d TESTS SUCCEEDED\n", countPass);
                continue;
            }
            StdOut.printf("PASS %d TESTS FAILED\n", countPass);
        } while (ok && countPass < limitPass);
        if (!ok) {
            throw new RuntimeException("TESTS FAILED");
        }
    }

    private class Node {
        private Node left;
        private Node right;
        private String key;
        private int b;

        public Node(String key, int b) {
            this.key = key;
            this.b = b;
        }
    }
}

