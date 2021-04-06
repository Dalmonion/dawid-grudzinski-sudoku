# Table of contents
* [General info](#general-info)
* [Game navigation](#game-navigation)
* [Board](#board)
* [Algorithm](#algorithm)
* [Guessing procedure](#guessing-procedure)
* [OutOfChoicesException](#outofchoicesexception)
* [Technologies](#technologies)
* [Setup](#setup)

# General info
I present to you a Sudoku resolver, where You decide where and how many numbers you want to add to the board.
If it's possible to solve, application will print out solution to the console or inform that it cannot
be solved.

# Game navigation
It's very straightforward- console accepts values in correct format. In such case example looks like this "1,2,3" where:
1 means column, 2 mean row and 3 is value to put into the board. Keep in mind you have to use comas, or the algorithm won't
put values into the board. Every value being passed to the board and printed to console as whole.
The table is numbered from 1. Next, when the board is ready you have to type: "sudoku" to begin the process.


# Board
The board is 9x9 elements grid. 


# Algorithm
The algorithm moves through the board field by field. Ever field has 2 variables. First represents the number which is 
displayed, and the second is a list with possible remaining choices which can be passed in present state into the field.
- if field has already a value, the algorithm jumps into next field. For every field in row, for every value from 
  remaining choice's array, algorithm checks if in another field there is the same value. If not, the algorithm deletes
  value from possible remaining choices array,
- if there is only one value remains in remaining choices array algorithm checks for possible present of such value in 
  column and section (3x3 piece) which complies to row index. If it finds such value, algorithm returns
  an exception [OutOfChoicesException](#outofchoicesexception). If not, the value is set in a current field, 
- if present value from remaining choices array is not present in a different field, and it's also impossible to write 
  it in a different field (there's no present of such value in different fields remaining choices array), the value is 
  being assigned to current field. If there's only one option remaining in remaining choices array, and it's also in a 
  different field, algorithm return exception,
- the algorithm repeats operation every field in column and then in the section,
- if algorithm passed all above operation without proceeding with neither, the [Guessing procedure](#guessing-procedure) begins,
- if all field are filled, the algorithm finishes it work and returns the board. 

# Guessing procedure
If there is more than one option in remaining choices array, the algorithm needs to pick one value from an array and 
proceed with the [algorithm](#algorithm). It's followed by steps:
- algorithm picks one vale from remaining choices array from the field which is empty,
- backtrack object which contains current board state (deepCopy), field location and guessed value is being created and 
  stored in the array,
- picked value is being assigned to the field and the [algorithm](#algorithm) continues.

# OutOfChoicesException
In case such an exception occurs it's being handled in followed way:
- if there are objects in the backtrack array, the last position is being opened,
- the board state is being loaded from that object,
- before guessed value is being deleted from the present field (after restoring board state) remaining choices array,
- [algorithm](#algorithm) 

If backtrack objects array is empty, the algorithm will inform the user that entered board is incorrect.

# Technologies
Project is created with:
* IntelliJ IDEA version: 2020.2.3 x64
* JUnit Jupiter Engine version: 5.6.2
* JUnit Jupiter API version: 5.6.2
* Mockito JUnit Jupiter version: 3.3.3
* Gradle version: 6.7.1

# Setup
To run this game, simply run main method from Application class

