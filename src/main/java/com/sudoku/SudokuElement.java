package com.sudoku;


import java.util.*;

public final class SudokuElement {
    private static final int EMPTY =  -1;

    private int value;
    private final Integer choiceArray [] = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private ArrayList<Integer> remainingChoices;

    public SudokuElement() {
        value = EMPTY;
        remainingChoices = new ArrayList<>(Arrays.asList(choiceArray));
    }

    public SudokuElement(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value == EMPTY) {
            this.value = EMPTY;
            remainingChoices = new ArrayList<>(Arrays.asList(choiceArray));
        } else {
            this.value = value;
            remainingChoices.clear();
        }
    }

    public void clearRemainingChoices() {
        remainingChoices.clear();
    }

    public void addChoice(int choice) {
        remainingChoices.add(choice);
    }

    public List<Integer> getRemainingChoices() {
        return new ArrayList<>(remainingChoices);
    }

    public void removeChoice(int choice) {
        remainingChoices.remove(Integer.valueOf(choice));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SudokuElement element = (SudokuElement) o;

        if (value != element.value) return false;
        return hashCode() == o.hashCode();
    }

    @Override
    public int hashCode() {
        int result = value;
        result = 31 * result + remainingChoices.hashCode();
        return result;
    }
}
