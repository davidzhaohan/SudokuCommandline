package com.sudoku.command;

/** Thrown when the player's input cannot be understood as a command. */
public class CommandParseException extends Exception {

    public CommandParseException(String message) {
        super(message);
    }
}
