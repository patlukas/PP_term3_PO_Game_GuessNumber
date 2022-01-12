package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuessNumberPage extends JFrame{
    private JTable table1;
    private JTextField guessNumberTextField;
    private JButton startButton;
    private JButton guessButton;
    private JPanel mainPanel;
    private JLabel numberOfGuessLabel;
    private JLabel numberTipLabel;
    private JLabel timerLabel;
    private Integer numberOfGuess, solutionNumber, numberOfSeconds;

    public GuessNumberPage() {
        setupFrame();

        startButton.addActionListener(e -> startNewGame());
        guessButton.addActionListener(e -> guessNumber());
        guessNumberTextField.addActionListener(e -> guessNumber());
    }

    private void setupFrame() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 400));
        setVisible(true);
        setVisibleTheGuessingWidgets(false);
    }

    private void startNewGame() {
        timer.stop();
        numberOfGuess = 0;
        solutionNumber = 55;
        numberOfSeconds = 0;
        timer.start();
        numberTipLabel.setText("");
        numberOfGuessLabel.setText("Numer próby: 0");
        setVisibleTheGuessingWidgets(true);
        guessNumberTextField.grabFocus();
        System.out.println("Start");
    }

    private void guessNumber() {
        numberOfGuess++;
        numberOfGuessLabel.setText("Numer próby: "+numberOfGuess);
        String numberString = guessNumberTextField.getText();
        Integer number = Integer.parseInt(numberString);
        guessNumberTextField.setText("");
        guessNumberTextField.grabFocus();
        if(number < solutionNumber) {
            numberTipLabel.setText("Liczba "+number+" jest MNIEJSZA od rozwiązania");
        } else if(number > solutionNumber) {
            numberTipLabel.setText("Liczba "+number+" jest WIĘKSZA od rozwiązania");
        } else {
            numberTipLabel.setText("SUKCES!!! Szukana liczba to "+number+"!!!");
            setVisibleTheGuessingWidgets(false);
            timer.stop();
        }

        System.out.println("Zgaduj");
    }

    private void setVisibleTheGuessingWidgets(Boolean visible) {
        guessNumberTextField.setVisible(visible);
        guessButton.setVisible(visible);
    }

    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            numberOfSeconds++;
            timerLabel.setText("Czas: "+numberOfSeconds+"s");
        }
    });
}
