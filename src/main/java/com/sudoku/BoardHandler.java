package com.sudoku;

public class BoardHandler {
    public boolean isInDifferentField(SudokuRow row, int value) {
        return row.getElements().stream()
                .filter(e -> e.getValue() == value)
                .anyMatch(e -> e.getValue() == value);
    }

    public boolean isInDifferentArray(SudokuRow row, int value, SudokuElement element) {
        return row.getElements().stream()
                .filter(e -> e != element && e.getValue() == -1)
                .flatMap(e -> e.getRemainingChoices().stream())
                .anyMatch(e -> e == value);
    }

    public boolean isInDifferentColumn(SudokuBoard board, int columnIndex, int value) {
        SudokuRow column = extractColumn(board, columnIndex);
        return isInDifferentField(column, value);
    }

    public boolean isInDifferentSection(SudokuBoard board, int rowIndex, int columnIndex, int value) {
        SudokuRow section = extractSection(board, rowIndex, columnIndex);
        return isInDifferentField(section, value);
    }

    public void secondOptionNowe(SudokuRow row, int index) {
        for (Integer valueFromTable : row.getElements().get(index).getRemainingChoices()) {
            boolean isInDifferentField = isInDifferentField(row, valueFromTable);
            boolean isInDifferentArray = isInDifferentArray(row, valueFromTable, row.getElements().get(index));
            if (!isInDifferentField && !isInDifferentArray) {
                row.getElements().get(index).setValue(valueFromTable);
                break;
            }
        }
    }

    public void thirdOption(SudokuRow row, int index, boolean isInDifferentColumnField, boolean isInDifferentSectionField) throws OutOfChoicesException {
        if (isInDifferentField(row, row.getElements().get(index).getRemainingChoices().get(0))
                || isInDifferentColumnField || isInDifferentSectionField) {
            throw new OutOfChoicesException();
        }
    }

    public SudokuRow extractColumn(SudokuBoard board, int columnNumber) {
        SudokuRow column = new SudokuRow();
        column.getElements().clear();
        for (int i = 0; i < board.getRows().size(); i++) {
            column.getElements().add(board.getRows().get(i).getElements().get(columnNumber));
        }
        return column;
    }

    public int[] sectionGuesser(int row, int column) {
        int sectionColumnNumber = -1;
        int sectionRowNumber = -1;
        if (row < 3) {
            if (column < 3) {
                sectionColumnNumber = 0;
                sectionRowNumber = 0;
            } else if (column < 6) {
                sectionColumnNumber = 1;
                sectionRowNumber = 0;
            } else {
                sectionColumnNumber = 2;
                sectionRowNumber = 0;
            }
        } else if (row < 6) {
            if (column < 3) {
                sectionColumnNumber = 0;
                sectionRowNumber = 1;
            } else if (column < 6) {
                sectionColumnNumber = 1;
                sectionRowNumber = 1;
            } else {
                sectionColumnNumber = 2;
                sectionRowNumber = 1;
            }
        } else {
            if (column < 3) {
                sectionColumnNumber = 0;
                sectionRowNumber = 2;
            } else if (column < 6) {
                sectionColumnNumber = 1;
                sectionRowNumber = 2;
            } else {
                sectionColumnNumber = 2;
                sectionRowNumber = 2;
            }
        }
        return new int[]{sectionColumnNumber, sectionRowNumber};
    }

    public SudokuRow extractSection(SudokuBoard board, int row, int column) {
        SudokuRow section = new SudokuRow();
        section.getElements().clear();
        int[] guesser = sectionGuesser(row, column);
        int sectionColumnNumber = guesser[0];
        int sectionRowNumber = guesser[1];

        for (int i = 3 * sectionRowNumber; i < 3 + (3 * sectionRowNumber); i++) {
            for (int j = 3 * sectionColumnNumber; j < 3 + (3 * sectionColumnNumber); j++) {
                section.getElements().add(board.getRows().get(i).getElements().get(j));
            }
        }
        return section;
    }
}
