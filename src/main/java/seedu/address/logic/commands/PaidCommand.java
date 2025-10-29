package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Name;
import seedu.address.model.person.Parent;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Toggles the payment status of a person.
 */
public class PaidCommand extends Command {

    public static final String COMMAND_WORD = "paid";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Toggles the payment status of the person identified "
            + "by the index or name.\n"
            + "Parameters: INDEX (must be a positive integer) | " + PREFIX_NAME + "NAME\n"
            + "Example: " + COMMAND_WORD + " 1 | " + COMMAND_WORD + " " + PREFIX_NAME + "Alex Yeoh";

    public static final String MESSAGE_MARK_PAID_SUCCESS = "Marked as paid: %1$s";
    public static final String MESSAGE_MARK_UNPAID_SUCCESS = "Marked as unpaid: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person with the name %1$s was found.";
    public static final String MESSAGE_PARENT_NO_CHILDREN =
            "This parent has no linked children. Link at least one student before toggling payment.";

    private final Name name;
    private final Index targetIndex;

    /**
     * Constructs a {@code PaidCommand} targeting the person identified by {@code name}.
     */
    public PaidCommand(Name name) {
        this.name = name;
        this.targetIndex = null;
    }

    /**
     * Constructs a {@code PaidCommand} targeting the person at the specified {@code targetIndex}.
     */
    public PaidCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
        this.name = null;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person personToMark;

        if (targetIndex != null) {
            List<Person> lastShownList = model.getFilteredPersonList();
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToMark = lastShownList.get(targetIndex.getZeroBased());
        } else {
            List<Person> personList = model.getAddressBook().getPersonList();
            personToMark = personList.stream()
                    .filter(person -> person.getName().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, name)));
        }

        boolean newStatusIsPaid = !personToMark.getPaymentStatus().isPaid();
        Person markedPerson;
        if (personToMark.getType().isStudent()) {
            PaymentStatus updatedStatus = new PaymentStatus(newStatusIsPaid);
            Student studentToMark = (Student) personToMark; // safe cast
            Student updatedStudent = new Student(studentToMark.getName(), studentToMark.getPhone(),
                    studentToMark.getEmail(),
                    studentToMark.getAddress(), studentToMark.getNote(), studentToMark.getSchedule(),
                    studentToMark.getCost(), updatedStatus,
                    studentToMark.getTags());
            studentToMark.getParents().forEach(updatedStudent::addParent);
            updatedStudent.setLinkedNames(new ArrayList<>(studentToMark.getLinkedNames()));
            markedPerson = updatedStudent;
            model.setPerson(personToMark, markedPerson);
        } else if (personToMark.getType().isParent()) {
            Parent parentToToggle = (Parent) personToMark;
            List<Student> children = resolveChildren(model, parentToToggle);
            if (children.isEmpty()) {
                throw new CommandException(MESSAGE_PARENT_NO_CHILDREN);
            }

            for (Student child : children) {
                Student latestChild = resolveStudent(model, child);
                if (latestChild == null) {
                    continue;
                }
                Student updatedChild = cloneStudentWithStatus(model, latestChild, newStatusIsPaid);
                model.setPerson(latestChild, updatedChild);
            }

            // parent instance might have been replaced during child updates
            Parent updatedParent = resolveParent(model, parentToToggle);
            if (updatedParent == null) {
                throw new CommandException(MESSAGE_PERSON_NOT_FOUND);
            }
            markedPerson = updatedParent;
        } else {
            throw new IllegalStateException("Unsupported person type: " + personToMark.getType());
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        String successMessage = newStatusIsPaid
                ? MESSAGE_MARK_PAID_SUCCESS
                : MESSAGE_MARK_UNPAID_SUCCESS;
        return new CommandResult(String.format(successMessage, markedPerson.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PaidCommand)) {
            return false;
        }

        PaidCommand otherPaidCommand = (PaidCommand) other;
        return Objects.equals(name, otherPaidCommand.name)
                && Objects.equals(targetIndex, otherPaidCommand.targetIndex);
    }

    private List<Student> resolveChildren(Model model, Parent parent) {
        List<Student> children = new ArrayList<>();
        for (Person person : model.getAddressBook().getPersonList()) {
            if (person instanceof Student student) {
                boolean linked = student.getParents().stream().anyMatch(p -> p.isSamePerson(parent));
                if (linked) {
                    children.add(student);
                }
            }
        }
        return children;
    }

    private Student resolveStudent(Model model, Student target) {
        return (Student) model.getAddressBook().getPersonList().stream()
                .filter(person -> person instanceof Student && person.isSamePerson(target))
                .findFirst()
                .orElse(null);
    }

    private Parent resolveParent(Model model, Parent target) {
        return (Parent) model.getAddressBook().getPersonList().stream()
                .filter(person -> person instanceof Parent && person.isSamePerson(target))
                .findFirst()
                .orElse(null);
    }

    private Student cloneStudentWithStatus(Model model, Student original, boolean isPaid) {
        PaymentStatus updatedStatus = new PaymentStatus(isPaid);
        Student updatedStudent = new Student(
                original.getName(),
                original.getPhone(),
                original.getEmail(),
                original.getAddress(),
                original.getNote(),
                original.getSchedule(),
                original.getCost(),
                updatedStatus,
                original.getTags()
        );

        for (Parent parent : original.getParents()) {
            Parent latestParent = resolveParent(model, parent);
            if (latestParent != null) {
                updatedStudent.addParent(latestParent);
            }
        }
        updatedStudent.setLinkedNames(new ArrayList<>(original.getLinkedNames()));
        return updatedStudent;
    }
}
