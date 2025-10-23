package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnlinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new UnlinkCommand object
 */
public class UnlinkCommandParser implements Parser<UnlinkCommand> {
    private static final String MESSAGE_MISSING_PARENT_INDEX =
            "Missing parent index. Example: unlink student/1 parent/2";
    private static final String MESSAGE_MISSING_STUDENT_INDEX =
            "Missing student index. Example: unlink student/1 parent/2";

    /**
     * Parses the given {@code String} of arguments in the context of an {@code UnlinkCommand}
     * and returns an {@code UnlinkCommand} object for execution.
     *
     * @param args the input string containing the student and parent indexes with prefixes
     * @return an {@code UnlinkCommand} constructed with the parsed student and parent indexes
     * @throws ParseException if the input string is missing required prefixes or contains invalid indexes
     */
    public UnlinkCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PARENT, PREFIX_STUDENT);

        Index parentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PARENT)
                .orElseThrow(() -> new ParseException(MESSAGE_MISSING_PARENT_INDEX)));
        Index studentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STUDENT)
                .orElseThrow(() -> new ParseException(MESSAGE_MISSING_STUDENT_INDEX)));

        return new UnlinkCommand(studentIndex, parentIndex);
    }
}
