package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ResetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input for 'reset all'.
 * Accepts only the exact token "all" (case-insensitive) with no extra tokens.
 */
public class ResetCommandParser implements Parser<ResetCommand> {

    @Override
    public ResetCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (!trimmedArgs.equalsIgnoreCase("all")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ResetCommand.MESSAGE_USAGE));
        }

        return new ResetCommand();
    }
}
