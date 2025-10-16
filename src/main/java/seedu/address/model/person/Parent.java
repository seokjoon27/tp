package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
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
    public Parent(Name name, Phone phone, Email email, Address address, Note note,
                  Cost cost, PaymentStatus paymentStatus, Set<Tag> tags) {
        super(new Type(Type.PARENT), name, phone, email, address, note, cost, paymentStatus, tags);
    }
    /**
     * Adds a child {@link Student} to this parent's list of children.
     * If the student is already linked, throw an exception.
     *
     * @param child the {@link Student} to be added as a child
     * @throws DuplicatePersonException if child is already linked.
     */
    public void addChild(Student child) {
        if (!children.contains(child)) {
            children.add(child);
        } else {
            throw new DuplicatePersonException();
        }
    }
    /**
     * Removes a child {@link Student} from this parent's list of children.
     * If the student is not currently linked, throw an exception.
     *
     * @param child the {@link Student} to be removed from this parent's children
     * @throws PersonNotFoundException if the child is not in the list of children
     */
    public void removeChild(Student child) {
        if (children.contains(child)) {
            children.remove(child);
        } else {
            throw new PersonNotFoundException();
        }
    }

    public List<Student> getChildren() {
        return children;
    }
}
