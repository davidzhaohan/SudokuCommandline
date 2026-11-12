package com.sudoku.command;

import com.sudoku.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    private final CommandParser parser = new CommandParser();

    @Test
    void parsesPlaceCommand() throws CommandParseException {
        assertEquals(new PlaceCommand(Position.parse("B3"), 7), parser.parse("B3 7"));
    }

    @Test
    void parsesClearCommand() throws CommandParseException {
        assertEquals(new ClearCommand(Position.parse("C5")), parser.parse("C5 clear"));
    }

    @Test
    void parsesKeywords() throws CommandParseException {
        assertInstanceOf(HintCommand.class, parser.parse("hint"));
        assertInstanceOf(CheckCommand.class, parser.parse("check"));
        assertInstanceOf(QuitCommand.class, parser.parse("quit"));
    }

    @Test
    void isCaseAndWhitespaceInsensitive() throws CommandParseException {
        assertEquals(new PlaceCommand(Position.parse("B3"), 7), parser.parse("  b3    7  "));
        assertEquals(new ClearCommand(Position.parse("C5")), parser.parse("c5 CLEAR"));
        assertInstanceOf(HintCommand.class, parser.parse("HINT"));
    }

    @Test
    void outOfRangeNumberIsParsedSoGameCanReject() throws CommandParseException {
        assertEquals(new PlaceCommand(Position.parse("A3"), 10), parser.parse("A3 10"));
        assertEquals(new PlaceCommand(Position.parse("A3"), 0), parser.parse("A3 0"));
    }

    @Test
    void rejectsGarbage() {
        for (String input : new String[] {null, "", "   ", "foo", "A3", "A3 x", "Z9 1", "A3 4 5", "hint now"}) {
            assertThrows(CommandParseException.class, () -> parser.parse(input), "input: " + input);
        }
    }
}
