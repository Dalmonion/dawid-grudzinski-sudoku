package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public final class GameProcessor {
    private static final List<Backtrack> backtracks = new ArrayList<>();
    private final RowHandler rowHandler = new RowHandler();
    private final ColumnHandler columnHandler = new ColumnHandler();
    private final SectionHandler sectionHandler = new SectionHandler();

    public int numberGuesser(SudokuBoard board, int rowIndex, int columnIndex) {
        int pickedValue = -1;
        pickedValue = board.getRows().get(rowIndex).getElements().get(columnIndex).getRemainingChoices().get(0);
        return pickedValue;
    }

    public SudokuResult process(SudokuBoard board) {
        for (int i = 0; i < board.getRows().size(); i++) {
            for (int j = 0; j < board.getRows().get(i).getElements().size(); j++) {
                if (board.getRows().get(i).getElements().get(j).getValue() != -1) continue;
                boolean anyActionOnRow;
                boolean anyActionOnColumn = false;
                boolean anyActionOnSection = false;
                try {
                    anyActionOnRow = rowHandler.processRow(board, board.getRows().get(i), j, i);
                    if (!anyActionOnRow) {
                        anyActionOnColumn = columnHandler.processColumn(board, j, i);
                        if (!anyActionOnColumn && !board.getRows().get(i).getElements().get(j).getRemainingChoices().isEmpty())
                            anyActionOnSection = sectionHandler.processSection(board, j, i);
                    }
                } catch (OutOfChoicesException e) {
                    if (!backtracks.isEmpty()) {
                        board = backtracks.get(backtracks.size() - 1).getSavedBoard();
                        i = backtracks.get(backtracks.size() - 1).getRowValue();
                        j = backtracks.get(backtracks.size() - 1).getColumnValue() - 1;
                        board.getRows().get(i).getElements().get(j + 1).removeChoice(backtracks.get(backtracks.size() - 1).getGuessedValue());
                        backtracks.remove(backtracks.size() - 1);
                        continue;
                    } else {
                        System.out.println("Entered Sudoku is incorrect!");
                        return new SudokuResult(board, false);
                    }
                }
                if (!anyActionOnRow && !anyActionOnColumn && !anyActionOnSection) {
                    int pickedValue = numberGuesser(board, i, j);
                    Backtrack backtrack = new Backtrack(i, j, pickedValue, board);
                    backtracks.add(backtrack);
                    board.getRows().get(i).getElements().get(j).setValue(pickedValue);
                }
            }
        }
        SudokuResult result = null;
        try {
            result = new SudokuResult(board.deepCopy(), true);
        } catch (CloneNotSupportedException e) {
            System.out.println(e);
        }
        return result;
    }
}
