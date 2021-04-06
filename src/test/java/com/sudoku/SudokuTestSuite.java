package com.sudoku;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class SudokuTestSuite {

    public static class BoardChecker {
        private final BoardHandler handler = new BoardHandler();
        public void checkBoard(SudokuResult result, GameProcessor processor) {
            List<Integer> testRow = new ArrayList<>();
            for (int k = 1; k < 10; k++) {
                testRow.add(k);
            }
            for (int i = 0; i < result.getBoard().getRows().size(); i++) {
                SudokuRow row = result.getBoard().getRows().get(i);
                List<Integer> rowValues = new ArrayList<>();
                for (int j = 0; j < result.getBoard().getRows().get(i).getElements().size(); j++) {
                    SudokuRow section = handler.extractSection(result.getBoard(), i, j);
                    SudokuRow column = handler.extractColumn(result.getBoard(), j);
                    List<Integer> columnValues = new ArrayList<>();
                    List<Integer> sectionValues = new ArrayList<>();
                    for (int k = 0; k < 9; k++) {
                        columnValues.add(column.getElements().get(k).getValue());
                        rowValues.add(row.getElements().get(k).getValue());
                        sectionValues.add(section.getElements().get(k).getValue());
                    }
                    for (int k = 0; k < 9;  k++) {
                        if (rowValues.contains(testRow.get(k))) rowValues.remove(Integer.valueOf(k + 1));
                        if (columnValues.contains(testRow.get(k))) columnValues.remove(Integer.valueOf(k + 1));
                        if (sectionValues.contains(testRow.get(k))) sectionValues.remove(Integer.valueOf(k + 1));
                    }
                    assertEquals(0, rowValues.size());
                    assertEquals(0, columnValues.size());
                    assertEquals(0, sectionValues.size());
                }
            }
        }
    }

    @DisplayName("Testing the isInDifferentField method")
    @Test
    void testIsInDifferentField() {
        //Given
        BoardHandler handler = new BoardHandler();
        SudokuRow row = new SudokuRow();
        IntStream.iterate(0, n -> n + 1)
                .limit(8)
                .forEach(n -> row.getElements().get(n).setValue(1 + n));

        //When & Then
        assertFalse(handler.isInDifferentField(row, 9));
    }

    @DisplayName("Testing if algorithm adds last missing value on the end of the row")
    @Test
    void testRowLast() {
        //Given
        RowHandler rowHandler = new RowHandler();
        SudokuBoard board = new SudokuBoard();
        board.getRows().remove(0);
        SudokuRow row = new SudokuRow();
        IntStream.iterate(0, n -> n + 1)
                .limit(8)
                .forEach(n -> row.getElements().get(n).setValue(1 + n));
        board.getRows().add(0, row);

        //When
        try {
            rowHandler.processRow(board, row, 8, 0);
        } catch (OutOfChoicesException e) {
            System.out.println(e);
        }

        //Then
        assertEquals(9, row.getElements().get(8).getValue());
        assertEquals(0, row.getElements().get(8).getRemainingChoices().size());
    }

    @DisplayName("Testing if algorithm adds last missing value on the beginning of the row")
    @Test
    void testRowFirst() {
        //Given
        RowHandler rowHandler = new RowHandler();
        SudokuRow row = new SudokuRow();
        SudokuBoard board = new SudokuBoard();
        board.getRows().remove(0);
        IntStream.iterate(1, n -> n + 1)
                .limit(8)
                .forEach(n -> row.getElements().get(n).setValue(1 + n));

        //When
        try {
            rowHandler.processRow(board, row, 0, 0);
        } catch (OutOfChoicesException e) {
            System.out.println(e);
        }

        //Then
        assertEquals(1, row.getElements().get(0).getValue());

    }

    @DisplayName("Testing if algorithm adds last missing value on the middle of the row")
    @Test
    void testRowMiddle() {
        //Given
        RowHandler rowHandler = new RowHandler();
        SudokuRow row = new SudokuRow();
        SudokuBoard board = new SudokuBoard();
        board.getRows().remove(0);
        IntStream.iterate(0, n -> n + 1)
                .limit(9)
                .forEach(n -> row.getElements().get(n).setValue(1 + n));
        row.getElements().get(4).setValue(-1);
        board.getRows().add(0, row);
        //When
        try {
            rowHandler.processRow(board, board.getRows().get(0), 4, 0);
        } catch (OutOfChoicesException e) {
            System.out.println(e);
        }

        //Then
        assertEquals(5, row.getElements().get(4).getValue());

    }

    @DisplayName("Testing if injectColumn method injects column in correct way")
    @Test
    void testColumnInjection() {
        //Given
        SudokuBoard board = new SudokuBoard();
        SudokuRow row = new SudokuRow();
        ColumnHandler handler = new ColumnHandler();

        for (int i = 0; i < 9; i++) {
            board.getRows().get(i).getElements().clear();
            for (int j = 0; j < 9; j++) {
                board.getRows().get(i).getElements().add(new SudokuElement(j + 1));
            }
        }

        for (int i = 0; i < 9; i++) {
            row.getElements().get(i).setValue(i + 1);
        }

        //When
        handler.injectColumn(row, board, 4);

        //Then
        assertEquals(1, board.getRows().get(0).getElements().get(4).getValue());
        assertEquals(9, board.getRows().get(8).getElements().get(4).getValue());
        assertEquals(9, board.getRows().get(0).getElements().size());
    }

    @DisplayName("Testing if extractColumn method extracts column in correct way")
    @Test
    void testColumnExtraction() {
        //Given
        SudokuBoard board = new SudokuBoard();
        BoardHandler handler = new BoardHandler();

        for (int i = 0; i < 9; i++) {
            board.getRows().get(i).getElements().clear();
            for (int j = 0; j < 9; j++) {
                board.getRows().get(i).getElements().add(new SudokuElement(j + 1));
            }
        }

        //When
        SudokuRow resultAtIndexZero = handler.extractColumn(board, 0);
        SudokuRow resultAtIndexFour = handler.extractColumn(board, 4);
        boolean containsOnlyOneValues = resultAtIndexZero.getElements().stream()
                .map(SudokuElement::getValue)
                .allMatch(e -> e == 1);
        boolean containsOnlyFiveValues = resultAtIndexFour.getElements().stream()
                .map(SudokuElement::getValue)
                .allMatch(e -> e == 5);

        //Then
        assertEquals(9, resultAtIndexZero.getElements().size());
        assertFalse(resultAtIndexZero.getElements().contains(1));
        assertTrue(containsOnlyOneValues);
        assertTrue(containsOnlyFiveValues);

    }

    @DisplayName("Testing if processColumn method moves values to main board column in correct order")
    @Test
    void testUpdateColumn() {
        //Given
        SudokuBoard board = new SudokuBoard();
        ColumnHandler columnHandler = new ColumnHandler();

        for (int i = 0; i < board.getRows().size(); i++) {
            board.getRows().get(i).getElements().get(0).setValue(1 + i);
        }
        board.getRows().get(3).getElements().get(0).setValue(-1);
        //When

        try {
            columnHandler.processColumn(board, 0, 3);
        } catch (OutOfChoicesException e) {
            System.out.println(e);
        }

        //Then
        assertEquals(4, board.getRows().get(3).getElements().get(0).getValue());
    }

    @DisplayName("Testing if extractSection method extracts section in correct way")
    @Test
    void testExtractSection() {
        //Given
        SudokuBoard board = new SudokuBoard();
        BoardHandler handler = new BoardHandler();

        for (int i = 0; i < 9; i++) {
            board.getRows().get(i).getElements().clear();
            for (int j = 0; j < 9; j++) {
                board.getRows().get(i).getElements().add(new SudokuElement(j + 1 + 9 * i));
            }
        }

        //When
        SudokuRow resultAtSectionTopLeft = handler.extractSection(board, 0, 0);
        SudokuRow resultAtSectionMiddle = handler.extractSection(board, 4, 4);
        SudokuRow resultAtSectionBottomRight = handler.extractSection(board, 8, 8);

        //Then
        assertEquals(9, resultAtSectionTopLeft.getElements().size());
        assertEquals(1, resultAtSectionTopLeft.getElements().get(0).getValue());
        assertEquals(41, resultAtSectionMiddle.getElements().get(4).getValue());
        assertEquals(81, resultAtSectionBottomRight.getElements().get(8).getValue());
    }

    @DisplayName("Testing if injectSection method injects section in correct way")
    @Test
    void testSectionInjection() {
        //Given
        SudokuBoard board = new SudokuBoard();
        SudokuRow row = new SudokuRow();
        SectionHandler handler = new SectionHandler();
        for (int i = 0; i < 9; i++) {
            board.getRows().get(i).getElements().clear();
            for (int j = 0; j < 9; j++) {
                board.getRows().get(i).getElements().add(new SudokuElement(j + 1));
            }
        }
        for (int i = 0; i < 9; i++) {
            row.getElements().get(i).setValue(i + 9);
        }

        //When
        handler.injectSection(row, board, 8, 8);

        //Then
        assertEquals(9, board.getRows().get(6).getElements().get(6).getValue());
        assertEquals(13, board.getRows().get(7).getElements().get(7).getValue());
        assertEquals(17, board.getRows().get(8).getElements().get(8).getValue());
    }

    @DisplayName("Testing if processSection method moves values to main board section in correct order")
    @Test
    void testUpdateSection() {
        //Given
        SudokuBoard board = new SudokuBoard();
        SudokuRow row = new SudokuRow();
        SectionHandler handler = new SectionHandler();
        for (int i = 0; i < 9; i++) {
            row.getElements().get(i).setValue(i + 1);
        }
        handler.injectSection(row, board, 4, 4);
        board.getRows().get(4).getElements().get(4).setValue(-1);

        //When
        try {
            handler.processSection(board, 4, 4);
        } catch (OutOfChoicesException e) {
            System.out.println(e);
        }

        //Then
        assertEquals(5, board.getRows().get(4).getElements().get(4).getValue());
    }

    @DisplayName("Testing if algorithm adds first value from remaining choices array where there is no " +
            "use of such value neither in all remaining fields or theirs remainingChoice arrays")
    @Test
    void testSecondOption() {
        //Given
        BoardHandler handler = new BoardHandler();
        SudokuRow row = new SudokuRow();
        row.getElements().get(0).setValue(1);
        row.getElements().get(1).setValue(2);
        row.getElements().get(3).setValue(3);
        row.getElements().get(4).setValue(5);
        row.getElements().get(5).setValue(6);
        row.getElements().get(6).setValue(7);
        row.getElements().get(7).setValue(8);
        row.getElements().get(8).setValue(8);

        //When
        handler.secondOptionNowe(row, 2);

        //Then
        assertEquals(4, row.getElements().get(2).getValue());
    }

    @DisplayName("Testing if algorithm throws \"OutOfChoicesException\" when field has only one option in remainingChoices array" +
            "which has been used in different place in row, column or section")
    @Test
    void testThrowOutOfChoicesException() {
        //Given
        SudokuRow row = new SudokuRow();
        BoardHandler handler = new BoardHandler();
        for (int i = 0; i < row.getElements().size(); i++) {
            row.getElements().get(i).setValue(1 + i);
        }
        row.getElements().get(3).setValue(-1);
        for (int i = 1; i < 10; i++) {
            if (i == 4) continue;
            row.getElements().get(3).removeChoice(i);
        }
        row.getElements().get(8).setValue(4);

        //When & Then
        assertThrows(OutOfChoicesException.class, () -> handler.thirdOption(row, 3, false, false));
    }

    @DisplayName("Testing if board is cloned correctly")
    @Test
    void testBoardClone() {
        //Given
        SudokuBoard board = new SudokuBoard();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.getRows().get(i).getElements().get(j).setValue(j + 1);
            }
        }
        //When
        SudokuBoard clonedBoard = null;
        try {
            clonedBoard = board.deepCopy();
        } catch (CloneNotSupportedException e) {
            System.out.println(e);
        }
        board.getRows().remove(0);
        for (int i = 0; i < board.getRows().size(); i++) {
            board.getRows().get(i).getElements().remove(8);
        }
        //Then
        assertNotEquals(clonedBoard.getRows().size(), board.getRows().size());
    }

    @DisplayName("Testing process method for solving basic sudoku example board")
    @Test
    void testProcess() {
        //Given
        GameProcessor processor = new GameProcessor();
        SudokuBoard board = new SudokuBoard();
        BoardChecker checker = new BoardChecker();

        board.getRows().get(0).getElements().get(3).setValue(2);
        board.getRows().get(0).getElements().get(4).setValue(6);
        board.getRows().get(0).getElements().get(6).setValue(7);
        board.getRows().get(0).getElements().get(8).setValue(1);
        board.getRows().get(1).getElements().get(0).setValue(6);
        board.getRows().get(1).getElements().get(1).setValue(8);
        board.getRows().get(1).getElements().get(4).setValue(7);
        board.getRows().get(1).getElements().get(7).setValue(9);
        board.getRows().get(2).getElements().get(0).setValue(1);
        board.getRows().get(2).getElements().get(1).setValue(9);
        board.getRows().get(2).getElements().get(5).setValue(4);
        board.getRows().get(2).getElements().get(6).setValue(5);
        board.getRows().get(3).getElements().get(0).setValue(8);
        board.getRows().get(3).getElements().get(1).setValue(2);
        board.getRows().get(3).getElements().get(3).setValue(1);
        board.getRows().get(3).getElements().get(7).setValue(4);
        board.getRows().get(4).getElements().get(2).setValue(4);
        board.getRows().get(4).getElements().get(3).setValue(6);
        board.getRows().get(4).getElements().get(5).setValue(2);
        board.getRows().get(4).getElements().get(6).setValue(9);
        board.getRows().get(5).getElements().get(1).setValue(5);
        board.getRows().get(5).getElements().get(5).setValue(3);
        board.getRows().get(5).getElements().get(7).setValue(2);
        board.getRows().get(5).getElements().get(8).setValue(8);
        board.getRows().get(6).getElements().get(2).setValue(9);
        board.getRows().get(6).getElements().get(3).setValue(3);
        board.getRows().get(6).getElements().get(7).setValue(7);
        board.getRows().get(6).getElements().get(8).setValue(4);
        board.getRows().get(7).getElements().get(1).setValue(4);
        board.getRows().get(7).getElements().get(4).setValue(5);
        board.getRows().get(7).getElements().get(7).setValue(3);
        board.getRows().get(7).getElements().get(8).setValue(6);
        board.getRows().get(8).getElements().get(0).setValue(7);
        board.getRows().get(8).getElements().get(2).setValue(3);
        board.getRows().get(8).getElements().get(4).setValue(1);
        board.getRows().get(8).getElements().get(5).setValue(8);

        //When
        SudokuResult result = processor.process(board);

        //Then
        checker.checkBoard(result, processor);
    }

    @DisplayName("Testing process method for solving basic sudoku example board 2")
    @Test
    void testProcessTwo() {
        //Given
        GameProcessor processor = new GameProcessor();
        SudokuBoard board = new SudokuBoard();
        BoardChecker checker = new BoardChecker();

        board.getRows().get(0).getElements().get(1).setValue(2);
        board.getRows().get(0).getElements().get(3).setValue(6);
        board.getRows().get(0).getElements().get(5).setValue(8);
        board.getRows().get(1).getElements().get(0).setValue(5);
        board.getRows().get(1).getElements().get(1).setValue(8);
        board.getRows().get(1).getElements().get(5).setValue(9);
        board.getRows().get(1).getElements().get(6).setValue(7);
        board.getRows().get(2).getElements().get(4).setValue(4);
        board.getRows().get(3).getElements().get(0).setValue(3);
        board.getRows().get(3).getElements().get(1).setValue(7);
        board.getRows().get(3).getElements().get(6).setValue(5);
        board.getRows().get(4).getElements().get(0).setValue(6);
        board.getRows().get(4).getElements().get(8).setValue(4);
        board.getRows().get(5).getElements().get(2).setValue(8);
        board.getRows().get(5).getElements().get(7).setValue(1);
        board.getRows().get(5).getElements().get(8).setValue(3);
        board.getRows().get(6).getElements().get(4).setValue(2);
        board.getRows().get(7).getElements().get(2).setValue(9);
        board.getRows().get(7).getElements().get(3).setValue(8);
        board.getRows().get(7).getElements().get(7).setValue(3);
        board.getRows().get(7).getElements().get(8).setValue(6);
        board.getRows().get(8).getElements().get(3).setValue(3);
        board.getRows().get(8).getElements().get(5).setValue(6);
        board.getRows().get(8).getElements().get(7).setValue(9);

        //When
        SudokuResult result = processor.process(board);

        //Then
        checker.checkBoard(result, processor);
    }

//    @DisplayName("Testing process method for solving basic sudoku example board 3")
//    @Test
//    void testProcessThree() {
//        //Given
//        GameProcessor processor = new GameProcessor();
//        SudokuBoard board = new SudokuBoard();
//        BoardChecker checker = new BoardChecker();
//
//        board.getRows().get(0).getElements().get(3).setValue(6);
//        board.getRows().get(0).getElements().get(6).setValue(4);
//        board.getRows().get(1).getElements().get(0).setValue(7);
//        board.getRows().get(1).getElements().get(5).setValue(3);
//        board.getRows().get(1).getElements().get(6).setValue(6);
//        board.getRows().get(2).getElements().get(4).setValue(9);
//        board.getRows().get(2).getElements().get(5).setValue(1);
//        board.getRows().get(2).getElements().get(7).setValue(8);
//        board.getRows().get(4).getElements().get(1).setValue(5);
//        board.getRows().get(4).getElements().get(3).setValue(1);
//        board.getRows().get(4).getElements().get(4).setValue(8);
//        board.getRows().get(4).getElements().get(8).setValue(3);
//        board.getRows().get(5).getElements().get(3).setValue(3);
//        board.getRows().get(5).getElements().get(5).setValue(6);
//        board.getRows().get(5).getElements().get(7).setValue(4);
//        board.getRows().get(5).getElements().get(8).setValue(5);
//        board.getRows().get(6).getElements().get(1).setValue(4);
//        board.getRows().get(6).getElements().get(3).setValue(2);
//        board.getRows().get(6).getElements().get(7).setValue(6);
//        board.getRows().get(7).getElements().get(0).setValue(9);
//        board.getRows().get(7).getElements().get(2).setValue(3);
//        board.getRows().get(8).getElements().get(1).setValue(2);
//        board.getRows().get(8).getElements().get(6).setValue(1);
//
//        //When
//        SudokuResult result = processor.process(board);
//
//        //Then
//        checker.checkBoard(result, processor);
//    }

    @DisplayName("Testing process method for solving basic sudoku example board 4")
    @Test
    void testProcessFour() {
        //Given
        GameProcessor processor = new GameProcessor();
        SudokuBoard board = new SudokuBoard();
        BoardChecker checker = new BoardChecker();

        board.getRows().get(0).getElements().get(1).setValue(2);
        board.getRows().get(1).getElements().get(3).setValue(6);
        board.getRows().get(1).getElements().get(8).setValue(3);
        board.getRows().get(2).getElements().get(1).setValue(7);
        board.getRows().get(2).getElements().get(2).setValue(4);
        board.getRows().get(2).getElements().get(4).setValue(8);
        board.getRows().get(3).getElements().get(5).setValue(3);
        board.getRows().get(3).getElements().get(8).setValue(2);
        board.getRows().get(4).getElements().get(1).setValue(8);
        board.getRows().get(4).getElements().get(4).setValue(4);
        board.getRows().get(4).getElements().get(7).setValue(1);
        board.getRows().get(5).getElements().get(0).setValue(6);
        board.getRows().get(5).getElements().get(3).setValue(5);
        board.getRows().get(6).getElements().get(4).setValue(1);
        board.getRows().get(6).getElements().get(6).setValue(7);
        board.getRows().get(6).getElements().get(7).setValue(8);
        board.getRows().get(7).getElements().get(0).setValue(5);
        board.getRows().get(7).getElements().get(5).setValue(9);
        board.getRows().get(8).getElements().get(7).setValue(4);
        System.out.println(board.toString());
        //When
        SudokuResult result = processor.process(board);
        System.out.println(result.getBoard().toString());

        //Then
        checker.checkBoard(result, processor);
    }
}
