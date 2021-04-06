package com.sudoku;

public class SudokuGame {

    private final SudokuBoard board;
    private final InputChoiceInterpreter inputChoiceInterpreter = new InputChoiceInterpreter();
    private final GameProcessor processor = new GameProcessor();

    public SudokuGame() {
        this.board = new SudokuBoard();
    }

    public boolean resolveSudoku() {
        System.out.println(board.toString());
        IOService.welcomeMessage();
        String input = "";
        while (true) {
            input = inputChoiceInterpreter.input(IOService.choiceInput());
            if (input.equalsIgnoreCase("sudoku")) {
                break;
            }
            if (input.length() == 3) {
                board.updateBoard(input);
                IOService.printBoard(board);
            }
        }

        SudokuResult result = processor.process(board);
        if(result.isResolved()) {
            IOService.afterResolve();
            IOService.printBoard(result.getBoard());
        }

        IOService.nextGameTypo();
        while (true) {
            input = IOService.continuePlay();
            if (input.equalsIgnoreCase("")) {
                return false;
            } else {
                return true;
            }
        }
    }
}
