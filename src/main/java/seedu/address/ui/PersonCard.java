package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Parent;
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
    private FlowPane parents;
    @FXML
    private VBox parentsContainer;


    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        type.setText(person.getType().value.equals("s") ? "Student" : "Parent");
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        note.setText(person.getNote().value);

        cost.setText(person.getCost() != null ? person.getCost().toString() : "");
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        if (person instanceof Student student) {
    // ----- Parents list -----
    parents.getChildren().clear();
    if (student.getParents() != null) {
        for (Parent parent : student.getParents()) {
            Label parentLabel = new Label(parent.getName().fullName);
            parentLabel.getStyleClass().add("cell_small_label");
            parents.getChildren().add(parentLabel);
        }
    }
    parentsContainer.setVisible(true);
    parentsContainer.setManaged(true);

    // ----- Schedule -----
    if (student.getSchedule() != null) {
        schedule.setText(student.getSchedule().value);
    } else {
        schedule.setText("");
    }
    schedule.setVisible(true);

} else {
    // Hide parents UI for non-students
    parents.getChildren().clear();
    parentsContainer.setVisible(false);
    parentsContainer.setManaged(false);

    // Hide schedule for non-students
    schedule.setText("");
    schedule.setVisible(false);
}
    }
}
