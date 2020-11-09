package com.github.snkotv.cgc.labs;

import com.github.snkotv.cgc.geom.utils.Vector;
import com.github.snkotv.cgc.gui.primitives.DrawableObject;
import com.github.snkotv.cgc.gui.primitives.Letter;
import com.github.snkotv.cgc.gui.primitives.LineSegment;
import com.github.snkotv.cgc.gui.primitives.Point;
import com.github.snkotv.cgc.gui.window.PointInsertionForm;
import com.github.snkotv.cgc.gui.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Lab4 implements Lab{
    private Window win = new Window("Lab4", 1680, 1020);
    private boolean isSelfIntersect = false;
    private boolean isPolygonFormed = false;
    private boolean isConvex = true;
    private int rotationDirection = -1; // -1 - undefined, 0 - counterClockwise, 1 - clockwise
    private double distEps = 1e-1;


    private synchronized void applyAction(int x, int y)    {
        if (win.getDrawingArea().getDrawableObjects().isEmpty()) {
            isPolygonFormed = false;
            isSelfIntersect = false;
            isPolygonFormed = false;
            isConvex = true;
            rotationDirection = -1;
        }

//        if (isSelfIntersect) {
//            win.getLog().printMessage("Can't apply algorithm to self intersecting polygon");
//            win.getDrawingArea().getDrawableObjects().clear();
//            return;
//        }

        if (isPolygonFormed) {
            Point p = new Point(x, y, 5);

            if (isInside(p)) {
                p.setColor(new Color(0,120,0));
            } else {
                p.setColor(Color.RED);
            }

            win.getDrawingArea().getDrawableObjects().add(p);
        } else {
            double xn = (x - win.getDrawingArea().getOriginX()) / (double)(win.getDrawingArea().getStepSize());
            double yn = (win.getDrawingArea().getOriginY() - y) / (double)(win.getDrawingArea().getStepSize());

            if (!win.getDrawingArea().getDrawableObjects().isEmpty())
            {
                Point p = (Point)win.getDrawingArea().getDrawableObjects().get(0);

                double xp = (p.getX() - win.getDrawingArea().getOriginX()) / (double)(win.getDrawingArea().getStepSize());
                double yp = (win.getDrawingArea().getOriginY() - p.getY()) / (double)(win.getDrawingArea().getStepSize());
                double dist = (xn - xp) * (xn - xp) + (yn - yp) * (yn - yp);
                int amountOfDrawableElements = win.getDrawingArea().getDrawableObjects().size();

                if (dist < distEps) {
                    if (win.getDrawingArea().getDrawableObjects().size() > 2 && rotationDirection != -1) {
                        Point p0 = ((LineSegment) win.getDrawingArea().getDrawableObjects().get(amountOfDrawableElements -  1)).getB();
                        Point p1 = (Point)win.getDrawingArea().getDrawableObjects().get(0);

                        LineSegment newLineSegment = new LineSegment(p0, p1);
                        checkPolygonIntersection(newLineSegment);
                        rotationDirection = checkRotationDirection(newLineSegment,
                                (LineSegment)win.getDrawingArea().getDrawableObjects().get(amountOfDrawableElements - 1));
                        rotationDirection = checkRotationDirection((LineSegment)win.getDrawingArea().getDrawableObjects().get(1),
                                newLineSegment);


                        win.getDrawingArea().getDrawableObjects().add(newLineSegment);

                        isPolygonFormed = true;
                    }
                    return;
                }

                if (win.getDrawingArea().getDrawableObjects().get(amountOfDrawableElements - 1) instanceof LineSegment) {
                    Point p0 = ((LineSegment) win.getDrawingArea().getDrawableObjects().get(amountOfDrawableElements -  1)).getB();
                    Point p1 = new Point(x, y);

                    LineSegment newLineSegment = new LineSegment(p0, p1);
                    checkPolygonIntersection(newLineSegment);
                    rotationDirection = checkRotationDirection(newLineSegment,
                            (LineSegment)win.getDrawingArea().getDrawableObjects().get(amountOfDrawableElements - 1));

                    win.getDrawingArea().getDrawableObjects().add(newLineSegment);
                } else {
                    Point p0 = (Point)win.getDrawingArea().getDrawableObjects().get(0);
                    Point p1 = new Point(x, y);

                    win.getDrawingArea().getDrawableObjects().add(new LineSegment(p0, p1));
                }
            } else {
                win.getDrawingArea().getDrawableObjects().add(new Point(x, y));
            }
        }
    }

    private boolean isInside(Point p) {
        double angle = 0;

        Iterator<DrawableObject> it = win.getDrawingArea().getDrawableObjects().iterator();
        it.next();
        LineSegment lineSegment = (LineSegment)it.next();
        while (lineSegment != null)
        {
            double cntAngle = computeAngle(p, lineSegment);

            if (Math.abs(Math.abs(cntAngle) - Math.PI) < 1e-12) {
                return true;
            }
            angle += cntAngle;

            try {
                lineSegment = (LineSegment)it.next();
            } catch (ClassCastException | NoSuchElementException e) {
                break;
            }
        }

        return Math.abs(Math.abs(angle) - 2 * Math.PI) < 1e-12;
    }

    private double computeAngle(Point p, LineSegment ls) {
        Vector AB = new Vector(ls.getA().getX() - p.getX(), ls.getA().getY() - p.getY());
        Vector AC = new Vector(ls.getB().getX() - p.getX(), ls.getB().getY() - p.getY());

        double ABLength = Vector.getLength(AB);
        double ACLength = Vector.getLength(AC);
        if (Math.abs(ABLength) < 1e-12 || Math.abs(ACLength) < 1e-12) {
            return Math.PI;
        }

//        win.getLog().printMessage("P: " + Vector.dotProduct(AB, AC));
//        win.getLog().printMessage("AB: " + Vector.getLength(AB));
//        win.getLog().printMessage("AC: " + Vector.getLength(AC));
//        win.getLog().printMessage("Cos: " + Vector.dotProduct(AB, AC) / Vector.getLength(AB) / Vector.getLength(AC));

        double dotProduct = Vector.dotProduct(AB, AC) / ABLength / ACLength;
        dotProduct = Math.max(dotProduct, -1.0);
        dotProduct = Math.min(dotProduct, 1.0);
        double angle = Math.acos(dotProduct);
        if (Vector.crossProduct(AB,AC).getZ() < 0) {
            angle *= -1;
        }

        return angle;
    }

    private boolean isRayIntersect(Point p, LineSegment ls) {
        if (ls.getA().getY() > ls.getB().getY()) {
            return isRayIntersect(p, new LineSegment(ls.getB(), ls.getA()));
        }

        if (p.getY() > ls.getB().getY() || p.getY() < ls.getA().getY() || Math.abs(p.getY() - ls.getA().getY()) < 1e-12
                || p.getX() > Math.max(ls.getA().getX(), ls.getB().getX())
                || Math.abs(p.getX() - Math.max(ls.getA().getX(), ls.getB().getX())) < 1e-12) {
            return false;
        }

        if (p.getX() < Math.min(ls.getA().getX(), ls.getB().getX())) {
            return true;
        }

        double tga = (p.getY() - ls.getA().getY()) / (p.getX() - ls.getA().getX());
        double tgb = (ls.getB().getY() - ls.getA().getY()) / (ls.getB().getX() - ls.getA().getX());
        return tga > tgb || Math.abs(tga -tgb) < 1e-12;
    }

    private boolean isBelongs(Point p, LineSegment ls) {
        double x1 = p.getX() - ls.getA().getX();
        double y1 = p.getY() - ls.getA().getY();
        double x2 = ls.getB().getX() - ls.getA().getX();
        double y2 = ls.getB().getY() - ls.getA().getY();

        if (Math.abs(x1 * y2 - x2 * y1) > 1e-12) {
            return false;
        }

        if (Math.abs(p.getX() - ls.getA().getX()) < 1e-12 && Math.abs(p.getY() - ls.getA().getY()) < 1e-12) {
            return true;
        }

        if (Math.abs(p.getX() - ls.getB().getX()) < 1e-12 && Math.abs(p.getY() - ls.getB().getY()) < 1e-12) {
            return true;
        }

        double minX = Math.min(ls.getA().getX(), ls.getB().getX());
        double maxX = Math.max(ls.getA().getX(), ls.getB().getX());
        double minY = Math.min(ls.getA().getY(), ls.getB().getY());
        double maxY = Math.max(ls.getA().getY(), ls.getB().getY());

        if (((p.getX() > minX || Math.abs(p.getX() - minX) < 1e-12) &&
                (p.getX() < maxX || Math.abs(p.getX() - maxX) < 1e-12)) &&
                ((p.getY() > minY || Math.abs(p.getY() - minY) < 1e-12) &&
                        (p.getY() < maxY || Math.abs(p.getY() - maxY) < 1e-12)))
            return true;

        return false;
    }

    private  void checkPolygonIntersection(LineSegment ls) {
        Iterator<DrawableObject> it = win.getDrawingArea().getDrawableObjects().iterator();
        it.next();
        LineSegment lineSegment = (LineSegment)it.next();
        while (it.hasNext())
        {
            if (!isSelfIntersect) {
                isSelfIntersect = isIntersect(lineSegment, ls);
            }
            lineSegment = (LineSegment)it.next();
        }
    }

    private  int checkRotationDirection(LineSegment ls1, LineSegment ls2) {
        double z = Vector.crossProduct(new Vector(ls1), new Vector(ls2)).getZ();

        if (Math.abs(z) < 1e-12) {
            return rotationDirection;
        } else if (z > 0) {
            if (rotationDirection == 1) {
                isConvex = false;
            }
            return 0;
        } else {
            if (rotationDirection == 0) {
                isConvex = false;
            }
            return 1;
        }
    }

    public  boolean isIntersect(LineSegment ls1, LineSegment ls2) {
        Point A1 = ls1.getA(), B1 = ls1.getB();
        Point A2 = ls2.getA(), B2 = ls2.getB();

        double z1 = Vector.crossProduct(new Vector(B1.getX() - A1.getX(), B1.getY() - A1.getY()),
                new Vector(A2.getX() - A1.getX(), A2.getY() - A1.getY())).getZ();

        double z2 = Vector.crossProduct(new Vector(B1.getX() - A1.getX(), B1.getY() - A1.getY()),
                new Vector(B2.getX() - A1.getX(), B2.getY() - A1.getY())).getZ();

        boolean firstIntersection = z1 * z2 < 0;

        z1 = Vector.crossProduct(new Vector(B2.getX() - A2.getX(), B2.getY() - A2.getY()),
                new Vector(A1.getX() - A2.getX(), A1.getY() - A2.getY())).getZ();

        z2 = Vector.crossProduct(new Vector(B2.getX() - A2.getX(), B2.getY() - A2.getY()),
                new Vector(B1.getX() - A2.getX(), B1.getY() - A2.getY())).getZ();

        boolean secondIntersection = z1 * z2 < 0;

        return firstIntersection && secondIntersection;
    }

    private  synchronized void showResult() {
        if (isSelfIntersect) {
            win.getLog().printMessage("The polygon is self intersecting");
            return;
        }
    }


    public static void main(String[] args) {
        Lab4 lab = new Lab4();

        lab.win.getDrawingArea().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lab.applyAction(e.getX(), e.getY());
            }
        });
        new PointInsertionForm(lab, 400, 300);

        lab.win.getDrawingArea().render();
    }

    @Override
    public void interactWithForm(int x, int y) {
        int x0 = win.getDrawingArea().getOriginX(), y0 = win.getDrawingArea().getOriginY();
        int stepSize = win.getDrawingArea().getStepSize();
        int xw = x0 + (int)(x / 1000.0 * stepSize);
        int yw = y0 - (int)(y / 1000.0 * stepSize);

        if (xw < 0 || xw > win.getDrawingArea().getWidth()) {
            JOptionPane.showMessageDialog(null, "x coordinate exceeds window's size", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (yw < 0 || yw > win.getDrawingArea().getHeight()) {
            JOptionPane.showMessageDialog(null, "y coordinate exceeds window's size", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        applyAction(xw, yw);
    }
}
