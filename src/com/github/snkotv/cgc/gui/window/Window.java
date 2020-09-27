package com.github.snkotv.cgc.gui.window;

import com.github.snkotv.cgc.gui.panels.DrawPanel;
import com.github.snkotv.cgc.gui.panels.LogPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    private int width, height;
    private DrawPanel drawingArea;
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
        drawingArea = new DrawPanel((int)(width * 0.5), (int)(width * 0.5));
        getContentPane().add(drawingArea, BorderLayout.WEST);

        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension((int)(width * 0.35), (int)(height * 0.85)));

        logPanel = new LogPanel((int)(width * 0.35), (int)(height * 0.72));
        sidePanel.add(logPanel);

        JButton resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension((int)(width * 0.1), (int)(height * 0.06)));
        resetButton.setFont(new Font("TNR", Font.CENTER_BASELINE, 14));
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingArea.clear();
                logPanel.clear();
            }
        });
        sidePanel.add(resetButton);

        getContentPane().add(sidePanel, BorderLayout.EAST);


    }

    public DrawPanel getDrawingArea() {
        return drawingArea;
    }

    public LogPanel getLog() {
        return logPanel;
    }

}
