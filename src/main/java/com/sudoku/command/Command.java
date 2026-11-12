package com.sudoku.command;

import com.sudoku.model.Game;

/** An action the player can perform during a game. */
public interface Command {

    CommandResult execute(Game game);
}
