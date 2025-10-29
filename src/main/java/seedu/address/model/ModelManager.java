package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
import seedu.address.model.person.Cost;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private boolean isRecalculatingParents;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        isRecalculatingParents = false;
        recalculateParentAggregates();
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        recalculateParentAggregates();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        requireNonNull(target);

        // First, remove all links that reference this person
        removeLinksTo(target);

        addressBook.removePerson(target);
        recalculateParentAggregates();
    }

    private void removeLinksTo(Person personToDelete) {
        List<Person> updatedPersons = new ArrayList<>();

        for (Person p : addressBook.getPersonList()) {
            if (p instanceof Student student) {
                // remove parent links
                student.getParents().removeIf(parent -> parent.isSamePerson(personToDelete));
            } else if (p instanceof Parent parent) {
                // remove student links
                parent.getChildren().removeIf(student -> student.isSamePerson(personToDelete));
            }
            updatedPersons.add(p);
        }

        addressBook.setPersons(updatedPersons);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        recalculateParentAggregates();
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
        recalculateParentAggregates();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    private void recalculateParentAggregates() {
        if (isRecalculatingParents) {
            return;
        }

        isRecalculatingParents = true;
        try {
            Map<Parent, ParentUpdateInfo> parentUpdates = new LinkedHashMap<>();
            List<Person> personsSnapshot = new ArrayList<>(addressBook.getPersonList());

            for (Person person : personsSnapshot) {
                if (!(person instanceof Parent)) {
                    continue;
                }

                Parent parent = (Parent) person;
                List<Student> resolvedChildren = resolveChildren(parent);
                Cost aggregatedCost = aggregateChildCost(resolvedChildren);
                PaymentStatus aggregatedStatus = aggregateChildPaymentStatus(resolvedChildren);

                boolean costChanged = !costsEqual(parent.getCost(), aggregatedCost);
                boolean statusChanged = parent.getPaymentStatus().isPaid() != aggregatedStatus.isPaid();

                if (!costChanged && !statusChanged) {
                    continue;
                }

                Parent updatedParent = new Parent(parent.getName(), parent.getPhone(), parent.getEmail(),
                        parent.getAddress(), parent.getNote(), aggregatedCost, aggregatedStatus, parent.getTags());
                for (Student child : resolvedChildren) {
                    updatedParent.addChild(child);
                }
                parentUpdates.put(parent, new ParentUpdateInfo(updatedParent, resolvedChildren));
            }

            if (parentUpdates.isEmpty()) {
                return;
            }

            for (Map.Entry<Parent, ParentUpdateInfo> entry : parentUpdates.entrySet()) {
                Parent oldParent = entry.getKey();
                ParentUpdateInfo updateInfo = entry.getValue();

                for (Student child : updateInfo.resolvedChildren) {
                    replaceParentReference(child, oldParent, updateInfo.updatedParent);
                }

                addressBook.setPerson(oldParent, updateInfo.updatedParent);
            }
        } finally {
            isRecalculatingParents = false;
        }
    }

    private List<Student> resolveChildren(Parent parent) {
        List<Student> resolvedChildren = new ArrayList<>();

        for (Student child : parent.getChildren()) {
            Student latestChild = resolveLatestChild(child);
            if (latestChild == null) {
                continue;
            }
            if (resolvedChildren.stream().noneMatch(existing -> existing.isSamePerson(latestChild))) {
                resolvedChildren.add(latestChild);
            }
        }
        return resolvedChildren;
    }

    private Student resolveLatestChild(Student child) {
        for (Person person : addressBook.getPersonList()) {
            if (person instanceof Student) {
                Student student = (Student) person;
                if (student.isSamePerson(child)) {
                    return student;
                }
            }
        }
        return null;
    }

    private Cost aggregateChildCost(List<Student> children) {
        if (children.isEmpty()) {
            return new Cost("0");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Student child : children) {
            Cost childCost = child.getCost();
            if (childCost == null) {
                continue;
            }
            total = total.add(new BigDecimal(childCost.value));
        }
        String totalString = total.compareTo(BigDecimal.ZERO) == 0
                ? "0"
                : total.stripTrailingZeros().toPlainString();
        return new Cost(totalString);
    }

    private PaymentStatus aggregateChildPaymentStatus(List<Student> children) {
        if (children.isEmpty()) {
            return new PaymentStatus(false);
        }

        boolean allPaid = children.stream()
                .allMatch(child -> child.getPaymentStatus() != null && child.getPaymentStatus().isPaid());
        return new PaymentStatus(allPaid);
    }

    private boolean costsEqual(Cost existingCost, Cost aggregatedCost) {
        if (existingCost == null) {
            return aggregatedCost == null;
        }
        return aggregatedCost != null && existingCost.equals(aggregatedCost);
    }

    private void replaceParentReference(Student child, Parent oldParent, Parent newParent) {
        List<Parent> parents = child.getParents();
        parents.removeIf(parent -> parent.isSamePerson(oldParent));

        boolean alreadyLinked = parents.stream().anyMatch(parent -> parent.isSamePerson(newParent));
        if (!alreadyLinked) {
            try {
                child.addParent(newParent);
            } catch (DuplicatePersonException e) {
                // ignore as parent already linked in another form
            }
        }
        addressBook.setPerson(child, child);
    }

    private static class ParentUpdateInfo {
        private final Parent updatedParent;
        private final List<Student> resolvedChildren;

        private ParentUpdateInfo(Parent updatedParent, List<Student> resolvedChildren) {
            this.updatedParent = updatedParent;
            this.resolvedChildren = resolvedChildren;
        }
    }
}
