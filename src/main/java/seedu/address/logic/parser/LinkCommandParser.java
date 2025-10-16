package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new LinkCommand object
 */
public class LinkCommandParser implements Parser<LinkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of a {@code LinkCommand}
     * and returns a {@code LinkCommand} object for execution.
     *
     * @param args the input string containing the student and parent indexes with prefixes
     * @return a {@code LinkCommand} constructed with the parsed student and parent indexes
     * @throws ParseException if the input string is missing required prefixes or contains invalid indexes
     */
    public LinkCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PARENT, PREFIX_STUDENT);

        Index parentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PARENT)
                .orElseThrow(() -> new ParseException("Missing parent index.")));
        Index studentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STUDENT)
                .orElseThrow(() -> new ParseException("Missing student index.")));

        return new LinkCommand(studentIndex, parentIndex);
    }
}
