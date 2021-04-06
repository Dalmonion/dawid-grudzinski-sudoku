package com.sudoku;

public class SectionHandler extends BoardHandler {

    public boolean processSection(SudokuBoard board, int columnIndex, int rowIndex) throws OutOfChoicesException {
        boolean anyAction = false;
        SudokuRow section = extractSection(board, rowIndex, columnIndex);
        int valueIndex = sectionValueIndex(section);

        for (Integer valueFromTable : section.getElements().get(valueIndex).getRemainingChoices()) {
            boolean isInDifferentField = isInDifferentField(section, valueFromTable);

            if (isInDifferentField) section.getElements().get(valueIndex).removeChoice(valueFromTable);

            if (section.getElements().get(valueIndex).getRemainingChoices().size() == 1) {
                boolean isInDifferentColumnField = isInDifferentColumn(board, columnIndex,
                        valueFromTable);
                boolean isInDifferentRowField = isInDifferentField(board.getRows().get(rowIndex),
                        valueFromTable);
                thirdOption(section, valueIndex, isInDifferentColumnField, isInDifferentRowField);
                section.getElements().get(valueIndex).setValue(section.getElements().get(valueIndex).getRemainingChoices().get(0));
                injectSection(section, board, rowIndex, columnIndex);
                anyAction = true;
                break;
            }
            boolean isInDifferentArray = isInDifferentArray(section, valueFromTable, section.getElements().get(columnIndex));
            if (!isInDifferentField && !isInDifferentArray /*&& !isInDifferentColumnField && !isInDifferentSectionField*/) {

                section.getElements().get(columnIndex).setValue(valueFromTable);
                injectSection(section, board, rowIndex, columnIndex);
                anyAction = true;
                break;
            }
        }
        return anyAction;
    }

    public int sectionValueIndex(SudokuRow section) {
        int result = -1;
        for (int i = 0; i < section.getElements().size(); i++) {
            if (section.getElements().get(i).getValue() == -1) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void injectSection(SudokuRow row, SudokuBoard board, int rowIndex, int columnIndex) {
        int[] guesser = sectionGuesser(rowIndex, columnIndex);
        int sectionColumnNumber = guesser[0];
        int sectionRowNumber = guesser[1];

        for (int i = 3 * sectionRowNumber; i < 3 + (3 * sectionRowNumber); i++) {
            for (int j = 3 * sectionColumnNumber; j < 3 + (3 * sectionColumnNumber); j++) {
                board.getRows().get(i).getElements().add(j, row.getElements().get(0));
                row.getElements().remove(0);
                board.getRows().get(i).getElements().remove(j + 1);
            }
        }
    }


}
