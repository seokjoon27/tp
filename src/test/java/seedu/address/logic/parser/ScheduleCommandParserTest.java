package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Schedule;

public class ScheduleCommandParserTest {

    private ScheduleCommandParser parser = new ScheduleCommandParser();

    @Test
    public void parse_validScheduleSpecified_success() throws ParseException {
        Index targetIndex = INDEX_FIRST_PERSON;

        // Valid schedule: day + time
        String input1 = targetIndex.getOneBased() + " " + PREFIX_SCHEDULE + "Monday 14:00-16:00";
        Schedule schedule1 = new Schedule("Monday 14:00-16:00");
        ScheduleCommand expectedCommand1 = new ScheduleCommand(targetIndex, schedule1);
        assertParseSuccess(parser, input1, expectedCommand1);

        // Valid schedule: date + time
        String input2 = targetIndex.getOneBased() + " " + PREFIX_SCHEDULE + "12-10-2025 14:00-16:00";
        Schedule schedule2 = new Schedule("12-10-2025 14:00-16:00");
        ScheduleCommand expectedCommand2 = new ScheduleCommand(targetIndex, schedule2);
        assertParseSuccess(parser, input2, expectedCommand2);

        // Empty schedule
        String input3 = targetIndex.getOneBased() + " " + PREFIX_SCHEDULE;
        Schedule schedule3 = new Schedule("");
        ScheduleCommand expectedCommand3 = new ScheduleCommand(targetIndex, schedule3);
        assertParseSuccess(parser, input3, expectedCommand3);
    }

    @Test
    public void parse_invalidScheduleFormat_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // Invalid schedule string
        String input = targetIndex.getOneBased() + "  " + PREFIX_SCHEDULE + "InvalidSchedule";
        assertParseFailure(parser, input,
                "Invalid schedule format. Use either: "
                        + "DAY HH:mm-HH:mm or MM-DD-YYYY HH:mm-HH:mm Example: 'Monday 14:00-16:00', "
                        + "'12-10-2025 14:00-16:00' End time must be after start time.");
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE);

        // no arguments
        assertParseFailure(parser, ScheduleCommand.COMMAND_WORD, expectedMessage);

        // no index
        assertParseFailure(parser, ScheduleCommand.COMMAND_WORD + " "
                + PREFIX_SCHEDULE + "Mon 14:00-16:00", expectedMessage);
    }
}
