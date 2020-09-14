package com.github.snkotv.cgc.gui.primitives;

import java.awt.*;

public class LineSegment implements DrawableObject {
    private Point A, B;

    public LineSegment(Point A, Point B) {
        this.A = A;
        this.B = B;
    }

    public Point getA() {
        return A;
    }

    public void setA(Point a) {
        A = a;
    }

    public Point getB() {
        return B;
    }

    public void setB(Point b) {
        B = b;
    }

    @Override
    public void onRender(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        g2d.drawLine((int)A.getX(), (int)A.getY(), (int)B.getX(), (int)B.getY());
    }
}
