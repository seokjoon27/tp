package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.PaidCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

public class PaidCommandParserTest {

    private final PaidCommandParser parser = new PaidCommandParser();

    @Test
    public void parse_validName_returnsPaidCommand() throws Exception {
        Name name = new Name("Alice Pauline");
        PaidCommand command = parser.parse(" " + PREFIX_NAME + name.fullName);
        assertEquals(new PaidCommand(name), command);
    }

    @Test
    public void parse_validIndex_returnsPaidCommand() throws Exception {
        PaidCommand command = parser.parse(" 1");
        assertEquals(new PaidCommand(Index.fromOneBased(1)), command);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () -> parser.parse("0"));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, PaidCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("   "));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, PaidCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("1 n/Alice"));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, PaidCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, expectedMessage, () -> parser.parse("Alice Pauline"));
    }

    @Test
    public void parse_missingName_throwsParseException() {
        assertThrows(
                ParseException.class,
                seedu.address.model.person.Name.MESSAGE_CONSTRAINTS, (
                ) -> parser.parse(" " + PREFIX_NAME));
    }
}
