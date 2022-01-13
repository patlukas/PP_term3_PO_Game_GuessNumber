package com.company;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class GuessNumberPage extends JFrame{
    private JTable resultsTable;
    private JTextField guessNumberTextField;
    private JButton startButton;
    private JButton guessButton;
    private JPanel mainPanel;
    private JLabel numberOfGuessLabel;
    private JLabel numberTipLabel;
    private JLabel timerLabel;
    private JLabel entryLabel;
    private JButton endButton;
    private Integer numberOfGuess, solutionNumber;
    private final DefaultTableModel model = new DefaultTableModel();
    private long timeStartGame;
    private String defaultNickName = "";

    public GuessNumberPage() {
        setupFrame();
        endButton.addActionListener(e -> endGame());
        startButton.addActionListener(e -> startNewGame());
        guessButton.addActionListener(e -> guessNumber());
        guessNumberTextField.addActionListener(e -> guessNumber());
        mainPanel.getRootPane().setDefaultButton(startButton);
    }

    private void setupFrame() {
        setContentPane(mainPanel);
        setTitle("Zgadnij numer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setVisible(true);
        setVisibleTheGuessingWidgets(false);
        setDefaultSettingsTable();
        loadResultsFromFile();
    }

    private void startNewGame() {
        Random rand = new Random();

        timeStartGame = Instant.now().toEpochMilli();
        numberOfGuess = 0;
        solutionNumber = rand.nextInt(8192) + 1;
        timer.start();
        numberTipLabel.setText("");
        numberOfGuessLabel.setText("Numer próby: 0");
        setVisibleTheGuessingWidgets(true);
        guessNumberTextField.grabFocus();
    }

    private void endGame() {
        timer.stop();
        numberTipLabel.setText("Niestety nie ukończyłeś gry");
        setVisibleTheGuessingWidgets(false);
    }

    private void setDefaultSettingsTable() {
        resultsTable.setModel(model);
        resultsTable.getTableHeader().setReorderingAllowed(false);
        model.addColumn("Id");
        model.addColumn("Nick");
        model.addColumn("Kiedy");
        model.addColumn("Ilość prób");
        model.addColumn("Czas zgdywania");
        model.addColumn("Jaki numer był zgadywany");
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(125);
    }

    private void guessNumber() {
        try {
            Integer guessNumber = Validators.getGuessNumber(guessNumberTextField.getText());
            numberOfGuessLabel.setText("Numer próby: "+(++numberOfGuess));
            if(guessNumber < solutionNumber) numberTipLabel.setText("Za mało!!!");
            else if(guessNumber > solutionNumber) numberTipLabel.setText("Za dużo!!!");
            else afterGuessingTheNumber();
        } catch (GuessNumberExceptions e) {
            numberTipLabel.setText(e.toString());
        }
        guessNumberTextField.setText("");
        guessNumberTextField.grabFocus();
    }

    private void afterGuessingTheNumber() {
        numberTipLabel.setText("SUKCES!!! Szukana liczba to "+solutionNumber+"!!!");
        setVisibleTheGuessingWidgets(false);
        timer.stop();
        timerLabel.setText("Czas: "+getGameTimeInSeconds());
        addRowToResultsTable();
    }

    private void setVisibleTheGuessingWidgets(Boolean visible) {
        guessNumberTextField.setVisible(visible);
        guessButton.setVisible(visible);
        entryLabel.setVisible(visible);
        endButton.setVisible(visible);
        startButton.setVisible(!visible);
    }

    Timer timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Float time = getGameTimeInSeconds();
            String result = String.format("Czas: %.1fs", time);
            timerLabel.setText(result);
        }
    });

    private Float getGameTimeInSeconds() {
        Long timeMili = Instant.now().toEpochMilli() - timeStartGame;
        Float time = timeMili.floatValue();
        return time / 1000;
    }

    private void addRowToResultsTable() {
        Float time = getGameTimeInSeconds();
        String nowDate = getNowDate();
        String nickPlayer = JOptionPane.showInputDialog(this, "Podaj nick:", defaultNickName);
        defaultNickName = "";
        if(nickPlayer == null || nickPlayer.equals("")) nickPlayer = "Gall Anonim";
        else defaultNickName = nickPlayer;
        Object[] row = {nickPlayer, nowDate, numberOfGuess, time, solutionNumber};
        addRowAndSortTableWithResults(row);
        addRowToFile(row);
    }

    private void addRowToFile(Object[] row) {
        try {
            FileWriter file = new FileWriter("results.txt", true);
            file.write(row[0]+";"+row[1]+";"+row[2]+";"+row[3]+";"+row[4]+"\n");
            file.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Nie udało się zapisać wyniku do pliku",  "Błąd pliku", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String getNowDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void addRowAndSortTableWithResults(Object[] newRow) {
        ArrayList<Object[]> valFromTable = new ArrayList<>();
        valFromTable.add(newRow);
        while(model.getRowCount() > 0) {
            Object[] row = {
                    model.getValueAt(0, 1),
                    model.getValueAt(0, 2),
                    model.getValueAt(0, 3),
                    model.getValueAt(0, 4),
                    model.getValueAt(0, 5)
            };
            model.removeRow(0);
            valFromTable.add(row);
        }
        sortRowAndMakeTableWithResults(valFromTable);
    }

    private void sortRowAndMakeTableWithResults(ArrayList<Object[]> valFromTable) {
        valFromTable.sort(new Comparator<Object[]>() {
            @Override
            public int compare(Object[] lhs, Object[] rhs) {
                int numberGuess1 = (Integer)lhs[2];
                int numberGuess2 = (Integer)rhs[2];
                if(numberGuess1 < numberGuess2) return -1;
                if(numberGuess1 > numberGuess2) return 1;

                float time1 = (Float)lhs[3];
                float time2 = (Float)rhs[3];
                if(time1 < time2) return -1;
                if(time1 > time2) return 1;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                Date date1, date2;
                try {
                    date1 = sdf.parse((String)rhs[1]);
                } catch (ParseException e) {return -1;}
                try {
                    date2 = sdf.parse((String)lhs[1]);
                } catch (ParseException e) {return 1;}
                return date2.compareTo(date1);
            }
        });
        for(int i=0; i<valFromTable.size(); i++) {
            Object[] el = valFromTable.get(i);
            Object[] row = {i+1, el[0], el[1], el[2], el[3], el[4]};
            model.addRow(row);
        }
    }

    private void loadResultsFromFile() {
        File file = new File("results.txt");
        if (file.exists()) {
            try {
                Scanner myReader = new Scanner(file);
                ArrayList<Object[]> listRowData = new ArrayList<>();
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    String[] splitedData = line.split(";");
                    Object[] row = {
                            splitedData[0],
                            splitedData[1],
                            Integer.parseInt(splitedData[2]),
                            Float.parseFloat(splitedData[3]),
                            splitedData[4]
                    };
                    listRowData.add(row);
                }
                sortRowAndMakeTableWithResults(listRowData);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Nie można otworzyć pliku z wynikami", "Błąd pliku", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Niepoprawne dane w pliku z wynikami",  "Błąd danych", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
