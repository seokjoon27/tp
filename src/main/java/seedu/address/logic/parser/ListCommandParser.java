package seedu.address.logic.parser;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;

/**
 * Parses input arguments and creates a new {@link ListCommand}.
 * Supported forms:
 *   {@code list} — show all persons
 *   {@code list paid} — show only persons with paid status
 *   {@code list unpaid} — show only persons with unpaid status
 * Any other argument is rejected with a {@link ParseException}.
 */
public class ListCommandParser implements Parser<ListCommand> {

    @Override
    public ListCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            return new ListCommand(Model.PREDICATE_SHOW_ALL_PERSONS, "Listed all persons");
        }

        String lowered = trimmed.toLowerCase(java.util.Locale.ROOT);
        switch (lowered) {
        case "paid":
            return new ListCommand(
                    p -> p.getPaymentStatus() != null && p.getPaymentStatus().isPaid(),
                    "Listed persons with payment status: PAID"
            );
        case "unpaid":
            return new ListCommand(
                    p -> p.getPaymentStatus() != null && !p.getPaymentStatus().isPaid(),
                    "Listed persons with payment status: UNPAID"
            );
        default:
            // list should NOT do name search—point users to find
            throw new ParseException(
                    "Invalid list argument: \"" + trimmed + "\"\n"
                            + "Use:\n"
                            + "  list          (show all)\n"
                            + "  list paid     (filter paid)\n"
                            + "  list unpaid   (filter unpaid)\n"
                            + "For name search, use: find n/<keywords>"
            );
        }
    }
}
