package com.sudoku.command;

import com.sudoku.model.Game;
import com.sudoku.model.InvalidMoveException;
import com.sudoku.model.Position;

/** Empties a cell, e.g. {@code C5 clear}. */
public record ClearCommand(Position position) implements Command {

    @Override
    public CommandResult execute(Game game) {
        try {
            game.clear(position);
            return CommandResult.withGrid("Cell " + position.label() + " cleared.");
        } catch (InvalidMoveException e) {
            return CommandResult.withGrid("Invalid move. " + e.getMessage());
        }
    }
}
