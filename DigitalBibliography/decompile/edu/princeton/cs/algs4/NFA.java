/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package edu.princeton.cs.algs4;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedDFS;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class NFA {
    private Digraph graph;
    private String regexp;
    private final int m;

    public NFA(String regexp) {
        this.regexp = regexp;
        this.m = regexp.length();
        Stack<Integer> ops = new Stack<Integer>();
        this.graph = new Digraph(this.m + 1);
        for (int i = 0; i < this.m; ++i) {
            int lp = i;
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '|') {
                ops.push(i);
            } else if (regexp.charAt(i) == ')') {
                int or = (Integer)ops.pop();
                if (regexp.charAt(or) == '|') {
                    lp = (Integer)ops.pop();
                    this.graph.addEdge(lp, or + 1);
                    this.graph.addEdge(or, i);
                } else if (regexp.charAt(or) == '(') {
                    lp = or;
                } else assert (false);
            }
            if (i < this.m - 1 && regexp.charAt(i + 1) == '*') {
                this.graph.addEdge(lp, i + 1);
                this.graph.addEdge(i + 1, lp);
            }
            if (regexp.charAt(i) != '(' && regexp.charAt(i) != '*' && regexp.charAt(i) != ')') continue;
            this.graph.addEdge(i, i + 1);
        }
        if (ops.size() != 0) {
            throw new IllegalArgumentException("Invalid regular expression");
        }
    }

    public boolean recognizes(String txt) {
        DirectedDFS dfs = new DirectedDFS(this.graph, 0);
        Bag<Integer> pc = new Bag<Integer>();
        for (int v = 0; v < this.graph.V(); ++v) {
            if (!dfs.marked(v)) continue;
            pc.add(v);
        }
        for (int i = 0; i < txt.length(); ++i) {
            if (txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')') {
                throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");
            }
            Bag<Integer> match = new Bag<Integer>();
            Iterator iterator = pc.iterator();
            while (iterator.hasNext()) {
                int v = (Integer)iterator.next();
                if (v == this.m || this.regexp.charAt(v) != txt.charAt(i) && this.regexp.charAt(v) != '.') continue;
                match.add(v + 1);
            }
            if (match.isEmpty()) continue;
            dfs = new DirectedDFS(this.graph, match);
            pc = new Bag();
            for (int v = 0; v < this.graph.V(); ++v) {
                if (!dfs.marked(v)) continue;
                pc.add(v);
            }
            if (pc.size() != 0) continue;
            return false;
        }
        Iterator iterator = pc.iterator();
        while (iterator.hasNext()) {
            int v = (Integer)iterator.next();
            if (v != this.m) continue;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFA nfa = new NFA(regexp);
        StdOut.println(nfa.recognizes(txt));
    }
}

