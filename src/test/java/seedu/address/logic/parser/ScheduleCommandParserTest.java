package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.model.person.Schedule;

public class ScheduleCommandParserTest {

    private static final String VALID_DAY_SCHEDULE = "Monday 14:00-16:00";
    private static final String VALID_DATE_SCHEDULE = "12-10-2025 14:00-16:00";
    private static final String INVALID_SCHEDULE = "InvalidSchedule";
    private final ScheduleCommandParser parser = new ScheduleCommandParser();

    // Helper for parse success
    private void assertParseSuccessForSchedule(String userInput, Index index, String scheduleValue) {
        ScheduleCommand expectedCommand = new ScheduleCommand(index, new Schedule(scheduleValue));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // Helper for parse failure
    private void assertParseFailureForSchedule(String userInput, String expectedMessage) {
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_validScheduleSpecified_success() throws Exception {
        Index targetIndex = INDEX_FIRST_PERSON;

        assertParseSuccessForSchedule(targetIndex.getOneBased() + " " + PREFIX_SCHEDULE + VALID_DAY_SCHEDULE,
                targetIndex, VALID_DAY_SCHEDULE);

        assertParseSuccessForSchedule(targetIndex.getOneBased() + " " + PREFIX_SCHEDULE + VALID_DATE_SCHEDULE,
                targetIndex, VALID_DATE_SCHEDULE);

        assertParseSuccessForSchedule(targetIndex.getOneBased()
                + " " + PREFIX_SCHEDULE, targetIndex, "");
    }

    @Test
    public void parse_invalidScheduleFormat_failure() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String message = "Invalid schedule format. Use either: "
                + "DAY HH:mm-HH:mm or MM-DD-YYYY HH:mm-HH:mm Example: 'Monday 14:00-16:00', "
                + "'12-10-2025 14:00-16:00' End time must be after start time and cannot cross midnight.";

        assertParseFailureForSchedule(targetIndex.getOneBased()
                + " " + PREFIX_SCHEDULE + INVALID_SCHEDULE, message);
        assertParseFailureForSchedule(targetIndex.getOneBased()
                + " " + PREFIX_SCHEDULE + "Monday 16:00-14:00",
                "Invalid time format. End time must be after start time and "
                        + "cannot cross midnight (e.g., 14:00-16:00).");
        assertParseFailureForSchedule(targetIndex.getOneBased()
                + " " + PREFIX_SCHEDULE + "Monday 14-16",
                "Invalid time format. Time must be in HH:mm (24-hour) format.");
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE);

        assertParseFailureForSchedule(ScheduleCommand.COMMAND_WORD, expectedMessage);
        assertParseFailureForSchedule(ScheduleCommand.COMMAND_WORD + " " + PREFIX_SCHEDULE + "Mon 14:00-16:00",
                expectedMessage);
    }

    //EP: Extra whitespace
    @Test
    public void parse_whitespaceTolerance_success() throws Exception {
        Index targetIndex = INDEX_FIRST_PERSON;
        String input = "  " + targetIndex.getOneBased() + "   " + PREFIX_SCHEDULE + "   " + VALID_DAY_SCHEDULE + "   ";
        assertParseSuccessForSchedule(input, targetIndex, VALID_DAY_SCHEDULE);
    }
}
