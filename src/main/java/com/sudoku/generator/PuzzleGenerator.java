package com.sudoku.generator;

import com.sudoku.model.Grid;
import com.sudoku.model.Position;
import com.sudoku.model.Puzzle;
import com.sudoku.model.PuzzleSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates puzzles with exactly one solution: builds a random complete grid, then removes numbers one at a time
 * (in random order), keeping each removal only if the puzzle stays uniquely solvable, until the requested number of
 * clues remains.
 */
public final class PuzzleGenerator implements PuzzleSource {

    public static final int DEFAULT_CLUES = 30;

    private final int clues;
    private final Random random;
    private final Solver solver;

    public PuzzleGenerator(int clues, Random random) {
        if (clues < 17 || clues > Position.SIZE * Position.SIZE) {
            throw new IllegalArgumentException("A unique puzzle needs between 17 and 81 clues but got " + clues);
        }
        this.clues = clues;
        this.random = random;
        this.solver = new Solver(random);
    }

    @Override
    public Puzzle next() {
        while (true) {
            Grid solution = solver.solve(new Grid()).orElseThrow();
            Grid puzzle = removeNumbers(solution);
            if (puzzle.countFilled() == clues) {
                return new Puzzle(puzzle, solution);
            }
        }
    }

    private Grid removeNumbers(Grid solution) {
        Grid puzzle = solution.copy();
        for (Position position : shuffledPositions()) {
            if (puzzle.countFilled() == clues) {
                break;
            }
            tryRemove(puzzle, position);
        }
        return puzzle;
    }

    private void tryRemove(Grid puzzle, Position position) {
        int original = puzzle.get(position);
        puzzle.clear(position);
        if (solver.countSolutions(puzzle, 2) != 1) {
            puzzle.set(position, original);
        }
    }

    private List<Position> shuffledPositions() {
        List<Position> positions = new ArrayList<>(Position.all());
        Collections.shuffle(positions, random);
        return positions;
    }
}
