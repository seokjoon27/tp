package seedu.address.model.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Represents a student in the address book.
 * Inherits common fields and methods from {@link Person}.
 */
public class Student extends Person {
    private final List<Parent> parents = new ArrayList<>();
    private List<String> linkedNames = new ArrayList<>(); // temporary names from JSON

    private final Schedule schedule;
    /**
     * Constructs a {@code Student} with the specified details.
     *
     */
    public Student(Name name, Phone phone, Email email, Address address, Note note, Schedule schedule,
                   Cost cost, PaymentStatus paymentStatus, Set<Tag> tags) {
        super(new Type(Type.STUDENT), name, phone, email, address, note, cost, paymentStatus, tags);
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Returns a copy of this student with the updated schedule.
     * Used when executing ScheduleCommand.
     */
    public Student withSchedule(Schedule newSchedule) {
        return new Student(getName(), getPhone(), getEmail(), getAddress(), getNote(),
                newSchedule, getCost(), getPaymentStatus(), getTags());
    }


    @Override
    public int hashCode() {
        return super.hashCode() + schedule.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + "; Schedule: " + schedule;
    }
    /**
     * Adds a parent {@link Parent} to this student's list of parents.
     * If the parent is already linked, throw an exception.
     *
     * @param parent the {@link Student} to be added
     * @throws DuplicatePersonException if the parent is already linked
     */
    public void addParent(Parent parent) {
        if (!this.parents.contains(parent)) {
            parents.add(parent);
        } else {
            throw new DuplicatePersonException();
        }
    }
    /**
     * Removes a parent {@link Parent} from this student's list of parents.
     * If the parent is not currently linked, an exception is thrown.
     *
     * @param parent the {@link Parent} to be removed from this student's parents
     * @throws PersonNotFoundException if the parent is not currently linked
     */
    public void removeParent(Parent parent) {
        if (this.parents.contains(parent)) {
            parents.remove(parent);
        } else {
            throw new PersonNotFoundException();
        }
    }

    public void setLinkedNames(List<String> names) {
        this.linkedNames = names;
    }

    public List<String> getLinkedNames() {
        return linkedNames;
    }

    public List<Parent> getParents() {
        return parents;
    }
}
