package com.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A cell coordinate on the Sudoku grid. Rows are lettered A-I and columns are numbered 1-9 when shown to the user;
 * internally both are zero-based indexes.
 */
public record Position(int row, int col) {

    public static final int SIZE = 9;
    public static final int BOX_SIZE = 3;

    private static final List<Position> ALL = createAll();

    public Position {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Position out of range: row=" + row + ", col=" + col);
        }
    }

    /**
     * Parses a label such as {@code B3} (case-insensitive) into a position.
     *
     * @throws IllegalArgumentException if the text is not a row letter A-I followed by a column digit 1-9
     */
    public static Position parse(String text) {
        if (text == null || text.length() != 2) {
            throw invalidLabel(text);
        }
        int row = Character.toUpperCase(text.charAt(0)) - 'A';
        int col = text.charAt(1) - '1';
        try {
            return new Position(row, col);
        } catch (IllegalArgumentException e) {
            throw invalidLabel(text);
        }
    }

    /** All 81 positions in reading order (left to right, top to bottom). */
    public static List<Position> all() {
        return ALL;
    }

    /** Zero-based index (0-8) of the 3x3 subgrid containing this position. */
    public int box() {
        return (row / BOX_SIZE) * BOX_SIZE + col / BOX_SIZE;
    }

    /** User-facing label, e.g. {@code B3}. */
    public String label() {
        return "" + (char) ('A' + row) + (col + 1);
    }

    private static List<Position> createAll() {
        List<Position> positions = new ArrayList<>();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                positions.add(new Position(r, c));
            }
        }
        return Collections.unmodifiableList(positions);
    }

    private static IllegalArgumentException invalidLabel(String text) {
        return new IllegalArgumentException("'" + text + "' is not a valid cell. Use a row A-I followed by a column 1-9, e.g. B3.");
    }
}
