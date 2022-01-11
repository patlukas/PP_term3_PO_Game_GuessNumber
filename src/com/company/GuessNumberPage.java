package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.ArrayList;

public class GuessNumberPage extends JFrame{
    private JTable table1;
    private JTextField textField1;
    private JButton startButton;
    private JButton zgadujButton;
    private JPanel mainPanel;

    public GuessNumberPage() {
        setupFrame();

        startButton.addActionListener(e -> startNewGame());
    }

    private void setupFrame() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setVisible(true);
    }

    private void startNewGame() {
        return;
    }
}
