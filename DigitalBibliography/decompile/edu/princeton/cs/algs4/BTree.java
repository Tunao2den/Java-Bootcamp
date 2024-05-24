/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.StdOut;

public class BTree<Key extends Comparable<Key>, Value> {
    private static final int M = 4;
    private Node root = new Node(0);
    private int height;
    private int n;

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.n;
    }

    public int height() {
        return this.height;
    }

    public Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        return this.search(this.root, key, this.height);
    }

    private Value search(Node x, Key key, int ht) {
        Entry[] children = x.children;
        if (ht == 0) {
            for (int j = 0; j < x.m; ++j) {
                if (!this.eq((Comparable)key, children[j].key)) continue;
                return (Value)children[j].val;
            }
        } else {
            for (int j = 0; j < x.m; ++j) {
                if (j + 1 != x.m && !this.less((Comparable)key, children[j + 1].key)) continue;
                return this.search(children[j].next, key, ht - 1);
            }
        }
        return null;
    }

    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("argument key to put() is null");
        }
        Node u = this.insert(this.root, key, val, this.height);
        ++this.n;
        if (u == null) {
            return;
        }
        Node t = new Node(2);
        t.children[0] = new Entry(this.root.children[0].key, null, this.root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        this.root = t;
        ++this.height;
    }

    private Node insert(Node h, Key key, Value val, int ht) {
        int j;
        Entry t = new Entry((Comparable)key, val, null);
        if (ht == 0) {
            for (j = 0; j < h.m && !this.less((Comparable)key, h.children[j].key); ++j) {
            }
        } else {
            for (j = 0; j < h.m; ++j) {
                if (j + 1 != h.m && !this.less((Comparable)key, h.children[j + 1].key)) continue;
                Node u = this.insert(h.children[j++].next, key, val, ht - 1);
                if (u == null) {
                    return null;
                }
                t.key = u.children[0].key;
                t.val = null;
                t.next = u;
                break;
            }
        }
        for (int i = h.m; i > j; --i) {
            h.children[i] = h.children[i - 1];
        }
        h.children[j] = t;
        ++h.m;
        if (h.m < 4) {
            return null;
        }
        return this.split(h);
    }

    private Node split(Node h) {
        Node t = new Node(2);
        h.m = 2;
        for (int j = 0; j < 2; ++j) {
            t.children[j] = h.children[2 + j];
        }
        return t;
    }

    public String toString() {
        return this.toString(this.root, this.height, "") + "\n";
    }

    private String toString(Node h, int ht, String indent) {
        StringBuilder s = new StringBuilder();
        Entry[] children = h.children;
        if (ht == 0) {
            for (int j = 0; j < h.m; ++j) {
                s.append(indent + children[j].key + " " + children[j].val + "\n");
            }
        } else {
            for (int j = 0; j < h.m; ++j) {
                if (j > 0) {
                    s.append(indent + "(" + children[j].key + ")\n");
                }
                s.append(this.toString(children[j].next, ht - 1, indent + "     "));
            }
        }
        return s.toString();
    }

    private boolean less(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }

    public static void main(String[] args) {
        BTree<String, String> st = new BTree<String, String>();
        st.put("www.cs.princeton.edu", "128.112.136.12");
        st.put("www.cs.princeton.edu", "128.112.136.11");
        st.put("www.princeton.edu", "128.112.128.15");
        st.put("www.yale.edu", "130.132.143.21");
        st.put("www.simpsons.com", "209.052.165.60");
        st.put("www.apple.com", "17.112.152.32");
        st.put("www.amazon.com", "207.171.182.16");
        st.put("www.ebay.com", "66.135.192.87");
        st.put("www.cnn.com", "64.236.16.20");
        st.put("www.google.com", "216.239.41.99");
        st.put("www.nytimes.com", "199.239.136.200");
        st.put("www.microsoft.com", "207.126.99.140");
        st.put("www.dell.com", "143.166.224.230");
        st.put("www.slashdot.org", "66.35.250.151");
        st.put("www.espn.com", "199.181.135.201");
        st.put("www.weather.com", "63.111.66.11");
        st.put("www.yahoo.com", "216.109.118.65");
        StdOut.println("cs.princeton.edu:  " + (String)st.get("www.cs.princeton.edu"));
        StdOut.println("hardvardsucks.com: " + (String)st.get("www.harvardsucks.com"));
        StdOut.println("simpsons.com:      " + (String)st.get("www.simpsons.com"));
        StdOut.println("apple.com:         " + (String)st.get("www.apple.com"));
        StdOut.println("ebay.com:          " + (String)st.get("www.ebay.com"));
        StdOut.println("dell.com:          " + (String)st.get("www.dell.com"));
        StdOut.println();
        StdOut.println("size:    " + st.size());
        StdOut.println("height:  " + st.height());
        StdOut.println(st);
        StdOut.println();
    }

    private static final class Node {
        private int m;
        private Entry[] children = new Entry[4];

        private Node(int k) {
            this.m = k;
        }
    }

    private static class Entry {
        private Comparable key;
        private Object val;
        private Node next;

        public Entry(Comparable key, Object val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }
}

