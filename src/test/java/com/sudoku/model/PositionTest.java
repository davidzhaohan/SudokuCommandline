package com.sudoku.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void parsesRowLetterAndColumnNumber() {
        assertEquals(new Position(1, 2), Position.parse("B3"));
    }

    @Test
    void parsingIsCaseInsensitive() {
        assertEquals(new Position(8, 8), Position.parse("i9"));
    }

    @Test
    void rejectsInvalidLabels() {
        for (String text : new String[] {null, "", "A", "A0", "A10", "J1", "11", "AA", "A1 "}) {
            assertThrows(IllegalArgumentException.class, () -> Position.parse(text), "input: " + text);
        }
    }

    @Test
    void labelRoundTrips() {
        for (Position position : Position.all()) {
            assertEquals(position, Position.parse(position.label()));
        }
    }

    @Test
    void allReturns81DistinctPositions() {
        assertEquals(81, Position.all().stream().distinct().count());
    }

    @Test
    void boxIndexesCoverNineSubgrids() {
        assertEquals(0, new Position(0, 0).box());
        assertEquals(4, new Position(4, 4).box());
        assertEquals(5, new Position(3, 8).box());
        assertEquals(8, new Position(8, 8).box());
    }

    @Test
    void constructorRejectsOutOfRangeValues() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, 9));
    }
}
