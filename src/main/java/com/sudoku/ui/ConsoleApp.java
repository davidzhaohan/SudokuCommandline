package com.sudoku.ui;

import com.sudoku.command.Command;
import com.sudoku.command.CommandParseException;
import com.sudoku.command.CommandParser;
import com.sudoku.command.CommandResult;
import com.sudoku.model.Game;
import com.sudoku.model.PuzzleSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.Random;

/** Runs Sudoku games on a text console: reads commands, runs them against the game and prints the results. */
public final class ConsoleApp {

    private static final String PROMPT = "Enter command (e.g., A3 4, C5 clear, hint, check, quit):";

    private final PuzzleSource puzzles;
    private final Random random;
    private final BufferedReader in;
    private final PrintStream out;
    private final CommandParser parser = new CommandParser();
    private final GridRenderer renderer = new GridRenderer();

    public ConsoleApp(PuzzleSource puzzles, Random random, BufferedReader in, PrintStream out) {
        this.puzzles = puzzles;
        this.random = random;
        this.in = in;
        this.out = out;
    }

    /** Plays games until the player quits or the input ends. */
    public void run() {
        out.println("Welcome to Sudoku!");
        boolean playAgain;
        do {
            playAgain = playGame();
        } while (playAgain);
    }

    /** @return true if the puzzle was completed and the player wants another game */
    private boolean playGame() {
        Game game = new Game(puzzles.next(), random);
        out.println();
        out.println("Here is your puzzle:");
        out.print(renderer.render(game.grid()));
        while (true) {
            out.println();
            out.println(PROMPT);
            String line = readLine();
            if (line == null) {
                return false;
            }
            CommandResult result = execute(game, line);
            if (result == null) {
                continue;
            }
            print(game, result);
            if (result.quit()) {
                return false;
            }
            if (game.isComplete()) {
                return congratulateAndAskToReplay();
            }
        }
    }

    private CommandResult execute(Game game, String line) {
        try {
            Command command = parser.parse(line);
            return command.execute(game);
        } catch (CommandParseException e) {
            out.println();
            out.println(e.getMessage());
            return null;
        }
    }

    private void print(Game game, CommandResult result) {
        out.println();
        out.println(result.message());
        if (result.showGrid()) {
            out.println();
            out.println("Current grid:");
            out.print(renderer.render(game.grid()));
        }
    }

    private boolean congratulateAndAskToReplay() {
        out.println();
        out.println("You have successfully completed the Sudoku puzzle!");
        out.println("Press Enter to play again (or Ctrl+D to exit)...");
        return readLine() != null;
    }

    private String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
