package com.sudoku.command;

import com.sudoku.model.Game;
import com.sudoku.model.Hint;

/** Reveals the correct number for one cell that is not yet correct. */
public final class HintCommand implements Command {

    @Override
    public CommandResult execute(Game game) {
        return game.hint()
                .map(HintCommand::describe)
                .orElseGet(() -> CommandResult.messageOnly("No hint available: every cell is already correct."));
    }

    private static CommandResult describe(Hint hint) {
        return CommandResult.messageOnly("Hint: Cell " + hint.position().label() + " = " + hint.value());
    }
}
