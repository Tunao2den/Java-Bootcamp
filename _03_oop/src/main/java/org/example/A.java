package org.example;

import java.awt.*;

public class A {
    private String id;

    public static void main(String[] args) {
        A a = new A();
        System.out.println(a.toString());
//        a.equals();
//        a.toString();
//        a.hashCode();
    }

    Object o = new Object();
    (A)o = new A();
    o = new Point();

}
