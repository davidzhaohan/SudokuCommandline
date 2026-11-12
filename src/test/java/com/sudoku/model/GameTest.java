package com.sudoku.model;

import com.sudoku.TestPuzzles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(TestPuzzles.classic(), new Random(1));
    }

    @Test
    void startsWithPrefilledCells() {
        assertEquals(30, game.grid().countFilled());
        assertTrue(game.isPrefilled(Position.parse("A1")));
        assertFalse(game.isPrefilled(Position.parse("A3")));
    }

    @Test
    void placeFillsEmptyCell() throws InvalidMoveException {
        game.place(Position.parse("A3"), 4);
        assertEquals(4, game.grid().get(Position.parse("A3")));
    }

    @Test
    void placeCanOverwritePlayerEntry() throws InvalidMoveException {
        game.place(Position.parse("A3"), 4);
        game.place(Position.parse("A3"), 7);
        assertEquals(7, game.grid().get(Position.parse("A3")));
    }

    @Test
    void placeRejectsPrefilledCell() {
        InvalidMoveException e = assertThrows(InvalidMoveException.class, () -> game.place(Position.parse("A1"), 6));
        assertEquals("A1 is pre-filled.", e.getMessage());
        assertEquals(5, game.grid().get(Position.parse("A1")));
    }

    @Test
    void placeRejectsNumbersOutsideOneToNine() {
        Position a3 = Position.parse("A3");
        assertThrows(InvalidMoveException.class, () -> game.place(a3, 0));
        assertThrows(InvalidMoveException.class, () -> game.place(a3, 10));
        assertTrue(game.grid().isEmpty(a3));
    }

    @Test
    void clearEmptiesPlayerEntry() throws InvalidMoveException {
        game.place(Position.parse("A3"), 4);
        game.clear(Position.parse("A3"));
        assertTrue(game.grid().isEmpty(Position.parse("A3")));
    }

    @Test
    void clearRejectsPrefilledCell() {
        assertThrows(InvalidMoveException.class, () -> game.clear(Position.parse("A1")));
    }

    @Test
    void gridReturnsSnapshot() {
        Grid snapshot = game.grid();
        snapshot.set(Position.parse("A3"), 9);
        assertTrue(game.grid().isEmpty(Position.parse("A3")));
    }

    @Test
    void checkReportsViolationsCausedByMoves() throws InvalidMoveException {
        assertTrue(game.check().isEmpty());
        game.place(Position.parse("A3"), 3);
        assertFalse(game.check().isEmpty());
    }

    @Test
    void hintRevealsCorrectNumberForAnUnsolvedCell() {
        Hint hint = game.hint().orElseThrow();
        Grid solution = TestPuzzles.classic().solution();
        assertTrue(game.grid().isEmpty(hint.position()));
        assertEquals(solution.get(hint.position()), hint.value());
    }

    @Test
    void hintTargetsCellsThatAreWrong() throws InvalidMoveException {
        fillWithSolution();
        Position a3 = Position.parse("A3");
        game.place(a3, 9);

        Hint hint = game.hint().orElseThrow();

        assertEquals(a3, hint.position());
        assertEquals(4, hint.value());
    }

    @Test
    void hintIsEmptyWhenSolved() throws InvalidMoveException {
        fillWithSolution();
        assertEquals(Optional.empty(), game.hint());
    }

    @Test
    void isCompleteOnlyWhenFullAndValid() throws InvalidMoveException {
        assertFalse(game.isComplete());
        fillWithSolution();
        assertTrue(game.isComplete());
    }

    @Test
    void fullButInvalidGridIsNotComplete() throws InvalidMoveException {
        fillWithSolution();
        game.place(Position.parse("A3"), 9);
        assertFalse(game.isComplete());
    }

    private void fillWithSolution() throws InvalidMoveException {
        Grid solution = TestPuzzles.classic().solution();
        for (Position position : Position.all()) {
            if (!game.isPrefilled(position)) {
                game.place(position, solution.get(position));
            }
        }
    }
}
