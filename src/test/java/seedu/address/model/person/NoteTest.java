package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NoteTest {

    @Test
    public void equals() {
        Note note = new Note("Hello");

        // same object -> returns true
        assertTrue(note.equals(note));

        // same values -> returns true
        Note noteCopy = new Note(note.value);
        assertTrue(note.equals(noteCopy));

        // different types -> returns false
        assertFalse(note.equals(1));

        // null -> returns false
        assertFalse(note.equals(null));

        // different Note -> returns false
        Note differentNote = new Note("Bye");
        assertFalse(note.equals(differentNote));
    }

    @Test
    public void isValidNoteLength() {
        // Defensive coding: empty note
        assertTrue(Note.isValidNoteLength(""));

        // Defensive coding: at boundary
        assertTrue(Note.isValidNoteLength("a".repeat(100)));

        // Defensive coding: right below boundary
        assertTrue(Note.isValidNoteLength("a".repeat(99)));

        // Defensive coding: above boundary
        assertFalse(Note.isValidNoteLength("a".repeat(101)));
    }


    @Test
    public void constructor_noteTooLong_throwsIllegalArgumentException() {
        String longNote = "a".repeat(101); // 101 characters
        assertThrows(IllegalArgumentException.class, () -> new Note(longNote));
    }
}
