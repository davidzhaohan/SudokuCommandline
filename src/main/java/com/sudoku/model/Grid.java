package com.sudoku.model;

import java.util.Arrays;

/** A mutable 9x9 Sudoku grid. Empty cells hold {@link #EMPTY}; filled cells hold 1-9. */
public final class Grid {

    public static final int EMPTY = 0;
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 9;

    private final int[][] cells = new int[Position.SIZE][Position.SIZE];

    /**
     * Builds a grid from 81 characters in reading order. Digits 1-9 are values; {@code 0}, {@code _} and {@code .}
     * are empty cells. Whitespace is ignored.
     */
    public static Grid parse(String text) {
        String compact = text.replaceAll("\\s", "");
        if (compact.length() != Position.SIZE * Position.SIZE) {
            throw new IllegalArgumentException("Expected 81 cells but found " + compact.length());
        }
        Grid grid = new Grid();
        for (Position position : Position.all()) {
            char c = compact.charAt(position.row() * Position.SIZE + position.col());
            grid.set(position, parseCell(c));
        }
        return grid;
    }

    public int get(Position position) {
        return cells[position.row()][position.col()];
    }

    public void set(Position position, int value) {
        if (value < EMPTY || value > MAX_VALUE) {
            throw new IllegalArgumentException("Value must be between 0 and 9 but was " + value);
        }
        cells[position.row()][position.col()] = value;
    }

    public void clear(Position position) {
        set(position, EMPTY);
    }

    public boolean isEmpty(Position position) {
        return get(position) == EMPTY;
    }

    public boolean isFull() {
        return countFilled() == Position.SIZE * Position.SIZE;
    }

    public int countFilled() {
        int filled = 0;
        for (Position position : Position.all()) {
            if (!isEmpty(position)) {
                filled++;
            }
        }
        return filled;
    }

    /**
     * Whether {@code value} could be put at {@code position} without clashing with any other cell in the same row,
     * column or subgrid. The cell's current content is ignored.
     */
    public boolean canPlace(Position position, int value) {
        for (int i = 0; i < Position.SIZE; i++) {
            if (i != position.col() && cells[position.row()][i] == value) {
                return false;
            }
            if (i != position.row() && cells[i][position.col()] == value) {
                return false;
            }
        }
        return !boxContainsElsewhere(position, value);
    }

    public Grid copy() {
        Grid copy = new Grid();
        for (int r = 0; r < Position.SIZE; r++) {
            copy.cells[r] = Arrays.copyOf(cells[r], Position.SIZE);
        }
        return copy;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Grid grid && Arrays.deepEquals(cells, grid.cells);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(cells);
    }

    private boolean boxContainsElsewhere(Position position, int value) {
        int boxRow = position.row() / Position.BOX_SIZE * Position.BOX_SIZE;
        int boxCol = position.col() / Position.BOX_SIZE * Position.BOX_SIZE;
        for (int r = boxRow; r < boxRow + Position.BOX_SIZE; r++) {
            for (int c = boxCol; c < boxCol + Position.BOX_SIZE; c++) {
                boolean isSelf = r == position.row() && c == position.col();
                if (!isSelf && cells[r][c] == value) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int parseCell(char c) {
        if (c == '0' || c == '_' || c == '.') {
            return EMPTY;
        }
        if (c >= '1' && c <= '9') {
            return c - '0';
        }
        throw new IllegalArgumentException("Unexpected character in grid: '" + c + "'");
    }
}
