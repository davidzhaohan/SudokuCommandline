package com.sudoku.command;

import com.sudoku.TestPuzzles;
import com.sudoku.model.Game;
import com.sudoku.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CommandsTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(TestPuzzles.classic(), new Random(1));
    }

    @Test
    void placeAcceptsValidMove() {
        CommandResult result = new PlaceCommand(Position.parse("A3"), 4).execute(game);

        assertEquals("Move accepted.", result.message());
        assertTrue(result.showGrid());
        assertEquals(4, game.grid().get(Position.parse("A3")));
    }

    @Test
    void placeRejectsPrefilledCell() {
        CommandResult result = new PlaceCommand(Position.parse("A1"), 6).execute(game);

        assertEquals("Invalid move. A1 is pre-filled.", result.message());
        assertTrue(result.showGrid());
    }

    @Test
    void placeRejectsNumberOutOfRange() {
        CommandResult result = new PlaceCommand(Position.parse("A3"), 12).execute(game);

        assertTrue(result.message().startsWith("Invalid move."));
        assertTrue(game.grid().isEmpty(Position.parse("A3")));
    }

    @Test
    void clearEmptiesCell() {
        new PlaceCommand(Position.parse("A3"), 4).execute(game);
        CommandResult result = new ClearCommand(Position.parse("A3")).execute(game);

        assertEquals("Cell A3 cleared.", result.message());
        assertTrue(game.grid().isEmpty(Position.parse("A3")));
    }

    @Test
    void clearRejectsPrefilledCell() {
        CommandResult result = new ClearCommand(Position.parse("A1")).execute(game);
        assertEquals("Invalid move. A1 is pre-filled.", result.message());
    }

    @Test
    void checkReportsNoViolations() {
        assertEquals("No rule violations detected.", new CheckCommand().execute(game).message());
    }

    @Test
    void checkReportsRowViolation() {
        new PlaceCommand(Position.parse("A3"), 3).execute(game);
        assertTrue(new CheckCommand().execute(game).message().contains("Number 3 already exists in Row A."));
    }

    @Test
    void checkReportsColumnViolation() {
        new PlaceCommand(Position.parse("C1"), 5).execute(game);
        assertTrue(new CheckCommand().execute(game).message().contains("Number 5 already exists in Column 1."));
    }

    @Test
    void checkReportsSubgridViolation() {
        new PlaceCommand(Position.parse("B3"), 8).execute(game);
        assertTrue(new CheckCommand().execute(game).message()
                .contains("Number 8 already exists in the same 3×3 subgrid."));
    }

    @Test
    void hintRevealsCellAndValue() {
        CommandResult result = new HintCommand().execute(game);

        assertTrue(result.message().matches("Hint: Cell [A-I][1-9] = [1-9]"), result.message());
        assertFalse(result.showGrid());
    }

    @Test
    void hintDoesNotChangeTheGrid() {
        new HintCommand().execute(game);
        assertEquals(30, game.grid().countFilled());
    }

    @Test
    void quitSignalsExit() {
        assertTrue(new QuitCommand().execute(game).quit());
    }

    @Test
    void otherCommandsDoNotQuit() {
        assertFalse(new CheckCommand().execute(game).quit());
        assertFalse(new HintCommand().execute(game).quit());
    }
}
