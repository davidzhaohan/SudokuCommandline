package com.sudoku.model;

/**
 * A puzzle together with its solution.
 *
 * @param initial  the grid as presented to the player (pre-filled cells only)
 * @param solution the completely filled grid that solves {@code initial}
 */
public record Puzzle(Grid initial, Grid solution) {
}
