package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a student in the address book.
 * Inherits common fields and methods from {@link Person}.
 */
public class Student extends Person {
    private List<Parent> parents =  new ArrayList<>();

    /**
     * Constructs a {@code Student} with the specified details.
     *
     */
    public Student(Name name, Phone phone, Email email, Address address, Note note, Cost cost, Set<Tag> tags) {
        super(new Type(Type.STUDENT), name, phone, email, address, note, cost, tags);
    }

    public void addParent(Parent parent) {
        if (!this.parents.contains(parent)) {
            parents.add(parent);
        }
    }

    public List<Parent> getParents() {
        return parents;
    }
}
