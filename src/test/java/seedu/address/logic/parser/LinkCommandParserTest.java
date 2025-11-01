package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LinkCommand;

public class LinkCommandParserTest {

    private final LinkCommandParser parser = new LinkCommandParser();

    @Test
    public void parse_validArgs_returnsLinkCommand() throws Exception {
        // Input: student/1 parent/2
        String userInput = " student/1 parent/2";

        LinkCommand expectedCommand = new LinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingParentPrefix_throwsParseException() {
        // Missing parent prefix
        String userInput = " student/1";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_missingStudentPrefix_throwsParseException() {
        // Missing student prefix
        String userInput = " parent/2";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        // Invalid index (non-numeric)
        String userInput = " student/one parent/2";
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, LinkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_swappedPrefixes_stillParsesCorrectly() throws Exception {
        // Parent first, student second — order should not matter
        String userInput = " parent/2 student/1";
        LinkCommand expectedCommand = new LinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_extraWhitespace_success() throws Exception {
        String userInput = "   student/1   parent/2  ";
        LinkCommand expectedCommand = new LinkCommand(Index.fromOneBased(1), Index.fromOneBased(2));
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
