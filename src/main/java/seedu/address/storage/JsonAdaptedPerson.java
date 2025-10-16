package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
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
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String type;
    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String note;
    private final String cost;
    private final Boolean paymentStatus;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<String> linkedNames = new ArrayList<>();
    private final String schedule;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("type") String type, @JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email, @JsonProperty("address") String address,
                             @JsonProperty("note") String note, @JsonProperty("cost") String cost,
                             @JsonProperty("paymentStatus") Boolean paymentStatus,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("linkedNames") List<String> linkedNames,
                             @JsonProperty("schedule") String schedule) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.note = note;
        this.cost = cost;
        this.paymentStatus = paymentStatus;
        this.schedule = schedule;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (linkedNames != null) {
            this.linkedNames.addAll(linkedNames);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        type = source.getType().value;
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        note = source.getNote().value;
        cost = source.getCost() != null ? source.getCost().value : null;
        paymentStatus = source.getPaymentStatus().isPaid();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        if (source instanceof Student student) {
            // preserve schedule
            schedule = student.getSchedule() != null ? student.getSchedule().value : null;

            // include linked parent names
            for (Parent parent : student.getParents()) {
                linkedNames.add(parent.getName().fullName);
            }
        } else {
            schedule = null;
        }

    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        if (type == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Type.class.getSimpleName()));
        }
        if (!Type.isValidType(type)) {
            throw new IllegalValueException(Type.MESSAGE_CONSTRAINTS);
        }
        final Type modelType = new Type(type);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        if (note == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Note.class.getSimpleName()));
        }
        final Note modelNote = new Note(note);

        final Cost modelCost;
        if (cost == null || cost.isEmpty()) {
            modelCost = null;
        } else if (!Cost.isValidCost(cost)) {
            throw new IllegalValueException(Cost.MESSAGE_CONSTRAINTS);
        } else {
            modelCost = new Cost(cost);
        }

        if (paymentStatus == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PaymentStatus.class.getSimpleName()));
        }
        final PaymentStatus modelPaymentStatus = new PaymentStatus(paymentStatus);

        final Schedule modelSchedule = (schedule == null) ? new Schedule("") : new Schedule(schedule);


        if (modelType.isStudent()) {
            // Use the constructor that preserves schedule
            Student student = new Student(
                    modelName,
                    modelPhone,
                    modelEmail,
                    modelAddress,
                    modelNote,
                    modelSchedule,
                    modelCost,
                    modelPaymentStatus,
                    modelTags
            );

            student.setLinkedNames(linkedNames == null ? new ArrayList<>() : new ArrayList<>(linkedNames));
            return student;

        } else {
            return new Parent(
                    modelName,
                    modelPhone,
                    modelEmail,
                    modelAddress,
                    modelNote,
                    modelCost,
                    modelPaymentStatus,
                    modelTags
            );
        }
    }
}
