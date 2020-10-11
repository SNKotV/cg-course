package com.github.snkotv.cgc.labs;

import com.github.snkotv.cgc.gui.primitives.Letter;
import com.github.snkotv.cgc.gui.primitives.LineSegment;
import com.github.snkotv.cgc.gui.primitives.Point;
import com.github.snkotv.cgc.gui.window.Window;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Lab1 {
    private static ArrayList<Point> points = new ArrayList<>(3);
    private static ArrayList<Double> ABVector = new ArrayList<>(2);
    private static ArrayList<Double> ACVector = new ArrayList<>(2);
    private static Window win = new Window("Lab1", 1680, 1020);


    private static synchronized void addPoint(int x, int y) {
        if (points.size() == 3) {
            return;
        }

        points.add(new Point(x, y));
        win.getDrawingArea().getDrawableObjects().add(points.get(points.size() - 1));
        win.getDrawingArea().getDrawableObjects().add(new Letter("" + (char)('A' + points.size() - 1), x - 10, y - 10));

        double xd = (x - win.getDrawingArea().getOriginX()) / (double)(win.getDrawingArea().getStepSize());
        double yd = (win.getDrawingArea().getOriginY() - y) / (double)(win.getDrawingArea().getStepSize());
        win.getLog().printMessage("" + (char)('A' + points.size() - 1) + " (" + (int)(xd * 100) / 100.0
                + ", " + (int)(yd * 100) / 100.0 + ") ");

        if (points.size() > 1) {
            win.getDrawingArea().getDrawableObjects().add(new LineSegment(points.get(0), points.get(points.size() - 1)));

            double x0 = (points.get(0).getX() - win.getDrawingArea().getOriginX()) / (double)(win.getDrawingArea().getStepSize());
            x0 = (int)(x0 * 100) / 100.0;
            double y0 = (points.get(0).getY() - win.getDrawingArea().getOriginX()) / (double)(win.getDrawingArea().getStepSize());
            y0 = (int)(y0 * 100) / 100.0;
            double x1 = (points.get(points.size() - 1).getX() - win.getDrawingArea().getOriginX()) / (double)(win.getDrawingArea().getStepSize());
            x1 = (int)(x1 * 100) / 100.0;
            double y1 = (points.get(points.size() - 1).getY() - win.getDrawingArea().getOriginX()) / (double)(win.getDrawingArea().getStepSize());
            y1 = (int)(y1 * 100) / 100.0;

            double vx = x1 - x0, vy = y0 - y1;
            vx = (int)(vx * 100) / 100.0;
            vy = (int)(vy * 100) / 100.0;
            
            if (ABVector.isEmpty()) {
                ABVector.add(vx);
                ABVector.add(vy);
            } else {
                ACVector.add(vx);
                ACVector.add(vy);
            }

            win.getLog().printMessage("A" + (char)('A' + points.size() - 1) + " (" + vx
                    + ", " + vy + ")");
        }

        if (points.size() == 3) {
            calculate();
        }
    }

    private static void calculate() {
        win.getLog().printMessage("");
        
        double x0 = ABVector.get(0), y0 = ABVector.get(1);
        double x1 = ACVector.get(0), y1 = ACVector.get(1);

        double ABLength = Math.sqrt(x0 * x0 + y0 * y0);
        double ACLength = Math.sqrt(x1 * x1 + y1 * y1);
        
        double dotProduct = x0 * x1 + y0 * y1;
        double angle = Math.acos(dotProduct / ABLength / ACLength);
        angle = (int)(angle * 100) /  100.0;

        win.getLog().printMessage("Angle between AB and AC = " + angle + " rad");
        if (Math.abs(angle - (int)(Math.PI * 100) / 100.0) < 1e-12) {
            win.getLog().printMessage("[Straight angle]");
        } else if (Math.abs(angle - (int)(Math.PI * 100) / 200.0) < 1e-12) {
            win.getLog().printMessage("[Right angle]");
        } else if (angle < (int)(Math.PI * 100) / 200.0) {
            win.getLog().printMessage("[Acute angle]");
        } else {
            win.getLog().printMessage("[Obtuse angle]");
        }

        win.getLog().printMessage("");

        double zCrossProduct = x0 * y1 - y0 * x1;

        if (Math.abs(zCrossProduct) < 1e-12) {
            win.getLog().printMessage("AB and AC are collinear");
        } else if (zCrossProduct > 0) {
            win.getLog().printMessage("A rotation from AB to AC goes counterclockwise");
        } else {
            win.getLog().printMessage("A rotation from AB to AC goes clockwise");
        }
    }

    public static void main(String[] args) {
        win.getDrawingArea().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                addPoint(e.getX(), e.getY());
            }
        });

        win.getDrawingArea().render();
    }
}
