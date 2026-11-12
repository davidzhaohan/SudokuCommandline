package com.sudoku.command;

import com.sudoku.model.Game;

/** Leaves the game. */
public final class QuitCommand implements Command {

    @Override
    public CommandResult execute(Game game) {
        return CommandResult.exit();
    }
}
