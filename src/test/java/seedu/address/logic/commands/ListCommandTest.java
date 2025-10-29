package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listAll_showsAllPersons() {
        ListCommand command = new ListCommand(PREDICATE_SHOW_ALL_PERSONS, ListCommand.MESSAGE_SUCCESS);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertCommandSuccess(command, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listPaid_showsPaidPersons() {
        Predicate<Person> paidPredicate = p -> p.getPaymentStatus() != null && p.getPaymentStatus().isPaid();
        ListCommand command = new ListCommand(paidPredicate, "Listed persons with payment status: PAID");

        expectedModel.updateFilteredPersonList(paidPredicate);
        assertCommandSuccess(command, model, "Listed persons with payment status: PAID", expectedModel);
    }

    @Test
    public void execute_listUnpaid_showsUnpaidPersons() {
        Predicate<Person> unpaidPredicate = p -> p.getPaymentStatus() != null && !p.getPaymentStatus().isPaid();
        ListCommand command =
                new ListCommand(unpaidPredicate, "Listed persons with payment status: UNPAID");

        expectedModel.updateFilteredPersonList(unpaidPredicate);
        assertCommandSuccess(command, model, "Listed persons with payment status: UNPAID", expectedModel);
    }

    @Test
    public void execute_listSchedule_showsStudentsWithSchedules() {
        Predicate<Person> schedulePredicate = p -> p instanceof Student
                && ((Student) p).getSchedule() != null
                && !((Student) p).getSchedule().isEmpty();
        ListCommand command = new ListCommand(schedulePredicate, "Listed students with a schedule");

        expectedModel.updateFilteredPersonList(schedulePredicate);
        assertCommandSuccess(command, model, "Listed students with a schedule", expectedModel);
    }

    @Test
    public void execute_listDay_showsMatchingDaySchedules() {
        DayOfWeek monday = DayOfWeek.MONDAY;
        Predicate<Person> dayPredicate = p -> p instanceof Student
                && ((Student) p).getSchedule() != null
                && monday.equals(((Student) p).getSchedule().getDayOfWeek());
        ListCommand command = new ListCommand(dayPredicate, "Listed students with schedule on Monday");

        expectedModel.updateFilteredPersonList(dayPredicate);
        assertCommandSuccess(command, model, "Listed students with schedule on Monday", expectedModel);
    }

    @Test
    public void execute_listDate_showsMatchingDateSchedules() {
        LocalDate date = LocalDate.of(2025, 12, 12);
        String formatted = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        Predicate<Person> datePredicate = p -> p instanceof Student
                && ((Student) p).getSchedule() != null
                && date.equals(((Student) p).getSchedule().getDate());
        ListCommand command = new ListCommand(datePredicate, "Listed students with schedule on " + formatted);

        expectedModel.updateFilteredPersonList(datePredicate);
        assertCommandSuccess(command, model, "Listed students with schedule on " + formatted, expectedModel);
    }

    @Test
    public void execute_listAfterFilter_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        ListCommand command = new ListCommand(PREDICATE_SHOW_ALL_PERSONS, ListCommand.MESSAGE_SUCCESS);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        assertCommandSuccess(command, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_defaultConstructor_listsAllPersons() {
        // This specifically covers the no-arg constructor: new ListCommand()
        ListCommand command = new ListCommand();
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        assertCommandSuccess(command, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals_andHashCode() {
        ListCommand listAllA = new ListCommand(PREDICATE_SHOW_ALL_PERSONS, ListCommand.MESSAGE_SUCCESS);
        ListCommand listAllB = new ListCommand(PREDICATE_SHOW_ALL_PERSONS, ListCommand.MESSAGE_SUCCESS);

        ListCommand listPaid = new ListCommand(
                p -> p.getPaymentStatus() != null && p.getPaymentStatus().isPaid(),
                "Listed persons with payment status: PAID");

        org.junit.jupiter.api.Assertions.assertTrue(listAllA.equals(listAllA));
        org.junit.jupiter.api.Assertions.assertTrue(listAllA.equals(listAllB));
        org.junit.jupiter.api.Assertions.assertFalse(listAllA.equals(null));
        org.junit.jupiter.api.Assertions.assertFalse(listAllA.equals("not a ListCommand"));
        org.junit.jupiter.api.Assertions.assertFalse(listAllA.equals(listPaid));

        org.junit.jupiter.api.Assertions.assertEquals(listAllA.hashCode(), listAllB.hashCode());
    }
}
