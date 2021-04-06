package com.sudoku;

public final class Backtrack {
    private final int rowValue;
    private final int columnValue;
    private final int guessedValue;
    private SudokuBoard savedBoard;

    public Backtrack(final int rowValue,final int columnValue,final int guessedValue,final SudokuBoard board) {
        this.rowValue = rowValue;
        this.columnValue = columnValue;
        this.guessedValue = guessedValue;
        try {
            this.savedBoard = board.deepCopy();
        } catch (CloneNotSupportedException e) {
            System.out.println(e);
        }
    }

    public int getRowValue() {
        return rowValue;
    }

    public int getColumnValue() {
        return columnValue;
    }

    public int getGuessedValue() {
        return guessedValue;
    }

    public SudokuBoard getSavedBoard() {
        return savedBoard;
    }
}
