package com.company;

public class GuessNumberExceptions extends Exception {}

class InvalidEmptyGuessException extends GuessNumberExceptions {
    @Override
    public String toString() {
        return "Nie podano liczby";
    }
}

class InvalidGuessValueException extends GuessNumberExceptions {
    @Override
    public String toString() {
        return "Liczba jest spoza zakresu";
    }
}

class InvalidGuessFormatException extends GuessNumberExceptions {
    @Override
    public String toString() {
        return "Błędne dane wejściowe";
    }
}

