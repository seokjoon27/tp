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

    // Helper for parse success
    private void assertParseSuccessForNote(String userInput, Index index, String noteValue) {
        NoteCommand expectedCommand = new NoteCommand(index, new Note(noteValue));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    // Helper for parse failure
    private void assertParseFailureForNote(String userInput, String expectedMessage) {
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_validArgsWithNote_success() {
        assertParseSuccessForNote(INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NOTE + VALID_NOTE,
                INDEX_FIRST_PERSON, VALID_NOTE);
    }

    //EP: extra whitespace
    @Test
    public void parse_validArgsWithExtraWhitespace_success() {
        String userInput = "   " + INDEX_FIRST_PERSON.getOneBased() + "   " + PREFIX_NOTE + "   " + VALID_NOTE + "   ";
        assertParseSuccessForNote(userInput, INDEX_FIRST_PERSON, VALID_NOTE.trim());
    }

    @Test
    public void parse_missingIndex_failure() {
        String userInput = PREFIX_NOTE + VALID_NOTE;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);
        assertParseFailureForNote(userInput, expectedMessage);
    }

    //EP: empty note
    @Test
    public void parse_noParameters_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);
        assertParseFailureForNote("", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String userInput = "a " + PREFIX_NOTE + VALID_NOTE;
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);
        assertParseFailureForNote(userInput, expectedMessage);
    }

    //EP: 101 characters, exceeds boundary
    @Test
    public void parse_noteTooLong_failure() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NOTE + LONG_NOTE;
        assertParseFailureForNote(userInput, Note.MESSAGE_CONSTRAINTS);
    }

    //EP: 100 characters boundary
    @Test
    public void parse_noteAtMaxLength_success() {
        String validBoundaryNote = "a".repeat(Note.MAX_LENGTH);
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NOTE + validBoundaryNote;
        assertParseSuccessForNote(userInput, INDEX_FIRST_PERSON, validBoundaryNote);
    }

    //EP: 99 characters
    @Test
    public void parse_noteAtSuitableLength_success() {
        String validBoundaryNote = "a".repeat(Note.MAX_LENGTH - 1);
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_NOTE + validBoundaryNote;
        assertParseSuccessForNote(userInput, INDEX_FIRST_PERSON, validBoundaryNote);
    }
}
