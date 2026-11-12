package com.sudoku.model;

import com.sudoku.TestPuzzles;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridValidatorTest {

    private final GridValidator validator = new GridValidator();

    @Test
    void puzzleAndSolutionAreValid() {
        assertTrue(validator.isValid(TestPuzzles.classic().initial()));
        assertTrue(validator.isValid(TestPuzzles.classic().solution()));
    }

    @Test
    void emptyGridIsValid() {
        assertTrue(validator.findViolations(new Grid()).isEmpty());
    }

    @Test
    void detectsDuplicateInRow() {
        Grid grid = TestPuzzles.classic().initial();
        grid.set(Position.parse("A3"), 3);

        List<Violation> violations = validator.findViolations(grid);

        assertTrue(violations.contains(new Violation(3, UnitType.ROW, 0)));
        assertEquals("Number 3 already exists in Row A.", new Violation(3, UnitType.ROW, 0).describe());
    }

    @Test
    void detectsDuplicateInColumn() {
        Grid grid = TestPuzzles.classic().initial();
        grid.set(Position.parse("C1"), 5);

        assertTrue(validator.findViolations(grid).contains(new Violation(5, UnitType.COLUMN, 0)));
        assertEquals("Number 5 already exists in Column 1.", new Violation(5, UnitType.COLUMN, 0).describe());
    }

    @Test
    void detectsDuplicateInSubgrid() {
        Grid grid = TestPuzzles.classic().initial();
        grid.set(Position.parse("B3"), 8);

        assertTrue(validator.findViolations(grid).contains(new Violation(8, UnitType.SUBGRID, 0)));
        assertEquals("Number 8 already exists in the same 3×3 subgrid.",
                new Violation(8, UnitType.SUBGRID, 0).describe());
    }

    @Test
    void reportsEachViolationOnce() {
        Grid grid = new Grid();
        grid.set(Position.parse("A1"), 1);
        grid.set(Position.parse("A2"), 1);
        grid.set(Position.parse("A3"), 1);

        long rowViolations = validator.findViolations(grid).stream()
                .filter(v -> v.unitType() == UnitType.ROW)
                .count();
        assertEquals(1, rowViolations);
    }
}
