package com.sudoku.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** A row, column or subgrid: nine cells that must each contain a different number. */
public record Unit(UnitType type, int index, List<Position> positions) {

    private static final List<Unit> ALL = createAll();

    /** All 27 units: 9 rows, 9 columns and 9 subgrids. */
    public static List<Unit> all() {
        return ALL;
    }

    private static List<Unit> createAll() {
        List<Unit> units = new ArrayList<>();
        for (UnitType type : UnitType.values()) {
            for (int index = 0; index < Position.SIZE; index++) {
                units.add(new Unit(type, index, positionsOf(type, index)));
            }
        }
        return Collections.unmodifiableList(units);
    }

    private static List<Position> positionsOf(UnitType type, int index) {
        return Position.all().stream()
                .filter(position -> indexIn(type, position) == index)
                .toList();
    }

    private static int indexIn(UnitType type, Position position) {
        return switch (type) {
            case ROW -> position.row();
            case COLUMN -> position.col();
            case SUBGRID -> position.box();
        };
    }
}
