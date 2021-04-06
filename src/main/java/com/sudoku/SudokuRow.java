package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public final class SudokuRow {
    private final List<SudokuElement> row = new ArrayList<>();

    public SudokuRow() {
        for (int i = 0; i < 9; i++) {
            row.add(new SudokuElement());
        }
    }
    public List<SudokuElement> getElements() {
        return row;
    }
}
