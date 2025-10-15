package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
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
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_TYPE = "s";
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_NOTE = "";
    public static final String DEFAULT_SCHEDULE = "";

    private Type type;
    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private Note note;
    private seedu.address.model.person.Cost cost;
    private PaymentStatus paymentStatus;
    private Schedule schedule;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        type = new Type(DEFAULT_TYPE);
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        note = new Note(DEFAULT_NOTE);
        tags = new HashSet<>();
        cost = null;
        paymentStatus = new PaymentStatus(false);
        schedule = new Schedule(DEFAULT_SCHEDULE);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        type = personToCopy.getType();
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        note = personToCopy.getNote();
        cost = personToCopy.getCost();
        paymentStatus = personToCopy.getPaymentStatus();
        tags = new HashSet<>(personToCopy.getTags());
        if (personToCopy instanceof Student) {
            schedule = ((Student) personToCopy).getSchedule();
        } else {
            schedule = new Schedule(DEFAULT_SCHEDULE);
        }
    }

    /**
     * Sets the {@code Type} of the {@code Person} that we are building.
     */
    public PersonBuilder withType(String type) {
        this.type = new Type(type);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }
    /**
     * Sets the {@code Schedule} of the {@code Person} that we are building.
     */
    public PersonBuilder withSchedule(String schedule) {
        this.schedule = new Schedule(schedule);
        return this;
    }

    /**
     * Builds and returns the {@code Person} with the accumulated values.
     */
    public Person build() {
        if (type.isStudent()) {
            return new Student(name, phone, email, address, note, schedule, cost, paymentStatus, tags);
        }
        return new Parent(name, phone, email, address, note, cost, paymentStatus, tags);
    }

    /**
     * Sets the {@code Note} of the {@code Person} that we are building.
     */
    public PersonBuilder withNote(String note) {
        this.note = new Note(note);
        return this;
    }

    /**
     * Sets the {@code Cost} of the {@code Person} that we are building.
     */
    public PersonBuilder withCost(String cost) {
        this.cost = cost != null ? new seedu.address.model.person.Cost(cost) : null;
        return this;
    }

    /**
     * Sets the {@code PaymentStatus} of the {@code Person} that we are building.
     */
    public PersonBuilder withPaymentStatus(boolean isPaid) {
        this.paymentStatus = new PaymentStatus(isPaid);
        return this;
    }
}
