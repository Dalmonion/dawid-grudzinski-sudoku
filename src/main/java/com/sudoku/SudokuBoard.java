package com.sudoku;

import java.util.ArrayList;
import java.util.List;

public final class SudokuBoard extends Prototype<SudokuBoard>{

    private List<SudokuRow> board = new ArrayList<>();

    public SudokuBoard() {
        for (int i = 0; i < 9; i++) {
            board.add(new SudokuRow());
        }
    }

    public List<SudokuRow> getRows() {
        return board;
    }

    public void updateBoard(String updateValues) {
        int row = Integer.parseInt(updateValues.substring(1,2)) - 1;
        int column = Integer.parseInt(updateValues.substring(0,1)) - 1;
        int value = Integer.parseInt(updateValues.substring(2,3));

        board.get(row).getElements().get(column).setValue(value);
    }

    public SudokuBoard deepCopy() throws CloneNotSupportedException {
        SudokuBoard clonedBoard = super.clone();
        clonedBoard.board = new ArrayList<>();
        for (SudokuRow row : board) {
            SudokuRow clonedRow = new SudokuRow();
            clonedRow.getElements().clear();
            for (SudokuElement element : row.getElements()) {
                SudokuElement clonedElement = new SudokuElement();
                clonedElement.setValue(element.getValue());
                clonedElement.clearRemainingChoices();
                for (int i = 0; i <element.getRemainingChoices().size(); i++) {
                    clonedElement.addChoice(element.getRemainingChoices().get(i));
                }
                clonedRow.getElements().add(clonedElement);
            }
            clonedBoard.getRows().add(clonedRow);
        }
        return clonedBoard;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < board.size(); i++) {
            result.append("| ");
            if (i == 3 || i == 6) result.append("- - - + - - - + - - - |\n| ");
            for (int j = 0; j < board.get(i).getElements().size(); j++) {
                if (board.get(i).getElements().get(j).getValue() == -1) {
                    result.append(". ");
                } else {
                    result.append(board.get(i).getElements().get(j).getValue() + " ");
                }
                if (j == 2 || j == 5) result.append("| ");
            }
            result.append("|\n");
        }
        return result.toString();
    }
}
