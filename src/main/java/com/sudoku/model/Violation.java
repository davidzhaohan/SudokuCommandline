package com.sudoku.model;

/** A number that appears more than once within a single {@link Unit}. */
public record Violation(int number, UnitType unitType, int unitIndex) {

    /** Human readable description, e.g. {@code Number 3 already exists in Row A.} */
    public String describe() {
        return "Number " + number + " already exists in " + location() + ".";
    }

    private String location() {
        return switch (unitType) {
            case ROW -> "Row " + (char) ('A' + unitIndex);
            case COLUMN -> "Column " + (unitIndex + 1);
            case SUBGRID -> "the same 3×3 subgrid";
        };
    }
}
