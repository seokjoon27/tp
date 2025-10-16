package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ScheduleTest {

    @Test
    public void constructor_validWeeklySchedule_success() {
        String weekly = "Monday 14:00-16:00";
        Schedule schedule = new Schedule(weekly);
        assertEquals(weekly, schedule.value);
        assertEquals("Monday 14:00-16:00", schedule.toString());
    }

    @Test
    public void constructor_validOneOffSchedule_success() {
        String oneOff = "12-10-2025 14:00-16:00";
        Schedule schedule = new Schedule(oneOff);
        assertEquals(oneOff, schedule.value);
        assertEquals("12-10-2025 14:00-16:00", schedule.toString());
    }

    @Test
    public void constructor_emptySchedule_success() {
        Schedule schedule = new Schedule("");
        assertEquals("", schedule.value);
        assertEquals("No schedule set", schedule.toString());
    }

    @Test
    public void constructor_invalidFormat_throwsIllegalArgumentException() {
        // Missing time
        assertThrows(IllegalArgumentException.class, () -> new Schedule("Monday"));
        // Invalid day
        assertThrows(IllegalArgumentException.class, () -> new Schedule("Funday 14:00-16:00"));
        // Invalid date format
        assertThrows(IllegalArgumentException.class, () -> new Schedule("2025-12-10 14:00-16:00"));
        // Completely invalid
        assertThrows(IllegalArgumentException.class, () -> new Schedule("random text"));
    }

    @Test
    public void equals_sameValue_returnsTrue() {
        Schedule schedule1 = new Schedule("Monday 14:00-16:00");
        Schedule schedule2 = new Schedule("Monday 14:00-16:00");
        assertEquals(schedule1, schedule2);
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Schedule schedule1 = new Schedule("Monday 14:00-16:00");
        Schedule schedule2 = new Schedule("Tuesday 16:00-18:00");
        assertThrows(AssertionError.class, () -> assertEquals(schedule1, schedule2));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        Schedule schedule1 = new Schedule("Monday 14:00-16:00");
        Schedule schedule2 = new Schedule("Monday 14:00-16:00");
        assertEquals(schedule1.hashCode(), schedule2.hashCode());
    }
}
