package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.LinkCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PARENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT;

public class LinkCommandParser implements Parser<LinkCommand> {
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
