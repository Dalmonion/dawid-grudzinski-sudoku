package com.sudoku;

public class RowHandler extends BoardHandler {

    public boolean processRow(SudokuBoard board, SudokuRow row, int columnIndex, int rowIndex) throws OutOfChoicesException {
        boolean anyAction = false;
        for (Integer valueFromTable : row.getElements().get(columnIndex).getRemainingChoices()) {
            boolean isInDifferentField = isInDifferentField(row, valueFromTable);
            if (isInDifferentField) row.getElements().get(columnIndex).removeChoice(valueFromTable);
            if (row.getElements().get(columnIndex).getRemainingChoices().size() == 1) {
                boolean isInDifferentColumnField = isInDifferentColumn(board, columnIndex,
                        row.getElements().get(columnIndex).getRemainingChoices().get(0));
                boolean isInDifferentSectionField = isInDifferentSection(board, rowIndex, columnIndex,
                        row.getElements().get(columnIndex).getRemainingChoices().get(0));
                thirdOption(row, columnIndex, isInDifferentColumnField, isInDifferentSectionField);
                row.getElements().get(columnIndex).setValue(row.getElements().get(columnIndex).getRemainingChoices().get(0));
                anyAction = true;
                break;
            }
            boolean isInDifferentArray = isInDifferentArray(row, valueFromTable, row.getElements().get(columnIndex));
            if (!isInDifferentField && !isInDifferentArray /*&& !isInDifferentColumnField && !isInDifferentSectionField*/) {
                boolean isInDifferentColumnField = isInDifferentColumn(board, columnIndex,
                        row.getElements().get(columnIndex).getRemainingChoices().get(0));
                boolean isInDifferentSectionField = isInDifferentSection(board, rowIndex, columnIndex,
                        row.getElements().get(columnIndex).getRemainingChoices().get(0));
                thirdOption(row, columnIndex, isInDifferentColumnField, isInDifferentSectionField);
                row.getElements().get(columnIndex).setValue(valueFromTable);
                anyAction = true;
                break;
            }
        }
        return anyAction;
    }






}
