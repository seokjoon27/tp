package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.model.person.Note;

public class NoteCommandParserTest {
    private static final String VALID_NOTE = "Some Note.";
    private static final String EMPTY_NOTE = "";
    private static final String LONG_NOTE = "a".repeat(Note.MAX_LENGTH + 1);
    private final NoteCommandParser parser = new NoteCommandParser();

    @Test
    public void parse_validArgsWithNote_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_NOTE + VALID_NOTE;

        NoteCommand expectedCommand = new NoteCommand(targetIndex, new Note(VALID_NOTE));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validArgsWithExtraWhitespace_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = "   " + targetIndex.getOneBased() + "   " + PREFIX_NOTE + "   " + VALID_NOTE + "   ";

        NoteCommand expectedCommand = new NoteCommand(targetIndex, new Note(VALID_NOTE.trim()));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);
        String userInput = PREFIX_NOTE + VALID_NOTE;
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_noParameters_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);
        String userInput = "a " + PREFIX_NOTE + VALID_NOTE;
        assertParseFailure(parser, userInput, expectedMessage);
    }

    //Defensive Coding: test above 100 characters boundary
    @Test
    public void parse_noteTooLong_failure() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NOTE + LONG_NOTE;
        assertParseFailure(parser, userInput, Note.MESSAGE_CONSTRAINTS);
    }

    //Defensive Coding: test 100 characters boundary
    @Test
    public void parse_noteAtMaxLength_success() {
        String validBoundaryNote = "a".repeat(Note.MAX_LENGTH);
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NOTE + validBoundaryNote;

        NoteCommand expectedCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(validBoundaryNote));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    //Defensive Coding: test 99 characters
    @Test
    public void parse_noteAtSuitableLength_success() {
        String validBoundaryNote = "a".repeat(Note.MAX_LENGTH - 1);
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NOTE + validBoundaryNote;

        NoteCommand expectedCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(validBoundaryNote));
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
