package com.github.snkotv.cgc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Window extends JFrame {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private static final int MIN_WIDTH = 400;
    private static final int MIN_HEIGHT = 300;

    private int width, height;
    private DrawPanel drawingArea;
    private JPanel sidePanel; // Log and scale buttons
    private LogPanel logPanel;

    public Window(String title, int width, int height)    {
        setTitle(title);
        setPreferredSize(new Dimension(width, height));
        this.width = width;
        this.height = height;
        init();
    }

    public Window(String title)    {
        this(title, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        int hgap = (int) (width * 0.05);
        int vgap = (int) (height * 0.05);
        setLayout(new FlowLayout(0, hgap, vgap));
        addPanels();

        setVisible(true);
    }

    private void addPanels() {
        drawingArea = new DrawPanel((int)(width * 0.5), (int)(height * 0.85));
        getContentPane().add(drawingArea);

        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension((int)(width * 0.35), (int)(height * 0.85)));
        sidePanel.setLayout(new FlowLayout(0, 0, (int) (height * 0.05)));

        logPanel = new LogPanel((int)(width * 0.35), (int)(height * 0.65));
        sidePanel.add(logPanel);

        JPanel scalePanel = new JPanel();
        scalePanel.setPreferredSize(new Dimension((int)(width * 0.35), (int)(height * 0.1)));
        scalePanel.setLayout(new FlowLayout(0, (int)(width * 0.02), (int)(height * 0.02)));
        scalePanel.setBackground(Color.WHITE);

        JLabel scaleFactorLabel = new JLabel("Scale:");
        scaleFactorLabel.setPreferredSize(new Dimension((int)(width * 0.09), (int)(height * 0.06)));
        scaleFactorLabel.setFont(new Font("TNR", Font.ROMAN_BASELINE, (int)(width * 0.03)));
        scalePanel.add(scaleFactorLabel);

        JLabel scaleFactorValueLabel = new JLabel(drawingArea.getScaleFactor() + "");
        scaleFactorValueLabel.setPreferredSize(new Dimension((int)(width * 0.06), (int)(height * 0.06)));
        scaleFactorValueLabel.setFont(new Font("TNR", Font.ROMAN_BASELINE, (int)(width * 0.03)));
        scalePanel.add(scaleFactorValueLabel);

        JButton scaleUp = new JButton("+");
        scaleUp.setPreferredSize(new Dimension((int)(width * 0.06), (int)(height * 0.06)));
        scaleUp.setFont(new Font("TNR", Font.ROMAN_BASELINE, (int)(width * 0.03)));
        scaleUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double scaleFactor = drawingArea.getScaleFactor();
                if (scaleFactor > 1.75) return;
                scaleFactor += 0.25;
                drawingArea.setScaleFactor(scaleFactor);
                scaleFactorValueLabel.setText(scaleFactor + "");
            }
        });
        scalePanel.add(scaleUp);

        JButton scaleDown = new JButton("-");
        scaleDown.setPreferredSize(new Dimension((int)(width * 0.06), (int)(height * 0.06)));
        scaleDown.setFont(new Font("TNR", Font.ROMAN_BASELINE, (int)(width * 0.03)));
        scaleDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double scaleFactor = drawingArea.getScaleFactor();
                if (scaleFactor < 0.5) return;
                scaleFactor -= 0.25;
                drawingArea.setScaleFactor(scaleFactor);
                scaleFactorValueLabel.setText(scaleFactor + "");
            }
        });
        scalePanel.add(scaleDown);

        sidePanel.add(scalePanel);
        getContentPane().add(sidePanel);
    }

    public DrawPanel getDrawingArea() {
        return drawingArea;
    }

    public LogPanel getLog() {
        return logPanel;
    }

}
