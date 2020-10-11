package com.github.snkotv.cgc.gui.primitives;

import java.awt.*;

public abstract class DrawableObject {
    protected Color color = Color.BLACK;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public abstract void onRender(Graphics g);
}
