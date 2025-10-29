package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PaidCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_toggleToPaid_success() throws Exception {
        Name targetName = ALICE.getName();
        PaidCommand paidCommand = new PaidCommand(targetName);

        Person originalPerson = model.getAddressBook().getPersonList().stream()
                .filter(person -> person.getName().equals(targetName))
                .findFirst()
                .orElseThrow();

        Person paidPerson = new PersonBuilder(originalPerson).withPaymentStatus(true).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(originalPerson, paidPerson);

        assertCommandSuccess(paidCommand, model,
                String.format(PaidCommand.MESSAGE_MARK_PAID_SUCCESS, paidPerson.getName()), expectedModel);
    }

    @Test
    public void execute_toggleToPaidByIndex_success() throws Exception {
        Index targetIndex = Index.fromOneBased(1);
        PaidCommand paidCommand = new PaidCommand(targetIndex);

        Person personToMark = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        Person paidPerson = new PersonBuilder(personToMark).withPaymentStatus(true).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personToMark, paidPerson);

        assertCommandSuccess(paidCommand, model,
                String.format(PaidCommand.MESSAGE_MARK_PAID_SUCCESS, paidPerson.getName()), expectedModel);
    }

    @Test
    public void execute_toggleToUnpaid_success() throws Exception {
        Person paidAlice = new PersonBuilder(ALICE).withPaymentStatus(true).build();
        model.setPerson(ALICE, paidAlice);

        PaidCommand paidCommand = new PaidCommand(ALICE.getName());

        Person unpaidAlice = new PersonBuilder(paidAlice).withPaymentStatus(false).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(paidAlice, unpaidAlice);

        assertCommandSuccess(paidCommand, model,
                String.format(PaidCommand.MESSAGE_MARK_UNPAID_SUCCESS, unpaidAlice.getName()), expectedModel);
    }

    @Test
    public void execute_toggleToUnpaidByIndex_success() throws Exception {
        Person paidAlice = new PersonBuilder(ALICE).withPaymentStatus(true).build();
        model.setPerson(ALICE, paidAlice);
        Index targetIndex = Index.fromOneBased(1);
        PaidCommand paidCommand = new PaidCommand(targetIndex);

        Person unpaidAlice = new PersonBuilder(paidAlice).withPaymentStatus(false).build();
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(paidAlice, unpaidAlice);

        assertCommandSuccess(paidCommand, model,
                String.format(PaidCommand.MESSAGE_MARK_UNPAID_SUCCESS, unpaidAlice.getName()), expectedModel);

    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        PaidCommand paidCommand = new PaidCommand(outOfBoundIndex);

        assertCommandFailure(paidCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_personNotFound_throwsCommandException() {
        Name missingName = new Name("Non Existent");
        PaidCommand paidCommand = new PaidCommand(missingName);
        assertCommandFailure(paidCommand, model,
                String.format(PaidCommand.MESSAGE_PERSON_NOT_FOUND, missingName));
    }

    @Test
    public void equals() {
        PaidCommand firstCommand = new PaidCommand(ALICE.getName());
        PaidCommand secondCommand = new PaidCommand(new Name("Bob Choo"));
        PaidCommand indexCommand = new PaidCommand(Index.fromOneBased(1));

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        PaidCommand firstCommandCopy = new PaidCommand(ALICE.getName());
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different values -> returns false
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, secondCommand);
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, indexCommand);
    }
}
