package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Parent;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

public class ResetCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        setAllPersonsPayment(model, true);
        setAllPersonsPayment(expectedModel, false);
    }

    @Test
    public void execute_resetsAllToUnpaid_success() {
        ResetCommand command = new ResetCommand();
        assertCommandSuccess(command, model, ResetCommand.MESSAGE_SUCCESS, expectedModel);

        assertAllUnpaid(model);
    }

    @Test
    public void equals_sameType_isTrue() {
        assertTrue(new ResetCommand().equals(new ResetCommand()));
    }

    private void setAllPersonsPayment(Model m, boolean paid) {
        List<Person> list = m.getAddressBook().getPersonList();
        for (Person p : list) {
            if (p instanceof Student s) {
                Student updated = new Student(
                        s.getName(),
                        s.getPhone(),
                        s.getEmail(),
                        s.getAddress(),
                        s.getNote(),
                        s.getSchedule(),
                        s.getCost(),
                        new PaymentStatus(paid),
                        s.getTags()
                );
                m.setPerson(s, updated);
            } else if (p instanceof Parent par) {
                Parent updated = new Parent(
                        par.getName(),
                        par.getPhone(),
                        par.getEmail(),
                        par.getAddress(),
                        par.getNote(),
                        par.getCost(),
                        new PaymentStatus(paid),
                        par.getTags()
                );
                m.setPerson(par, updated);
            } else {
                // If there are other subtypes of Person with payment status in the future, add handling here
            }
        }
    }

    private void assertAllUnpaid(Model m) {
        for (Person p : m.getAddressBook().getPersonList()) {
            PaymentStatus ps = p.getPaymentStatus();
            // null-safe check
            if (ps != null) {
                assertTrue(!ps.isPaid());
            }
        }
    }
}
