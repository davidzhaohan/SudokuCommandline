package com.sudoku.model;

/** Thrown when a move breaks the rules of the game (e.g. changing a pre-filled cell). */
public class InvalidMoveException extends Exception {

    public InvalidMoveException(String message) {
        super(message);
    }
}
