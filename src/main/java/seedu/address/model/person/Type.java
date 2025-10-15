package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's type (Student or Parent) in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidType(String)}
 */
public class Type {

    public static final String MESSAGE_CONSTRAINTS =
            "Type must be either 's' (student) or 'p' (parent), case insensitive.";

    public static final String STUDENT = "s";
    public static final String PARENT = "p";

    public final String value;

    /**
     * Constructs a {@code Type}.
     *
     * @param type A valid type string.
     */
    public Type(String type) {
        requireNonNull(type);
        checkArgument(isValidType(type), MESSAGE_CONSTRAINTS);
        this.value = type.trim().toLowerCase(); // always normalize to lowercase
    }

    /**
     * Returns true if a given string is a valid type.
     */
    public static boolean isValidType(String test) {
        if (test == null) {
            return false;
        }
        String t = test.trim().toLowerCase();
        return t.equals(STUDENT) || t.equals(PARENT);
    }

    public boolean isStudent() {
        return value.equals(STUDENT);
    }

    public boolean isParent() {
        return value.equals(PARENT);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Type
                && value.equals(((Type) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
