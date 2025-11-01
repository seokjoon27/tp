package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnlinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnlinkCommand object
 */
public class UnlinkCommandParser implements Parser<UnlinkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of an {@code UnlinkCommand}
     * and returns an {@code UnlinkCommand} object for execution.
     *
     * @param args the input string containing the student and parent indexes with prefixes
     * @return an {@code UnlinkCommand} constructed with the parsed student and parent indexes
     * @throws ParseException if the input string is missing required prefixes or contains invalid indexes
     */
    public UnlinkCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE));
        }
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PARENT, PREFIX_STUDENT);
        if (!argMultimap.getValue(PREFIX_PARENT).isPresent() || !argMultimap.getValue(PREFIX_STUDENT).isPresent()
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE));
        }

        String parentValue = argMultimap.getValue(PREFIX_PARENT).get().trim();
        String studentValue = argMultimap.getValue(PREFIX_STUDENT).get().trim();

        if (parentValue.isEmpty() || studentValue.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE));
        }

        try {
            Index parentIndex = ParserUtil.parseIndex(parentValue);
            Index studentIndex = ParserUtil.parseIndex(studentValue);
            return new UnlinkCommand(studentIndex, parentIndex);
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnlinkCommand.MESSAGE_USAGE));
        }
    }
}
