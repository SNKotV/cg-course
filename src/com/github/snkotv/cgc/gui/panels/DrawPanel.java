package com.github.snkotv.cgc.gui.panels;

import com.github.snkotv.cgc.gui.primitives.DrawableObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class DrawPanel extends Canvas {

    private int width, height;
    private ArrayList<DrawableObject> drawableObjects = new ArrayList<>();
    private Queue<DrawableObject> drawableObjectsQueue = new ArrayDeque<>();

    private BufferStrategy bs;
    private Graphics g;

    public ArrayList<DrawableObject> getDrawableObjects() {
        return drawableObjects;
    }

    public Queue<DrawableObject> getDrawableObjectsQueue() {
        return drawableObjectsQueue;
    }

    public DrawPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    public int getOriginX() {
        return width / 2;
    }

    public int getOriginY() {
        return height / 2;
    }

    public int getStepSize() {
        return (int)(width * 0.05);
    }

    public void drawGrid(Graphics g) {
        int x0 = width / 2, y0 = height / 2;
        int step = (int)(width * 0.05);

        for (int i = 1; i < width / step; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x0 + i * step, 0, x0 + i * step, height);
            g.drawLine(x0 - i * step, 0, x0 - i * step, height);

            g.setColor(Color.BLACK);
            g.drawString(i + "", x0 + i * step, y0 + 15);
            g.drawString(-i + "", x0 - i * step, y0 + 15);
        }

        for (int i = 1; i < height / step; i++) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(0, y0 + i * step, width, y0 + i * step);
            g.drawLine(0, y0 - i * step, width, y0 - i * step);

            g.setColor(Color.BLACK);
            g.drawString(-i + "", x0 - 20, y0 + i * step);
            g.drawString(i + "", x0 - 20, y0 - i * step);
        }

        g.setColor(Color.BLACK);
        g.drawLine(x0, 0, x0, height);
        g.drawLine(0, y0, width, y0);
        g.drawString("0", x0,y0 + 15);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("TNR", Font.BOLD, 24));
        g2d.drawString("X", width - 30, y0 - 15);
        g2d.drawString("Y", x0 +20, 25);
    }

    public void render() {

        if (bs == null) {
            createBufferStrategy(2);
        }

        bs = getBufferStrategy();
        g = bs.getDrawGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        drawGrid(g);

        for (DrawableObject drawableObject: drawableObjectsQueue) {
            drawableObjects.add(drawableObject);
            drawableObjectsQueue.remove(drawableObject);
        }

        for (DrawableObject drawObj: drawableObjects) {
            drawObj.onRender(g);
        }

        g.dispose();
        bs.show();

//        try {
//            Thread.sleep(3);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        EventQueue.invokeLater(() -> render());
    }

    public void clear() {
        drawableObjects.clear();
    }

}
