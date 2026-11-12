package com.sudoku.model;

import com.sudoku.TestPuzzles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    private static final Position A1 = new Position(0, 0);

    @Test
    void newGridIsEmpty() {
        Grid grid = new Grid();
        assertEquals(0, grid.countFilled());
        assertTrue(grid.isEmpty(A1));
        assertFalse(grid.isFull());
    }

    @Test
    void setAndGetValue() {
        Grid grid = new Grid();
        grid.set(A1, 5);
        assertEquals(5, grid.get(A1));
        assertEquals(1, grid.countFilled());
    }

    @Test
    void clearEmptiesCell() {
        Grid grid = new Grid();
        grid.set(A1, 5);
        grid.clear(A1);
        assertTrue(grid.isEmpty(A1));
    }

    @Test
    void setRejectsValuesOutsideRange() {
        Grid grid = new Grid();
        assertThrows(IllegalArgumentException.class, () -> grid.set(A1, 10));
        assertThrows(IllegalArgumentException.class, () -> grid.set(A1, -1));
    }

    @Test
    void parseReadsEightyOneCells() {
        Grid grid = TestPuzzles.classic().initial();
        assertEquals(30, grid.countFilled());
        assertEquals(5, grid.get(Position.parse("A1")));
        assertEquals(9, grid.get(Position.parse("I9")));
        assertTrue(grid.isEmpty(Position.parse("A3")));
    }

    @Test
    void parseRejectsWrongLengthAndBadCharacters() {
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("123"));
        assertThrows(IllegalArgumentException.class, () -> Grid.parse("x".repeat(81)));
    }

    @Test
    void canPlaceDetectsRowColumnAndSubgridClashes() {
        Grid grid = TestPuzzles.classic().initial();
        assertFalse(grid.canPlace(Position.parse("A3"), 3), "row A has a 3");
        assertFalse(grid.canPlace(Position.parse("A3"), 6), "column 3 has no 6 but subgrid does (B1)");
        assertFalse(grid.canPlace(Position.parse("B3"), 8), "column 3 has an 8 (C3)");
        assertTrue(grid.canPlace(Position.parse("A3"), 4));
    }

    @Test
    void canPlaceIgnoresCellsOwnValue() {
        Grid grid = new Grid();
        grid.set(A1, 5);
        assertTrue(grid.canPlace(A1, 5));
    }

    @Test
    void copyIsIndependent() {
        Grid original = new Grid();
        Grid copy = original.copy();
        copy.set(A1, 3);
        assertTrue(original.isEmpty(A1));
        assertNotEquals(original, copy);
    }

    @Test
    void solutionIsFull() {
        assertTrue(TestPuzzles.classic().solution().isFull());
    }
}
