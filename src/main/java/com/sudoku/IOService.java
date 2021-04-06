package com.sudoku;

import java.util.Scanner;


public class IOService {

    public static final Scanner scanner = new Scanner(System.in);

    public static void welcomeMessage() {
        System.out.println("Welcome to Sudoku Game!\n" +
                "Type numbers from 1 to 9 in specific order (This applies to columns, rows and values):\n" +
                "- example: 1,2,3 where 1 is column, 2 is row and 3 is specific number to put.\n" +
                "(Please remember about the comma between each number)\n" +
                "Then type \"SUDOKU\" to start game.");
    }

    public static void typeMistake() {
        System.out.println("Input type invalid. Please type in specific order:\n" +
        "example: 1,2,3 where 1 is column, 2 is row and 3 is specific number to put.\n" +
                "(Please remember about the comma between each number)\n");
    }

    public static String choiceInput() {
        System.out.print("Enter your choice: ");
        return scanner.nextLine();
    }

    public static void afterResolve() {
        System.out.println("Here is the solution:");
    }

    public static void nextGameTypo() {
        System.out.println("If you wish to play again, press enter...");
    }

    public static String continuePlay() {
        return scanner.nextLine();
    }

    public static void printBoard(SudokuBoard board) {
        System.out.print(board.toString());
    }
}
