package com.github.snkotv.cgc.gui.panels;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel {
    private JLabel title;
    private JScrollPane logPanel;
    private JTextArea textArea;

    public LogPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setLayout(new FlowLayout(0, (int)(width * 0.05), (int)(height * 0.05)));
        setTitle((int)(width * 0.9), (int)(height * 0.10));
        setLogPanel((int)(width * 0.9), (int)(height * 0.75));
    }

    private void setTitle(int width, int height) {
        title = new JLabel("LOG", SwingConstants.CENTER);
        title.setPreferredSize(new Dimension(width, height));
        title.setFont(new Font("TNR", Font.ITALIC,width / 10));
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(title);
    }

    private void setLogPanel(int width, int height) {
        textArea = new JTextArea();
        textArea.setFont(new Font("TNR", Font.ITALIC,width / 15));
        textArea.setLineWrap(true);
        textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        textArea.setEditable(false);

        logPanel = new JScrollPane(textArea);
        logPanel.setPreferredSize(new Dimension(width, height));
        add(logPanel);
    }

    public void printMessage(String message) {
        textArea.append(message + '\n');
    }

}
