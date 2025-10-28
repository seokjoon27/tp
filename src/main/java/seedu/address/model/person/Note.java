package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's Note in the address book.
 * Guarantees: immutable; is always valid
 */
public class Note {
    public static final String MESSAGE_CONSTRAINTS = "Notes should not exceed 100 characters.";
    public static final int MAX_LENGTH = 100;

    public final String value;

    /**
     * Constructs a Note with the specified value.
     * @param note the content of the note; must not be null
     * @throws IllegalArgumentException if note exceeds 100 characters
     */
    public Note(String note) {
        requireNonNull(note);
        if (!isValidNoteLength(note)) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        value = note;
    }

    /**
     * Returns true if the given note is 100 characters or fewer.
     */
    public static boolean isValidNoteLength(String test) {
        requireNonNull(test);
        return test.length() <= MAX_LENGTH;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Note)) {
            return false;
        }

        Note otherNote = (Note) other;
        boolean isSameValue = value.equals(otherNote.value);

        return isSameValue;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
