package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import java.util.logging.Logger;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new {@code NoteCommand} object
 */
public class NoteCommandParser implements Parser<NoteCommand> {
    private static final Logger logger = Logger.getLogger(NoteCommandParser.class.getName());

    /**
     * Parses the given {@code String} of arguments in the context of the {@code NoteCommand}
     * and returns a {@code NoteCommand} object for execution.
     * @param args user input string
     * @throws ParseException if the user input does not conform the expected format
     */
    public NoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String normalizedArgs = args.replaceAll("(?i)NOTE/", "note/");
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(normalizedArgs, PREFIX_NOTE);
        assert argMultimap != null : "ArgumentMultimap should not be null after tokenization";


        Index index = parseIndexSafely(argMultimap);
        Note note = parseNoteSafely(argMultimap);

        logger.info(String.format("Parsed NoteCommand with index %d and note: %s",
                index.getOneBased(), note.toString()));

        return new NoteCommand(index, note);
    }

    /**
     * Parses and validates the index from user input.
     * @throws ParseException if the index is invalid.
     */
    private Index parseIndexSafely(ArgumentMultimap argMultimap) throws ParseException {
        try {
            return ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            logger.warning("Invalid index provided: " + argMultimap.getPreamble());
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Parses and validates the note content from user input.
     * @throws ParseException if the note length is invalid.
     */
    private Note parseNoteSafely(ArgumentMultimap argMultimap) throws ParseException {
        String noteString = argMultimap.getValue(PREFIX_NOTE).orElse("");
        try {
            return new Note(noteString);
        } catch (IllegalArgumentException e) {
            logger.warning("Invalid note provided: exceeds " + Note.MAX_LENGTH + " characters.");
            throw new ParseException(Note.MESSAGE_CONSTRAINTS);
        }
    }
}
