package com.sudoku.generator;

import com.sudoku.model.GridValidator;
import com.sudoku.model.Position;
import com.sudoku.model.Puzzle;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleGeneratorTest {

    @Test
    void generatesPuzzleWithRequestedNumberOfClues() {
        Puzzle puzzle = new PuzzleGenerator(30, new Random(7)).next();
        assertEquals(30, puzzle.initial().countFilled());
    }

    @Test
    void solutionIsCompleteAndValid() {
        Puzzle puzzle = new PuzzleGenerator(30, new Random(7)).next();
        assertTrue(puzzle.solution().isFull());
        assertTrue(new GridValidator().isValid(puzzle.solution()));
    }

    @Test
    void cluesMatchTheSolution() {
        Puzzle puzzle = new PuzzleGenerator(30, new Random(11)).next();
        for (Position position : Position.all()) {
            if (!puzzle.initial().isEmpty(position)) {
                assertEquals(puzzle.solution().get(position), puzzle.initial().get(position));
            }
        }
    }

    @Test
    void puzzleHasExactlyOneSolution() {
        Puzzle puzzle = new PuzzleGenerator(30, new Random(3)).next();
        Solver solver = new Solver(new Random(1));
        assertEquals(1, solver.countSolutions(puzzle.initial(), 2));
    }

    @Test
    void sameSeedGivesSamePuzzle() {
        Puzzle first = new PuzzleGenerator(30, new Random(99)).next();
        Puzzle second = new PuzzleGenerator(30, new Random(99)).next();
        assertEquals(first, second);
    }

    @Test
    void successiveCallsGiveDifferentPuzzles() {
        PuzzleGenerator generator = new PuzzleGenerator(30, new Random(5));
        assertNotEquals(generator.next(), generator.next());
    }

    @Test
    void rejectsImpossibleClueCounts() {
        assertThrows(IllegalArgumentException.class, () -> new PuzzleGenerator(16, new Random()));
        assertThrows(IllegalArgumentException.class, () -> new PuzzleGenerator(82, new Random()));
    }
}
