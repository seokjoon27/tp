package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a parent in the address book.
 * Inherits common fields and methods from {@link Person}.
 */
public class Parent extends Person {
    private List<Student> children = new ArrayList<>();

    /**
     * Constructs a {@code Parent} with the specified details.
     *
     */
    public Parent(Name name, Phone phone, Email email, Address address, Note note, Cost cost, Set<Tag> tags) {
        super(new Type(Type.PARENT), name, phone, email, address, note, cost, tags);
    }

    public void addChild(Student child) {
        children.add(child);
    }

    public List<Student> getChildren() {
        return children;
    }
}
