package seedu.address.model.person;

/**
 * Represents whether a person has completed payment.
 * This is immutable.
 */
public class PaymentStatus {

    private final boolean isPaid;

    /**
     * Constructs a {@code PaymentStatus}.
     */
    public PaymentStatus(boolean isPaid) {
        this.isPaid = isPaid;
    }

    /**
     * Returns true if payment has been made.
     */
    public boolean isPaid() {
        return isPaid;
    }

    @Override
    public String toString() {
        return Boolean.toString(isPaid);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PaymentStatus
                && isPaid == ((PaymentStatus) other).isPaid);
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(isPaid);
    }
}
