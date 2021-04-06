package com.sudoku;

public final class SudokuResult {
    private final SudokuBoard board;
    private final boolean isResolved;

    public SudokuResult(SudokuBoard board, boolean isResolved) {
        this.board = board;
        this.isResolved = isResolved;
    }

    public SudokuBoard getBoard() {
        return board;
    }

    public boolean isResolved() {
        return isResolved;
    }
}
