package com.sudoku.command;

import com.sudoku.model.Game;
import com.sudoku.model.InvalidMoveException;
import com.sudoku.model.Position;

/** Places a number in a cell, e.g. {@code B3 7}. */
public record PlaceCommand(Position position, int value) implements Command {

    @Override
    public CommandResult execute(Game game) {
        try {
            game.place(position, value);
            return CommandResult.withGrid("Move accepted.");
        } catch (InvalidMoveException e) {
            return CommandResult.withGrid("Invalid move. " + e.getMessage());
        }
    }
}
