package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
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
    public void execute_markPaid_success() throws Exception {
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
                String.format(PaidCommand.MESSAGE_MARK_PAID_SUCCESS, Messages.format(paidPerson)), expectedModel);
    }

    @Test
    public void execute_alreadyPaid_throwsCommandException() {
        Person paidAlice = new PersonBuilder(ALICE).withPaymentStatus(true).build();
        model.setPerson(ALICE, paidAlice);

        PaidCommand paidCommand = new PaidCommand(ALICE.getName());

        assertCommandFailure(paidCommand, model,
                String.format(PaidCommand.MESSAGE_ALREADY_PAID, ALICE.getName()));
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

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        PaidCommand firstCommandCopy = new PaidCommand(ALICE.getName());
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different values -> returns false
        org.junit.jupiter.api.Assertions.assertNotEquals(firstCommand, secondCommand);
    }
}
