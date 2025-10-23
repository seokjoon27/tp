package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TYPE;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Cost;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Parent;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Schedule;
import seedu.address.model.person.Student;
import seedu.address.model.person.Type;
import seedu.address.model.tag.Tag;


/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    private static final Map<String, String> REQUIRED_PREFIX_DISPLAY_NAMES = createRequiredPrefixDisplayNames();
    private static final List<Prefix> REQUIRED_PREFIXES_ORDER = List.of(
            PREFIX_TYPE, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TYPE, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_TAG, PREFIX_NOTE, PREFIX_SCHEDULE, PREFIX_PAY);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        List<Prefix> missingRequiredPrefixes = REQUIRED_PREFIXES_ORDER.stream()
                .filter(prefix -> argMultimap.getValue(prefix).isEmpty())
                .collect(Collectors.toList());

        if (!missingRequiredPrefixes.isEmpty()) {
            String missingFields = missingRequiredPrefixes.stream()
                    .map(prefix -> prefix + REQUIRED_PREFIX_DISPLAY_NAMES.get(prefix.getPrefix()))
                    .collect(Collectors.joining(", "));
            throw new ParseException(String.format(Messages.MESSAGE_MISSING_REQUIRED_FIELDS,
                    missingFields, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TYPE, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_PAY);
        Type type = ParserUtil.parseType(argMultimap.getValue(PREFIX_TYPE).get());
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Note note;
        if (argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
        } else {
            note = new Note("");
        }
        Schedule schedule = null;
        if (type.isStudent()) {
            schedule = argMultimap.getValue(PREFIX_SCHEDULE).isPresent()
                    ? ParserUtil.parseSchedule(argMultimap.getValue(PREFIX_SCHEDULE).get())
                    : new Schedule("");
        } else if (type.isParent() && argMultimap.getValue(PREFIX_SCHEDULE).isPresent()) {
            throw new ParseException("Parents cannot have a schedule.");
        }
        Cost cost = argMultimap.getValue(PREFIX_PAY).isPresent()
                ? ParserUtil.parseCost(argMultimap.getValue(PREFIX_PAY).get())
                : null;
        PaymentStatus paymentStatus = new PaymentStatus(false);
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = type.isStudent()
                ? new Student(name, phone, email, address, note, schedule, cost, paymentStatus, tagList)
                : new Parent(name, phone, email, address, note, cost, paymentStatus, tagList);

        return new AddCommand(person);
    }

    private static Map<String, String> createRequiredPrefixDisplayNames() {
        Map<String, String> displayNames = new LinkedHashMap<>();
        displayNames.put(PREFIX_TYPE.getPrefix(), "TYPE");
        displayNames.put(PREFIX_NAME.getPrefix(), "NAME");
        displayNames.put(PREFIX_PHONE.getPrefix(), "PHONE");
        displayNames.put(PREFIX_EMAIL.getPrefix(), "EMAIL");
        displayNames.put(PREFIX_ADDRESS.getPrefix(), "ADDRESS");
        return Collections.unmodifiableMap(displayNames);
    }
}
