package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.model.Model;
import seedu.address.model.person.Student;

/**
 * Tests for {@link ListCommandParser}.
 * Matches the current ListCommand and ListCommandParser implementations.
 */
public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_emptyArgs_returnsListAllCommand() throws Exception {
        String userInput = "   ";
        ListCommand expectedCommand =
                new ListCommand(Model.PREDICATE_SHOW_ALL_PERSONS, "Listed all persons.");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_paidArgs_returnsPaidListCommand() throws Exception {
        String userInput = "paid";
        ListCommand expectedCommand = new ListCommand(
                p -> p.getPaymentStatus() != null && p.getPaymentStatus().isPaid(),
                "Listed persons with payment status: PAID");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_unpaidArgs_returnsUnpaidListCommand() throws Exception {
        String userInput = "unpaid";
        ListCommand expectedCommand = new ListCommand(
                p -> p.getPaymentStatus() != null && !p.getPaymentStatus().isPaid(),
                "Listed persons with payment status: UNPAID");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_scheduleArgs_returnsScheduleListCommand() throws Exception {
        String userInput = "schedule";
        ListCommand expectedCommand = new ListCommand(
                p -> p instanceof Student
                        && ((Student) p).getSchedule() != null
                        && !((Student) p).getSchedule().isEmpty(),
                "Listed students with a schedule.");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_dayArgs_returnsDayListCommand() throws Exception {
        String userInput = "Monday";
        DayOfWeek monday = DayOfWeek.MONDAY;
        ListCommand expectedCommand = new ListCommand(
                p -> p instanceof Student
                        && ((Student) p).getSchedule() != null
                        && monday.equals(((Student) p).getSchedule().getDayOfWeek()),
                "Listed students with schedule on Monday.");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_dateArgs_returnsDateListCommand() throws Exception {
        String userInput = "12-12-2025";
        LocalDate date = LocalDate.of(2025, 12, 12);
        String formatted = date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        ListCommand expectedCommand = new ListCommand(
                p -> p instanceof Student
                        && ((Student) p).getSchedule() != null
                        && date.equals(((Student) p).getSchedule().getDate()),
                "Listed students with schedule on " + formatted + ".");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        String userInput = "nonsenseDay";
        String expectedMessage = "Invalid list argument: \"nonsenseDay\"\n"
                + "Use:\n"
                + "  list          (show all)\n"
                + "  list paid     (filter paid)\n"
                + "  list unpaid   (filter unpaid)\n"
                + "  list schedule (filter schedules)\n"
                + "  list <DAY>    (e.g., list Monday)\n"
                + "  list <DATE>   (e.g., list 12-12-2025)\n"
                + "For name search, use: find n/<keywords>";
        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_extraWhitespace_success() throws Exception {
        String userInput = "   paid   ";
        ListCommand expectedCommand = new ListCommand(
                p -> p.getPaymentStatus() != null && p.getPaymentStatus().isPaid(),
                "Listed persons with payment status: PAID");
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_mixedCase_success() throws Exception {
        String userInput = "  PaId  ";
        ListCommand expectedCommand = new ListCommand(
                p -> p.getPaymentStatus() != null && p.getPaymentStatus().isPaid(),
                "Listed persons with payment status: PAID");
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
