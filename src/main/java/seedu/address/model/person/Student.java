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

    public void addParent(Parent parent) {
        if (!this.parents.contains(parent)) {
            parents.add(parent);
        }
    }
    public void removeParent(Parent parent) {
        if (this.parents.contains(parent)) {
            parents.remove(parent);
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
