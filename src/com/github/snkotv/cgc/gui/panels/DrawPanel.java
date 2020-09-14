package com.github.snkotv.cgc.gui.panels;

import com.github.snkotv.cgc.gui.primitives.DrawableObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawPanel extends JPanel {

    private int width, height;
    private ArrayList<DrawableObject> drawableObjects = new ArrayList<>();


    public ArrayList<DrawableObject> getDrawableObjects() {
        return drawableObjects;
    }

    public DrawPanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
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
        Graphics g = getGraphics();
        drawGrid(getGraphics());

        for (DrawableObject drawObj: drawableObjects) {
            drawObj.onRender(g);
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        render();
    }

}
