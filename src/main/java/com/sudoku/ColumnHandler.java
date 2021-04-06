package com.sudoku;

public class ColumnHandler extends BoardHandler {

    public boolean processColumn(SudokuBoard board, int columnIndex, int rowIndex) throws OutOfChoicesException {
        boolean anyAction = false;
        SudokuRow column = extractColumn(board, columnIndex);

        for (Integer valueFromTable : column.getElements().get(rowIndex).getRemainingChoices()) {
            boolean isInDifferentField = isInDifferentField(column, valueFromTable);

            if (isInDifferentField) column.getElements().get(rowIndex).removeChoice(valueFromTable);

            if (column.getElements().get(rowIndex).getRemainingChoices().size() == 1) {
                boolean isInDifferentRowField = isInDifferentField(board.getRows().get(rowIndex),
                        column.getElements().get(rowIndex).getRemainingChoices().get(0));
                boolean isInDifferentSectionField = isInDifferentSection(board, rowIndex, columnIndex,
                        column.getElements().get(rowIndex).getRemainingChoices().get(0));
                thirdOption(column, rowIndex, isInDifferentRowField, isInDifferentSectionField);
                column.getElements().get(rowIndex).setValue(column.getElements().get(rowIndex).getRemainingChoices().get(0));
                anyAction = true;
                injectColumn(column, board, columnIndex);
                break;
            }
            boolean isInDifferentArray = isInDifferentArray(column, valueFromTable, column.getElements().get(rowIndex));
            if (!isInDifferentField && !isInDifferentArray /*&& !isInDifferentColumnField && !isInDifferentSectionField*/) {
                column.getElements().get(rowIndex).setValue(valueFromTable);
                anyAction = true;
                injectColumn(column, board, columnIndex);
                break;
            }
        }
        return anyAction;
    }

    public void injectColumn(SudokuRow row, SudokuBoard board, int index) {

        for (int i = 0; i < board.getRows().size(); i++) {
            board.getRows().get(i).getElements().add(index, row.getElements().get(i));
            board.getRows().get(i).getElements().remove(index + 1);
        }
    }
}
