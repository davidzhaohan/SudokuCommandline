package com.sudoku.model;

/** Supplies puzzles to play. Abstracted so the game can be driven by generated or fixed puzzles. */
public interface PuzzleSource {

    Puzzle next();
}
