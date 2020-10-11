package com.github.snkotv.cgc.labs;

import com.github.snkotv.cgc.geom.utils.Vector;
import com.github.snkotv.cgc.gui.primitives.DrawableObject;
import com.github.snkotv.cgc.gui.primitives.Letter;
import com.github.snkotv.cgc.gui.primitives.LineSegment;
import com.github.snkotv.cgc.gui.primitives.Point;
import com.github.snkotv.cgc.gui.window.Window;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;

public class Lab2 {
    private static Window win = new Window("Lab2", 1680, 1020);
    private static boolean isSelfIntersect = false;
    private static boolean isPolygonFormed = false;
    private static boolean isConvex = true;
    private static int rotationDirection = -1; // -1 - undefined, 0 - counterClockwise, 1 - clockwise
    private static double distEps = 1e-1;


    private static synchronized void applyAction(int x, int y)    {
        if (win.getDrawingArea().getDrawableObjects().isEmpty()) {
            isPolygonFormed = false;
            isSelfIntersect = false;
            isPolygonFormed = false;
            isConvex = true;
            rotationDirection = -1;
        }

        if (isPolygonFormed) {
            return;
        }

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
                    showResult();
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

    private static void checkPolygonIntersection(LineSegment ls) {
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

    private static int checkRotationDirection(LineSegment ls1, LineSegment ls2) {
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

    public static boolean isIntersect(LineSegment ls1, LineSegment ls2) {
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

    private static synchronized void showResult() {
        if (isSelfIntersect) {
            win.getLog().printMessage("The polygon is self intersecting");
            return;
        }

        if (!isConvex) {
            win.getLog().printMessage("The polygon is concave");
            return;
        }

        if (rotationDirection == 0) {
            win.getLog().printMessage("Counterclockwise");
        } else {
            win.getLog().printMessage("Clockwise");
        }

        win.getLog().printMessage("The polygon is convex");

        Iterator<DrawableObject> it = win.getDrawingArea().getDrawableObjects().iterator();
        it.next();
        LineSegment ls = (LineSegment)it.next();
        int index = 1;

        while (it.hasNext())
        {
            double x = ls.getA().getX(), y = ls.getA().getY();
            Letter letter = new Letter("" + (index++), x, y);
            letter.setColor(Color.RED);
            win.getDrawingArea().getDrawableObjectsQueue().add(letter);
            ls = (LineSegment)it.next();
        }

        double x = ls.getA().getX(), y = ls.getA().getY();
        Letter letter = new Letter("" + (index++), x, y);
        letter.setColor(Color.RED);
        win.getDrawingArea().getDrawableObjectsQueue().add(letter);
    }


    public static void main(String[] args) {
        win.getDrawingArea().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                applyAction(e.getX(), e.getY());
            }
        });

        win.getDrawingArea().render();
    }
}
