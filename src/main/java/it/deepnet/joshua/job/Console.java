package it.deepnet.joshua.job;

import javax.swing.*;
//import java.awt.event.*;

public class Console {
    public static String title(Object o) {
        String t = o.getClass().toString();
        if (t.indexOf("class") != -1)
            t = t.substring(6);
        return t;
    }

    public static void setupClosing(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void setupHide(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public static void setupDispose(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void run(JFrame frame, int width, int height) {
        setupClosing(frame);
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void run(JApplet applet, int width, int height) {
        JFrame frame = new JFrame(title(applet));
        setupClosing(frame);
        frame.getContentPane().add(applet);
        frame.setSize(width, height);
        applet.init();
        applet.start();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static void run(JPanel panel, int width, int height) {
        JFrame frame = new JFrame(title(panel));
        setupClosing(frame);
        frame.getContentPane().add(panel);
        frame.setSize(width, height);
        frame.setVisible(true);
    }
}