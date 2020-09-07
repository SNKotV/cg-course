package com.github.snkotv.cgc;

import javax.swing.*;
import java.awt.*;

public class DrawPanel extends JPanel {

    private double scaleFactor = 1.0;

    public DrawPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.WHITE);
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public double getScaleFactor() { return scaleFactor;    }

}
