package com.sudoku.command;

import com.sudoku.model.Game;
import com.sudoku.model.Violation;

import java.util.List;
import java.util.stream.Collectors;

/** Reports every duplicate number in the current grid, one per line. */
public final class CheckCommand implements Command {

    @Override
    public CommandResult execute(Game game) {
        List<Violation> violations = game.check();
        if (violations.isEmpty()) {
            return CommandResult.messageOnly("No rule violations detected.");
        }
        String message = violations.stream()
                .map(Violation::describe)
                .collect(Collectors.joining(System.lineSeparator()));
        return CommandResult.messageOnly(message);
    }
}
