package com.sudoku.generator;

import com.sudoku.TestPuzzles;
import com.sudoku.model.Grid;
import com.sudoku.model.GridValidator;
import com.sudoku.model.Position;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    private final Solver solver = new Solver(new Random(42));

    @Test
    void solvesClassicPuzzle() {
        Grid solved = solver.solve(TestPuzzles.classic().initial()).orElseThrow();
        assertEquals(TestPuzzles.classic().solution(), solved);
    }

    @Test
    void solveDoesNotModifyInput() {
        Grid puzzle = TestPuzzles.classic().initial();
        solver.solve(puzzle);
        assertEquals(30, puzzle.countFilled());
    }

    @Test
    void generatesValidCompleteGridFromEmpty() {
        Grid solved = solver.solve(new Grid()).orElseThrow();
        assertTrue(solved.isFull());
        assertTrue(new GridValidator().isValid(solved));
    }

    @Test
    void returnsEmptyForUnsolvableGrid() {
        Grid grid = new Grid();
        // Row A has 1-8 so A9 must be 9, but column 9 already holds a 9.
        for (int col = 0; col < 8; col++) {
            grid.set(new Position(0, col), col + 1);
        }
        grid.set(Position.parse("B9"), 9);
        assertTrue(solver.solve(grid).isEmpty());
    }

    @Test
    void countsOneSolutionForUniquePuzzle() {
        assertEquals(1, solver.countSolutions(TestPuzzles.classic().initial(), 2));
    }

    @Test
    void countStopsAtLimit() {
        assertEquals(2, solver.countSolutions(new Grid(), 2));
    }

    @Test
    void countsZeroForUnsolvableGrid() {
        Grid grid = new Grid();
        for (int col = 0; col < 8; col++) {
            grid.set(new Position(0, col), col + 1);
        }
        grid.set(Position.parse("B9"), 9);
        assertEquals(0, solver.countSolutions(grid, 2));
    }
}
