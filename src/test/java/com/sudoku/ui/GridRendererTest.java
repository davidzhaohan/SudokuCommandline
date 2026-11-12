package com.sudoku.ui;

import com.sudoku.model.Grid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GridRendererTest {

    @Test
    void rendersGridLikeTheProblemStatement() {
        Grid grid = Grid.parse("""
                53_ _7_ ___
                6__ 195 ___
                _98 ___ _6_
                8__ _6_ __3
                4__ 8_3 __1
                7__ _2_ __6
                _6_ ___ 28_
                ___ 419 __5
                ___ _8_ _79
                """);

        String expected = String.join(System.lineSeparator(),
                "    1 2 3 4 5 6 7 8 9",
                "  A 5 3 _ _ 7 _ _ _ _",
                "  B 6 _ _ 1 9 5 _ _ _",
                "  C _ 9 8 _ _ _ _ 6 _",
                "  D 8 _ _ _ 6 _ _ _ 3",
                "  E 4 _ _ 8 _ 3 _ _ 1",
                "  F 7 _ _ _ 2 _ _ _ 6",
                "  G _ 6 _ _ _ _ 2 8 _",
                "  H _ _ _ 4 1 9 _ _ 5",
                "  I _ _ _ _ 8 _ _ 7 9",
                "");

        assertEquals(expected, new GridRenderer().render(grid));
    }
}
