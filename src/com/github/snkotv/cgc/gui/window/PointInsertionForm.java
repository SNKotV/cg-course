package com.github.snkotv.cgc.gui.window;

import com.github.snkotv.cgc.gui.primitives.Point;
import com.github.snkotv.cgc.labs.Lab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class PointInsertionForm extends JFrame {
    private Lab master;
    private int width, height;
    private JLabel xLabel, yLabel;
    private JTextField xTextField, yTextField;
    private JButton enterButton;

    public PointInsertionForm(Lab lab, int width, int height)
    {
        master = lab;
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        setPreferredSize(new Dimension(width, height));
        addContent();
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void addContent() {
        setLayout(new FlowLayout(0,5,5));

        Font font = new Font("TNR", Font.ITALIC,width / 15);

        JPanel xLinePanel = new JPanel();
        xLinePanel.setPreferredSize(new Dimension((int)(width * 0.85), height / 4));

        xLabel = new JLabel("X:");
        xLabel.setPreferredSize(new Dimension((int)(width * 0.085), height / 5));
        xLabel.setFont(font);
        xLinePanel.add(xLabel);

        xTextField = new JTextField();
        xTextField.setPreferredSize(new Dimension((int)(width * 0.5), height / 5));
        xTextField.setFont(font);
        xLinePanel.add(xTextField);

        getContentPane().add(xLinePanel);

        JPanel yLinePanel = new JPanel();
        yLinePanel.setPreferredSize(new Dimension((int)(width * 0.85), height / 4));

        yLabel = new JLabel("Y:");
        yLabel.setPreferredSize(new Dimension((int)(width * 0.085), height / 5));
        yLabel.setFont(font);
        yLinePanel.add(yLabel);

        yTextField = new JTextField();
        yTextField.setPreferredSize(new Dimension((int)(width * 0.5), height / 5));
        yTextField.setFont(font);
        yLinePanel.add(yTextField);

        getContentPane().add(yLinePanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension((int)(width * 0.85), height / 4));
        buttonPanel.setLayout(new FlowLayout(0, width / 4,0));
        
        enterButton = new JButton("Enter");
        enterButton.setPreferredSize(new Dimension((int)(width * 0.4), height / 5));
        enterButton.setFont(font);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter x coordinate", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (yTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Enter y coordinate", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double x, y;

                try {
                    x = Double.parseDouble(xTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "x coordinate must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    y = Double.parseDouble(yTextField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "y coordinate must be a number", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                xTextField.setText("");
                yTextField.setText("");

                master.interactWithForm((int)(x * 1000), (int)(y * 1000));
            }
        });

        buttonPanel.add(enterButton);

        getContentPane().add(buttonPanel);
    }
}
