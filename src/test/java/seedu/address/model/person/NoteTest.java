package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    public void equals() {
        Note Note = new Note("Hello");

        // same object -> returns true
        assertTrue(Note.equals(Note));

        // same values -> returns true
        Note NoteCopy = new Note(Note.value);
        assertTrue(Note.equals(NoteCopy));

        // different types -> returns false
        assertFalse(Note.equals(1));

        // null -> returns false
        assertFalse(Note.equals(null));

        // different Note -> returns false
        Note differentNote = new Note("Bye");
        assertFalse(Note.equals(differentNote));
    }
}