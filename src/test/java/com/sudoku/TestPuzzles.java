package com.sudoku;

import com.sudoku.model.Grid;
import com.sudoku.model.Puzzle;
import com.sudoku.model.PuzzleSource;

/** The well-known puzzle used in the problem statement, for deterministic tests. */
public final class TestPuzzles {

    public static final String PUZZLE = """
            53_ _7_ ___
            6__ 195 ___
            _98 ___ _6_
            8__ _6_ __3
            4__ 8_3 __1
            7__ _2_ __6
            _6_ ___ 28_
            ___ 419 __5
            ___ _8_ _79
            """;

    public static final String SOLUTION = """
            534 678 912
            672 195 348
            198 342 567
            859 761 423
            426 853 791
            713 924 856
            961 537 284
            287 419 635
            345 286 179
            """;

    private TestPuzzles() {
    }

    public static Puzzle classic() {
        return new Puzzle(Grid.parse(PUZZLE), Grid.parse(SOLUTION));
    }

    public static PuzzleSource source() {
        return TestPuzzles::classic;
    }
}
