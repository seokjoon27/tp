package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;

import java.util.logging.Logger;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Schedule;

/**
 * Parses input arguments and creates a new {@code ScheduleCommand} object
 */
public class ScheduleCommandParser implements Parser<ScheduleCommand> {
    private static final Logger logger = Logger.getLogger(ScheduleCommandParser.class.getName());

    /**
     * Parses the given {@code String} of arguments in the context of the {@code ScheduleCommand}
     * and returns a {@code ScheduleCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public ScheduleCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String normalizedArgs = args.replaceAll("(?i)SCHEDULE/", "schedule/");
        logger.fine("Parsing ScheduleCommand args: " + normalizedArgs);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(normalizedArgs, PREFIX_SCHEDULE);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ScheduleCommand.MESSAGE_USAGE), ive);
        }

        String scheduleString = argMultimap.getValue(PREFIX_SCHEDULE).orElse("");
        Schedule schedule;
        try {
            schedule = new Schedule(scheduleString);
        } catch (IllegalArgumentException e) {
            logger.warning("Schedule parsing failed: " + e.getMessage());
            throw new ParseException(e.getMessage());
        }

        return new ScheduleCommand(index, schedule);
    }
}
