package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a parent in the address book.
 * Inherits common fields and methods from {@link Person}.
 */
public class Parent extends Person {

    /**
     * Constructs a {@code Parent} with the specified details.
     *
     */
    public Parent(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
    }

}
