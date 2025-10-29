package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.person.Name;
import seedu.address.model.person.Parent;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;
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
                String.format(PaidCommand.MESSAGE_MARK_PAID_SUCCESS, Messages.format(paidPerson)), expectedModel);
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
                String.format(PaidCommand.MESSAGE_MARK_PAID_SUCCESS, Messages.format(paidPerson)), expectedModel);
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
                String.format(PaidCommand.MESSAGE_MARK_UNPAID_SUCCESS, Messages.format(unpaidAlice)), expectedModel);
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
                String.format(PaidCommand.MESSAGE_MARK_UNPAID_SUCCESS, Messages.format(unpaidAlice)), expectedModel);
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
    public void execute_parentTogglesChildren_success() throws CommandException {
        Model localModel = new ModelManager();

        Student childOne = (Student) new PersonBuilder().withName("Child One")
                .withPhone("81230000")
                .withEmail("childone@example.com")
                .withAddress("1 Child Lane")
                .withCost("40")
                .withPaymentStatus(false)
                .build();
        Student childTwo = (Student) new PersonBuilder().withName("Child Two")
                .withPhone("81230001")
                .withEmail("childtwo@example.com")
                .withAddress("2 Child Lane")
                .withCost("60")
                .withPaymentStatus(false)
                .build();
        Parent parent = (Parent) new PersonBuilder().withType("p")
                .withName("Parent Example")
                .withPhone("91230000")
                .withEmail("parent@example.com")
                .withAddress("10 Parent Road")
                .withCost("0")
                .withPaymentStatus(false)
                .build();

        localModel.addPerson(childOne);
        localModel.addPerson(childTwo);
        localModel.addPerson(parent);

        Parent linkedParent = getParent(localModel, "Parent Example");
        Student linkedChildOne = getStudent(localModel, "Child One");
        linkChildToParent(localModel, linkedChildOne, linkedParent);

        linkedParent = getParent(localModel, "Parent Example");
        Student linkedChildTwo = getStudent(localModel, "Child Two");
        linkChildToParent(localModel, linkedChildTwo, linkedParent);

        PaidCommand paidCommand = new PaidCommand(linkedParent.getName());
        CommandResult firstResult = paidCommand.execute(localModel);

        Parent parentAfterFirstToggle = getParent(localModel, "Parent Example");
        Student childOneAfterFirstToggle = getStudent(localModel, "Child One");
        Student childTwoAfterFirstToggle = getStudent(localModel, "Child Two");

        assertTrue(childOneAfterFirstToggle.getPaymentStatus().isPaid());
        assertTrue(childTwoAfterFirstToggle.getPaymentStatus().isPaid());
        assertTrue(parentAfterFirstToggle.getPaymentStatus().isPaid());
        assertEquals(String.format(PaidCommand.MESSAGE_MARK_PAID_SUCCESS,
                Messages.format(parentAfterFirstToggle)), firstResult.getFeedbackToUser());

        PaidCommand secondToggle = new PaidCommand(parentAfterFirstToggle.getName());
        CommandResult secondResult = secondToggle.execute(localModel);

        Parent parentAfterSecondToggle = getParent(localModel, "Parent Example");
        assertFalse(parentAfterSecondToggle.getPaymentStatus().isPaid());
        assertFalse(getStudent(localModel, "Child One").getPaymentStatus().isPaid());
        assertFalse(getStudent(localModel, "Child Two").getPaymentStatus().isPaid());
        assertEquals(String.format(PaidCommand.MESSAGE_MARK_UNPAID_SUCCESS,
                Messages.format(parentAfterSecondToggle)), secondResult.getFeedbackToUser());
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

    private Student getStudent(Model model, String name) {
        return (Student) model.getAddressBook().getPersonList().stream()
                .filter(person -> person instanceof Student && person.getName().fullName.equals(name))
                .findFirst()
                .orElseThrow();
    }

    private Parent getParent(Model model, String name) {
        return (Parent) model.getAddressBook().getPersonList().stream()
                .filter(person -> person instanceof Parent && person.getName().fullName.equals(name))
                .findFirst()
                .orElseThrow();
    }

    private void linkChildToParent(Model model, Student child, Parent parent) {
        child.addParent(parent);
        parent.addChild(child);
        model.setPerson(child, child);
    }
}
