package com.company;

public class Validators {
    static Integer getGuessNumber(String text) throws InvalidGuessValueException, InvalidGuessFormatException, InvalidEmptyGuessException {
        if(text.equals(""))
            throw new InvalidEmptyGuessException();
        try {
            Integer newInt = Integer.parseInt(text);
            if(newInt <= 0 || newInt > 10000)
                throw new InvalidGuessValueException();
            return newInt;
        } catch (NumberFormatException exception) {
            throw new InvalidGuessFormatException();
        }
    }
}
