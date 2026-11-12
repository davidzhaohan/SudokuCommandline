package com.sudoku.ui;

import com.sudoku.TestPuzzles;
import com.sudoku.model.Grid;
import com.sudoku.model.Position;
import com.sudoku.model.Puzzle;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/** End-to-end tests that drive the console with scripted input. */
class ConsoleAppTest {

    private String play(String... lines) {
        String input = String.join("\n", lines) + "\n";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        new ConsoleApp(TestPuzzles.source(), new Random(1), new BufferedReader(new StringReader(input)), out).run();
        return bytes.toString(StandardCharsets.UTF_8);
    }

    private static int count(String text, String part) {
        return text.split(java.util.regex.Pattern.quote(part), -1).length - 1;
    }

    @Test
    void showsWelcomeAndInitialPuzzle() {
        String output = play("quit");

        assertTrue(output.startsWith("Welcome to Sudoku!"));
        assertTrue(output.contains("Here is your puzzle:"));
        assertTrue(output.contains("  A 5 3 _ _ 7 _ _ _ _"));
        assertTrue(output.contains("Enter command (e.g., A3 4, C5 clear, hint, check, quit):"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void acceptedMoveShowsCurrentGrid() {
        String output = play("A3 4", "quit");

        assertTrue(output.contains("Move accepted."));
        assertTrue(output.contains("Current grid:"));
        assertTrue(output.contains("  A 5 3 4 _ 7 _ _ _ _"));
    }

    @Test
    void prefilledCellIsRejected() {
        String output = play("A1 6", "quit");

        assertTrue(output.contains("Invalid move. A1 is pre-filled."));
        assertTrue(output.contains("  A 5 3 _ _ 7 _ _ _ _"));
    }

    @Test
    void checkReportsViolation() {
        String output = play("A3 3", "check", "quit");
        assertTrue(output.contains("Number 3 already exists in Row A."));
    }

    @Test
    void checkReportsNoViolations() {
        String output = play("A3 4", "check", "quit");
        assertTrue(output.contains("No rule violations detected."));
    }

    @Test
    void hintRevealsANumber() {
        String output = play("hint", "quit");
        assertTrue(output.matches("(?s).*Hint: Cell [A-I][1-9] = [1-9].*"));
    }

    @Test
    void invalidInputShowsHelpfulMessageAndKeepsPlaying() {
        String output = play("banana", "A3 x", "Z1 4", "quit");

        assertTrue(output.contains("Unrecognised command 'banana'"));
        assertTrue(output.contains("'x' is not a number"));
        assertTrue(output.contains("'z1' is not a valid cell"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void numberOutOfRangeIsRejected() {
        String output = play("A3 10", "quit");
        assertTrue(output.contains("Invalid move. Number must be between 1 and 9."));
    }

    @Test
    void endsGracefullyWhenInputEnds() {
        assertDoesNotThrow(() -> play());
    }

    @Test
    void completingThePuzzleEndsTheGameAndOffersReplay() {
        Puzzle puzzle = TestPuzzles.classic();
        String[] moves = solvingMoves(puzzle);

        String[] script = new String[moves.length + 2];
        System.arraycopy(moves, 0, script, 0, moves.length);
        script[moves.length] = "";      // press Enter to play again
        script[moves.length + 1] = "quit";

        String output = play(script);

        assertEquals(1, count(output, "You have successfully completed the Sudoku puzzle!"));
        assertEquals(2, count(output, "Here is your puzzle:"), "a new game should start after replay");
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void doesNotCompleteWhileGridIsFullButInvalid() {
        Puzzle puzzle = TestPuzzles.classic();
        String[] moves = solvingMoves(puzzle);
        moves[moves.length - 1] = lastMoveWrong(puzzle);

        String[] script = new String[moves.length + 1];
        System.arraycopy(moves, 0, script, 0, moves.length);
        script[moves.length] = "quit";

        String output = play(script);

        assertFalse(output.contains("successfully completed"));
    }

    private static String[] solvingMoves(Puzzle puzzle) {
        return Position.all().stream()
                .filter(p -> puzzle.initial().isEmpty(p))
                .map(p -> p.label() + " " + puzzle.solution().get(p))
                .toArray(String[]::new);
    }

    private static String lastMoveWrong(Puzzle puzzle) {
        Position last = Position.all().stream()
                .filter(p -> puzzle.initial().isEmpty(p))
                .reduce((a, b) -> b).orElseThrow();
        int wrong = puzzle.solution().get(last) % Grid.MAX_VALUE + 1;
        return last.label() + " " + wrong;
    }
}
