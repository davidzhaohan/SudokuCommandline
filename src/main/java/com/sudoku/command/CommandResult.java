package com.sudoku.command;

/**
 * What a command wants shown to the player.
 *
 * @param message  text to print
 * @param showGrid whether the current grid should be printed after the message
 * @param quit     whether the player asked to leave the game
 */
public record CommandResult(String message, boolean showGrid, boolean quit) {

    public static CommandResult withGrid(String message) {
        return new CommandResult(message, true, false);
    }

    public static CommandResult messageOnly(String message) {
        return new CommandResult(message, false, false);
    }

    public static CommandResult exit() {
        return new CommandResult("Goodbye!", false, true);
    }
}
