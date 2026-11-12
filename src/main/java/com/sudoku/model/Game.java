package com.sudoku.model;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/** State and rules of a single Sudoku game: the player's moves, hints and validity checks. */
public final class Game {

    private final Grid current;
    private final Grid solution;
    private final Grid prefilled;
    private final GridValidator validator = new GridValidator();
    private final Random random;

    public Game(Puzzle puzzle, Random random) {
        this.current = puzzle.initial().copy();
        this.prefilled = puzzle.initial().copy();
        this.solution = puzzle.solution().copy();
        this.random = random;
    }

    /** A snapshot of the player's current grid. */
    public Grid grid() {
        return current.copy();
    }

    public boolean isPrefilled(Position position) {
        return !prefilled.isEmpty(position);
    }

    /**
     * Places a number in a cell. The number is accepted even if it clashes with other cells; use {@link #check()}
     * to find rule violations.
     *
     * @throws InvalidMoveException if the cell is pre-filled or the number is outside 1-9
     */
    public void place(Position position, int value) throws InvalidMoveException {
        requireEditable(position);
        if (value < Grid.MIN_VALUE || value > Grid.MAX_VALUE) {
            throw new InvalidMoveException("Number must be between " + Grid.MIN_VALUE + " and " + Grid.MAX_VALUE + ".");
        }
        current.set(position, value);
    }

    /**
     * Empties a cell.
     *
     * @throws InvalidMoveException if the cell is pre-filled
     */
    public void clear(Position position) throws InvalidMoveException {
        requireEditable(position);
        current.clear(position);
    }

    /** Picks a random cell that does not yet hold its correct number and reveals that number. */
    public Optional<Hint> hint() {
        List<Position> unsolved = Position.all().stream()
                .filter(position -> current.get(position) != solution.get(position))
                .toList();
        if (unsolved.isEmpty()) {
            return Optional.empty();
        }
        Position chosen = unsolved.get(random.nextInt(unsolved.size()));
        return Optional.of(new Hint(chosen, solution.get(chosen)));
    }

    public List<Violation> check() {
        return validator.findViolations(current);
    }

    /** The game is complete when every cell is filled and no rule is violated. */
    public boolean isComplete() {
        return current.isFull() && validator.isValid(current);
    }

    private void requireEditable(Position position) throws InvalidMoveException {
        if (isPrefilled(position)) {
            throw new InvalidMoveException(position.label() + " is pre-filled.");
        }
    }
}
