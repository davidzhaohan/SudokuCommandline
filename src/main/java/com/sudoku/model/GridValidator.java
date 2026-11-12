package com.sudoku.model;

import java.util.ArrayList;
import java.util.List;

/** Finds duplicate numbers in the rows, columns and subgrids of a {@link Grid}. Empty cells are ignored. */
public final class GridValidator {

    public List<Violation> findViolations(Grid grid) {
        List<Violation> violations = new ArrayList<>();
        for (Unit unit : Unit.all()) {
            violations.addAll(findViolations(grid, unit));
        }
        return violations;
    }

    public boolean isValid(Grid grid) {
        return findViolations(grid).isEmpty();
    }

    private List<Violation> findViolations(Grid grid, Unit unit) {
        int[] occurrences = new int[Grid.MAX_VALUE + 1];
        for (Position position : unit.positions()) {
            occurrences[grid.get(position)]++;
        }
        List<Violation> violations = new ArrayList<>();
        for (int number = Grid.MIN_VALUE; number <= Grid.MAX_VALUE; number++) {
            if (occurrences[number] > 1) {
                violations.add(new Violation(number, unit.type(), unit.index()));
            }
        }
        return violations;
    }
}
