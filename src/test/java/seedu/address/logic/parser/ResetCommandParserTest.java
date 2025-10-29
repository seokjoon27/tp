package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ResetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ResetCommandParserTest {

    private final ResetCommandParser parser = new ResetCommandParser();

    @Test
    public void parse_emptyArgs_failure() {
        // empty or spaces only are invalid
        assertParseFailure(parser, "", String.format(
                seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT, ResetCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "   ", String.format(
                seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT, ResetCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_all_success() {
        assertParseSuccess(parser, "all", new ResetCommand());
    }

    @Test
    public void parse_allUpper_success() {
        assertParseSuccess(parser, "ALL", new ResetCommand());
    }

    @Test
    public void parse_allMixed_success() {
        assertParseSuccess(parser, "AlL", new ResetCommand());
    }


    @Test
    public void parse_allWithWhitespace_success() throws ParseException {
        assertParseSuccess(parser, "   all   ", new ResetCommand());
    }

    @Test
    public void parse_extraTokens_failure() {
        assertParseFailure(parser, "all now", String.format(
                seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT, ResetCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "alls", String.format(
                seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT, ResetCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "some all", String.format(
                seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT, ResetCommand.MESSAGE_USAGE));
    }
}
