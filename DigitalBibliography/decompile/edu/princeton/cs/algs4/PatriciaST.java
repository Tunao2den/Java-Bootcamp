/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class PatriciaST<Value> {
    private Node head;
    private int count;

    public PatriciaST() {
        this.head.left = this.head = new Node("", null, 0);
        this.head.right = this.head;
        this.count = 0;
    }

    public void put(String key, Value val) {
        Node p;
        if (key == null) {
            throw new IllegalArgumentException("called put(null)");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("invalid key");
        }
        if (val == null) {
            this.delete(key);
        }
        Node x = this.head;
        do {
            p = x;
            x = PatriciaST.safeBitTest(key, x.b) ? x.right : x.left;
        } while (p.b < x.b);
        if (!x.key.equals(key)) {
            int b = PatriciaST.firstDifferingBit(x.key, key);
            x = this.head;
            do {
                p = x;
                x = PatriciaST.safeBitTest(key, x.b) ? x.right : x.left;
            } while (p.b < x.b && x.b < b);
            Node t = new Node(key, val, b);
            if (PatriciaST.safeBitTest(key, b)) {
                t.left = x;
                t.right = t;
            } else {
                t.left = t;
                t.right = x;
            }
            if (PatriciaST.safeBitTest(key, p.b)) {
                p.right = t;
            } else {
                p.left = t;
            }
            ++this.count;
        } else {
            x.val = val;
        }
    }

    public Value get(String key) {
        Node p;
        if (key == null) {
            throw new IllegalArgumentException("called get(null)");
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("invalid key");
        }
        Node x = this.head;
        do {
            p = x;
            x = PatriciaST.safeBitTest(key, x.b) ? x.right : x.left;
        } while (p.b < x.b);
        if (x.key.equals(key)) {
            return x.val;
        }
        return null;
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
            x = PatriciaST.safeBitTest(key, x.b) ? x.right : x.left;
        } while (p.b < x.b);
        if (x.key.equals(key)) {
            Node z;
            Node y = this.head;
            do {
                z = y;
            } while ((y = PatriciaST.safeBitTest(key, y.b) ? y.right : y.left) != x);
            if (x == p) {
                Node c = PatriciaST.safeBitTest(key, x.b) ? x.left : x.right;
                if (PatriciaST.safeBitTest(key, z.b)) {
                    z.right = c;
                } else {
                    z.left = c;
                }
            } else {
                Node c = PatriciaST.safeBitTest(key, p.b) ? p.left : p.right;
                if (PatriciaST.safeBitTest(key, g.b)) {
                    g.right = c;
                } else {
                    g.left = c;
                }
                if (PatriciaST.safeBitTest(key, z.b)) {
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

    public boolean contains(String key) {
        return this.get(key) != null;
    }

    boolean isEmpty() {
        return this.count == 0;
    }

    int size() {
        return this.count;
    }

    public Iterable<String> keys() {
        Queue<String> queue = new Queue<String>();
        if (this.head.left != this.head) {
            this.keys(this.head.left, 0, queue);
        }
        if (this.head.right != this.head) {
            this.keys(this.head.right, 0, queue);
        }
        return queue;
    }

    private void keys(Node x, int b, Queue<String> queue) {
        if (x.b > b) {
            this.keys(x.left, x.b, queue);
            queue.enqueue(x.key);
            this.keys(x.right, x.b, queue);
        }
    }

    private static boolean safeBitTest(String key, int b) {
        if (b < key.length() * 16) {
            return PatriciaST.bitTest(key, b) != 0;
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
        int c1 = PatriciaST.safeCharAt(k1, 0) & 0xFFFFFFFE;
        if (c1 == (c2 = PatriciaST.safeCharAt(k2, 0) & 0xFFFFFFFE)) {
            i = 1;
            while (PatriciaST.safeCharAt(k1, i) == PatriciaST.safeCharAt(k2, i)) {
                ++i;
            }
            c1 = PatriciaST.safeCharAt(k1, i);
            c2 = PatriciaST.safeCharAt(k2, i);
        }
        int b = 0;
        while ((c1 >>> b & 1) == (c2 >>> b & 1)) {
            ++b;
        }
        return i * 16 + b;
    }

    public static void main(String[] args) {
        PatriciaST<Integer> st = new PatriciaST<Integer>();
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
            String[] a = new String[limitItem];
            int[] v = new int[limitItem];
            StdOut.printf("Creating dataset (%d items)...\n", limitItem);
            for (i2 = 0; i2 < limitItem; ++i2) {
                a[i2] = Integer.toString(i2, 16);
                v[i2] = i2;
            }
            StdOut.printf("Shuffling...\n", new Object[0]);
            StdRandom.shuffle(v);
            StdOut.printf("Adding (%d items)...\n", limitItem);
            for (i2 = 0; i2 < limitItem; ++i2) {
                st.put(a[v[i2]], v[i2]);
            }
            int countKeys = 0;
            StdOut.printf("Iterating...\n", new Object[0]);
            for (String key : st.keys()) {
                ++countKeys;
            }
            StdOut.printf("%d items iterated\n", countKeys);
            if (countKeys != limitItem) {
                ok = false;
            }
            if (countKeys != st.size()) {
                ok = false;
            }
            StdOut.printf("Shuffling...\n", new Object[0]);
            StdRandom.shuffle(v);
            int limitDelete = limitItem / 2;
            StdOut.printf("Deleting (%d items)...\n", limitDelete);
            for (int i3 = 0; i3 < limitDelete; ++i3) {
                st.delete(a[v[i3]]);
            }
            countKeys = 0;
            StdOut.printf("Iterating...\n", new Object[0]);
            for (String key : st.keys()) {
                ++countKeys;
            }
            StdOut.printf("%d items iterated\n", countKeys);
            if (countKeys != limitItem - limitDelete) {
                ok = false;
            }
            if (countKeys != st.size()) {
                ok = false;
            }
            int countDelete = 0;
            int countRemain = 0;
            StdOut.printf("Checking...\n", new Object[0]);
            for (i = 0; i < limitItem; ++i) {
                if (i < limitDelete) {
                    if (st.contains(a[v[i]])) continue;
                    ++countDelete;
                    continue;
                }
                int val = (Integer)st.get(a[v[i]]);
                if (val != v[i]) continue;
                ++countRemain;
            }
            StdOut.printf("%d items found and %d (deleted) items missing\n", countRemain, countDelete);
            if (countRemain + countDelete != limitItem) {
                ok = false;
            }
            if (countRemain != st.size()) {
                ok = false;
            }
            if (st.isEmpty()) {
                ok = false;
            }
            StdOut.printf("Deleting the rest (%d items)...\n", limitItem - countDelete);
            for (i = countDelete; i < limitItem; ++i) {
                st.delete(a[v[i]]);
            }
            if (!st.isEmpty()) {
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
        private Value val;
        private int b;

        public Node(String key, Value val, int b) {
            this.key = key;
            this.val = val;
            this.b = b;
        }
    }
}

