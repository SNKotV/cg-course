package com.github.snkotv.cgc.geom.utils;

import com.github.snkotv.cgc.gui.primitives.LineSegment;
import com.github.snkotv.cgc.gui.primitives.Point;

public class Vector {

    private double x, y, z;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(double x, double y) {
        this(x,y,0);
    }

    public Vector(Point p) {
        x = p.getX();
        y = p.getY();
        z = 0;
    }

    public Vector(LineSegment ls) {
        x = ls.getB().getX() - ls.getA().getX();
        y = ls.getB().getY() - ls.getA().getY();
        z = 0;
    }

    public static double dotProduct(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vector crossProduct(Vector v1, Vector v2) {
        double xr = v1.y * v2.z - v1.z * v2.y;
        double yr = v1.x * v2.z - v1.z * v2.x;
        double zr = v1.x * v2.y - v1.y * v2.x;
        return new Vector(xr, yr, zr);
    }
}
