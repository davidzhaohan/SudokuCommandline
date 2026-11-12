package com.sudoku.model;

/** The correct number for one cell. */
public record Hint(Position position, int value) {
}
