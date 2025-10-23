package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnlinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class UnlinkCommandParserTest {

    private final UnlinkCommandParser parser = new UnlinkCommandParser();

    @Test
    public void parse_validArgs_returnsUnlinkCommand() {
        String userInput = " " + PREFIX_STUDENT + "1 " + PREFIX_PARENT + "2";
        UnlinkCommand expectedCommand = new UnlinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingParentPrefix_throwsParseException() {
        String userInput = " " + PREFIX_STUDENT + "1";
        assertParseFailure(parser, userInput, "Missing parent index. Example: unlink student/1 parent/2");
    }

    @Test
    public void parse_missingStudentPrefix_throwsParseException() {
        String userInput = " " + PREFIX_PARENT + "2";
        assertParseFailure(parser, userInput, "Missing student index. Example: unlink student/1 parent/2");
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = " " + PREFIX_STUDENT + "a " + PREFIX_PARENT + "2";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    public void equals() {
        UnlinkCommandParser firstParser = new UnlinkCommandParser();
        UnlinkCommandParser secondParser = new UnlinkCommandParser();

        // same object -> returns true
        assertEquals(firstParser, firstParser);

        // different object but same type -> returns false (parser has no equality override)
        assertThrows(AssertionError.class, () -> assertEquals(firstParser, secondParser));
    }
}
