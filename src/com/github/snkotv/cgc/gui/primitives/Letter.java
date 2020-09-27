package com.github.snkotv.cgc.gui.primitives;

import java.awt.*;

public class Letter implements DrawableObject {

    private String letter;
    private double x, y;
    private Color color = Color.BLACK;

    public void setColor(Color color) {this.color = color;}

    public Letter(String letter, double x, double y) {
        this.letter = letter;
        this.x = x;
        this.y = y;
    }

    @Override
    public void onRender(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);

        g2d.setFont(new Font("TNR", Font.BOLD, 24));
        g2d.drawString(letter, (int)x, (int)y);
    }
}
