package com.sudoku.generator;

import com.sudoku.model.Grid;
import com.sudoku.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/** Backtracking solver that always branches on the most constrained empty cell. */
public final class Solver {

    private final Random random;

    /** @param random decides the order in which candidates are tried, which makes {@link #solve} random */
    public Solver(Random random) {
        this.random = random;
    }

    /** Completes the grid, trying candidates in random order. Returns empty if the grid has no solution. */
    public Optional<Grid> solve(Grid grid) {
        Grid work = grid.copy();
        return search(work, null) ? Optional.of(work) : Optional.empty();
    }

    /** Counts solutions, stopping as soon as {@code limit} have been found. */
    public int countSolutions(Grid grid, int limit) {
        int[] counter = {0};
        search(grid.copy(), () -> ++counter[0] >= limit);
        return counter[0];
    }

    /**
     * @param onSolution called for every solution found; return true to stop searching. A null callback means
     *                   "stop at the first solution", leaving the solution in {@code grid}.
     */
    private boolean search(Grid grid, SolutionListener onSolution) {
        Position next = mostConstrainedEmptyCell(grid);
        if (next == null) {
            return onSolution == null || onSolution.found();
        }
        for (int candidate : shuffledCandidates(grid, next)) {
            grid.set(next, candidate);
            if (search(grid, onSolution)) {
                return true;
            }
        }
        grid.clear(next);
        return false;
    }

    private Position mostConstrainedEmptyCell(Grid grid) {
        Position best = null;
        int bestCount = Integer.MAX_VALUE;
        for (Position position : Position.all()) {
            if (!grid.isEmpty(position)) {
                continue;
            }
            int count = candidates(grid, position).size();
            if (count < bestCount) {
                best = position;
                bestCount = count;
            }
        }
        return best;
    }

    private List<Integer> shuffledCandidates(Grid grid, Position position) {
        List<Integer> candidates = candidates(grid, position);
        Collections.shuffle(candidates, random);
        return candidates;
    }

    private List<Integer> candidates(Grid grid, Position position) {
        List<Integer> candidates = new ArrayList<>();
        for (int value = Grid.MIN_VALUE; value <= Grid.MAX_VALUE; value++) {
            if (grid.canPlace(position, value)) {
                candidates.add(value);
            }
        }
        return candidates;
    }

    @FunctionalInterface
    private interface SolutionListener {
        boolean found();
    }
}
