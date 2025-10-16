package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;
import seedu.address.model.person.Student;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label type;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label note;
    @FXML
    private Label schedule;
    @FXML
    private Label cost;
    @FXML
    private CheckBox paidStatus;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(formatField("Name", person.getName().fullName));
        type.setText(formatField("Type", person.getType().isStudent() ? "Student" : "Parent"));
        phone.setText(formatField("Phone", person.getPhone().value));
        address.setText(formatField("Address", person.getAddress().value));
        email.setText(formatField("Email", person.getEmail().value));
        note.setText(formatField("Note", person.getNote().value));

        cost.setText(person.getCost() != null ? formatField("Cost", person.getCost().toString())
                : formatField("Cost", ""));
        paidStatus.setText("[Paid]");
        paidStatus.setSelected(person.getPaymentStatus().isPaid());
        tags.getChildren().clear();
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        if (person instanceof Student) {
            schedule.setText(formatField("Schedule", ((Student) person).getSchedule().value));
            schedule.setVisible(true);
        } else {
            schedule.setText("");
            schedule.setVisible(false);
        }
    }

    private String formatField(String label, String value) {
        return "[" + label + "] " + (value == null ? "" : value);
    }
}
