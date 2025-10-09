package main.java.seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's cost per lesson in the address book.
 * Guarantees: immutable; is always valid
 */
public class Cost {

    public static final String MESSAGE_CONSTRAINTS =
            "Cost per lesson should be a numeric value. E.g pay/72.5";
    public static final String VALIDATION_REGEX = "^\\d+(\\.\\d+)?$";

    public final String value;

    /**
     * Constructs a Cost with the specified value.
     *
     * @param cost the cost per lesson; must not be null and must be a valid numeric value
     */
    public Cost(String cost) {
        requireNonNull(cost);
        checkArgument(isValidCost(cost), MESSAGE_CONSTRAINTS);
        value = cost;
    }

    /**
     * Returns true if a given string is a valid cost.
     */
    public static boolean isValidCost(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return "$" + value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Cost // instanceof handles nulls
                && value.equals(((Cost) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
