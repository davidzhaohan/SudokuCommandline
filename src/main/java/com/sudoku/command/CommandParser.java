package com.sudoku.command;

import com.sudoku.model.Position;

/**
 * Turns a line typed by the player into a {@link Command}. Input is case-insensitive and extra whitespace is
 * ignored.
 *
 * <pre>
 *   B3 7     place 7 in row B, column 3
 *   C5 clear empty C5
 *   hint     reveal one correct number
 *   check    look for duplicates
 *   quit     leave the game
 * </pre>
 */
public final class CommandParser {

    public Command parse(String input) throws CommandParseException {
        String[] words = input == null ? new String[0] : input.trim().toLowerCase().split("\\s+");
        if (words.length == 1) {
            return parseKeyword(words[0]);
        }
        if (words.length == 2) {
            return parseCellCommand(words[0], words[1]);
        }
        throw unrecognised(input);
    }

    private Command parseKeyword(String word) throws CommandParseException {
        return switch (word) {
            case "hint" -> new HintCommand();
            case "check" -> new CheckCommand();
            case "quit" -> new QuitCommand();
            default -> throw unrecognised(word);
        };
    }

    private Command parseCellCommand(String cell, String argument) throws CommandParseException {
        Position position = parsePosition(cell);
        if (argument.equals("clear")) {
            return new ClearCommand(position);
        }
        return new PlaceCommand(position, parseNumber(argument));
    }

    private Position parsePosition(String cell) throws CommandParseException {
        try {
            return Position.parse(cell);
        } catch (IllegalArgumentException e) {
            throw new CommandParseException(e.getMessage());
        }
    }

    private int parseNumber(String text) throws CommandParseException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new CommandParseException("'" + text + "' is not a number. Enter a number between 1 and 9, or 'clear'.");
        }
    }

    private CommandParseException unrecognised(String input) {
        return new CommandParseException("Unrecognised command '" + (input == null ? "" : input.trim())
                + "'. Try A3 4, C5 clear, hint, check or quit.");
    }
}
