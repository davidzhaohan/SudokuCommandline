# Sudoku Command Line

A command-line Sudoku game written in Java. It generates a fresh puzzle with 30 pre-filled numbers and a unique
solution, then lets you fill in cells, clear them, ask for hints and check your grid for rule violations until the
puzzle is solved.

## Environment

- Any OS with a JDK 17 or newer (developed and tested on macOS with Temurin 21). Windows, Linux and macOS all work.
- No other installation is needed: the bundled Gradle wrapper (`gradlew` / `gradlew.bat`) downloads Gradle on first
  use, so internet access is required the first time you build.
- The wrapper contains `gradle/wrapper/gradle-wrapper.jar`. If your archive or mail filter strips `.jar` files,
  install Gradle 8+ and use `gradle test` / `gradle run` in place of `./gradlew` (or run `gradle wrapper` once to
  regenerate the wrapper).

## Build, test and run

```bash
./gradlew test                 # run the unit and end-to-end tests   (Windows: gradlew.bat test)
./gradlew run --console=plain  # play the game                       (Windows: gradlew.bat run --console=plain)
```

Alternatively, build a runnable distribution and start it directly:

```bash
./gradlew installDist
build/install/sudoku/bin/sudoku
```

## How to play

| Command    | Meaning                                                        |
| ---------- | -------------------------------------------------------------- |
| `B3 7`     | Put 7 in row **B**, column **3**                               |
| `C5 clear` | Empty cell C5                                                  |
| `hint`     | Reveal the correct number for one cell that is not yet correct |
| `check`    | Report duplicates in rows, columns and 3×3 subgrids            |
| `quit`     | Leave the game                                                 |

Commands are case-insensitive. Pre-filled cells cannot be changed, and numbers must be 1-9. When the grid is full and
has no violations the game announces success and offers a new puzzle.

## Design

The code is split into four packages with a one-way dependency flow
(`ui → command → model ← generator`):

| Package     | Responsibility                                                                                           |
| ----------- | -------------------------------------------------------------------------------------------------------- |
| `model`     | Pure game domain: `Grid`, `Position`, `Unit` (row/column/subgrid), `GridValidator`, `Violation`, `Game`. |
| `generator` | `Solver` (backtracking) and `PuzzleGenerator`, which implements the `PuzzleSource` interface.            |
| `command`   | Command pattern: `CommandParser` turns text into `Command` objects that act on a `Game`.                 |
| `ui`        | `ConsoleApp` (I/O loop) and `GridRenderer` (formatting).                                                 |

Notes on the main decisions:

- **Single responsibility.** `Game` owns rules and state, `GridValidator` finds duplicates, `GridRenderer` formats,
  `CommandParser` parses and `ConsoleApp` only does I/O. Each class is small.
- **Open/closed.** A new command is a new `Command` implementation plus one line in `CommandParser`; nothing else
  changes. `GridValidator` iterates over `Unit.all()`, so it does not special-case rows, columns or subgrids.
- **Dependency inversion.** `ConsoleApp` depends on the `PuzzleSource` interface and on an injected `Random`,
  `BufferedReader` and `PrintStream`. Tests therefore play complete games with a fixed puzzle and scripted input.
- **Puzzle generation.** `Solver` fills an empty grid in random order to produce a full solution. `PuzzleGenerator`
  then removes numbers in random order, keeping a removal only if `Solver.countSolutions` still finds exactly one
  solution, until 30 clues remain. The solution is stored with the puzzle, which is what makes hints cheap and correct.
- **Hints** pick a random cell whose current content differs from the solution (empty _or_ wrong) and reveal the
  correct number. They never change the grid.
- **Check** reports every duplicate found, one line each, using the message formats from the problem statement.

## Assumptions

- A move that clashes with other numbers is **accepted** (as in the problem statement's "violation" examples); the
  clash is only reported by `check`. Only pre-filled cells and numbers outside 1-9 are rejected.
- The game is complete when the grid is full and has no duplicates. Because puzzles have a unique solution, that is
  the same as matching the stored solution.
- The statement says "Press any key to play again". Java console input is line-based, so the prompt asks for
  **Enter**; end-of-input (Ctrl+D / Ctrl+Z) exits.
- Unknown or malformed input prints a helpful message and the game continues; it never crashes the program.
- The 30-clue count is the `PuzzleGenerator.DEFAULT_CLUES` constant.

## Tests

`./gradlew test` runs 80+ JUnit 5 tests: unit tests for every class, plus `ConsoleAppTest`, which drives whole
games (including solving a puzzle and replaying) through scripted input. The only test dependency is JUnit.
