package com.github.snkotv.cgc.gui.primitives;

import java.awt.*;

public class Point implements DrawableObject {
    private double x, y;
    private double r;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        r = 2;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public void onRender(Graphics g) {
        g.fillOval((int)(x - r), (int)(y - r), 2 * (int)r,2 * (int)r);
    }
}
