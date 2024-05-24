/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.MinPQ;

public class Huffman {
    private static final int R = 256;

    private Huffman() {
    }

    public static void compress() {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        int[] freq = new int[256];
        for (int i = 0; i < input.length; ++i) {
            char c = input[i];
            freq[c] = freq[c] + 1;
        }
        Node root = Huffman.buildTrie(freq);
        String[] st = new String[256];
        Huffman.buildCode(st, root, "");
        Huffman.writeTrie(root);
        BinaryStdOut.write(input.length);
        for (int i = 0; i < input.length; ++i) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); ++j) {
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                    continue;
                }
                if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                    continue;
                }
                throw new IllegalStateException("Illegal state");
            }
        }
        BinaryStdOut.close();
    }

    private static Node buildTrie(int[] freq) {
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char c = '\u0000'; c < '\u0100'; c = (char)((char)(c + 1))) {
            if (freq[c] <= 0) continue;
            pq.insert(new Node(c, freq[c], null, null));
        }
        while (pq.size() > 1) {
            Node left = (Node)pq.delMin();
            Node right = (Node)pq.delMin();
            Node parent = new Node('\u0000', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return (Node)pq.delMin();
    }

    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);
        Huffman.writeTrie(x.left);
        Huffman.writeTrie(x.right);
    }

    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            Huffman.buildCode(st, x.left, s + "0");
            Huffman.buildCode(st, x.right, s + "1");
        } else {
            st[x.ch] = s;
        }
    }

    public static void expand() {
        Node root = Huffman.readTrie();
        int length = BinaryStdIn.readInt();
        for (int i = 0; i < length; ++i) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) {
                    x = x.right;
                    continue;
                }
                x = x.left;
            }
            BinaryStdOut.write(x.ch, 8);
        }
        BinaryStdOut.close();
    }

    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new Node(BinaryStdIn.readChar(), -1, null, null);
        }
        return new Node('\u0000', -1, Huffman.readTrie(), Huffman.readTrie());
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            Huffman.compress();
        } else if (args[0].equals("+")) {
            Huffman.expand();
        } else {
            throw new IllegalArgumentException("Illegal command line argument");
        }
    }

    private static class Node
    implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left;
        private final Node right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert (this.left == null && this.right == null || this.left != null && this.right != null);
            return this.left == null && this.right == null;
        }

        @Override
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }
}

