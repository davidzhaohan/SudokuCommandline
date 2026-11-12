package com.sudoku.ui;

import com.sudoku.model.Grid;
import com.sudoku.model.Position;

/** Formats a {@link Grid} as text, with column numbers across the top and row letters down the side. */
public final class GridRenderer {

    private static final String EMPTY_CELL = "_";

    public String render(Grid grid) {
        StringBuilder out = new StringBuilder(header()).append(System.lineSeparator());
        for (int row = 0; row < Position.SIZE; row++) {
            out.append(renderRow(grid, row)).append(System.lineSeparator());
        }
        return out.toString();
    }

    private String header() {
        StringBuilder header = new StringBuilder("   ");
        for (int col = 1; col <= Position.SIZE; col++) {
            header.append(' ').append(col);
        }
        return header.toString();
    }

    private String renderRow(Grid grid, int row) {
        StringBuilder line = new StringBuilder("  ").append((char) ('A' + row));
        for (int col = 0; col < Position.SIZE; col++) {
            line.append(' ').append(renderCell(grid.get(new Position(row, col))));
        }
        return line.toString();
    }

    private String renderCell(int value) {
        return value == Grid.EMPTY ? EMPTY_CELL : String.valueOf(value);
    }
}
