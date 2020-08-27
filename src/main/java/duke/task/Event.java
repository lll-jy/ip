package duke.task;

import duke.command.InvalidCommandException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that consists of a description and a datetime as the happening time of the event.
 */
public class Event extends Task {
    private final LocalDateTime atTime;

    /**
     * Creates an event task.
     * @param description the description of the task
     * @param atTime the string description of the time the event happens at
     * @throws InvalidCommandException if the input time format is not yyyy-MM-dd HH:mm
     */
    public Event(String description, String atTime) throws InvalidCommandException {
        super(description);
        try {
            this.atTime = LocalDateTime.parse(atTime,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            throw new InvalidCommandException("Invalid input datetime, please input as yyyy-MM-dd HH:mm.");
        }
    }

    @Override
    public boolean isHappeningOn(LocalDate date) {
        return date.isEqual(atTime.toLocalDate());
    }

    @Override
    public boolean isHappeningToday() {
        return isHappeningOn(LocalDate.now());
    }

    @Override
    public boolean hasHappenedBefore(LocalDate date) {
        return atTime.toLocalDate().isBefore(date);
    }

    @Override
    public boolean hasHappenedBeforeToday() {
        return hasHappenedBefore(LocalDate.now());
    }

    @Override
    public boolean isHappeningAfter(LocalDate date) {
        return atTime.toLocalDate().isAfter(date);
    }

    @Override
    public boolean isHappeningAfterToday() {
        return isHappeningAfter(LocalDate.now());
    }

    @Override
    public boolean isHappeningBetween(LocalDate date1, LocalDate date2) {
        LocalDate date = atTime.toLocalDate();
        return !date.isAfter(date2) && !date.isBefore(date1);
    }

    @Override
    public boolean willHappenInDays(int n) {
        return isHappeningBetween(LocalDate.now(), LocalDate.now().plusDays(n));
    }

    @Override
    public String output() {
        return "E" + super.output() + " | At: " +
                atTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n";
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (at: " +
                atTime.format(DateTimeFormatter.ofPattern("hh:mm a   MMM d yyyy")) + ")";
    }

    /**
     * Checks whether the given object equals this Event task.
     * @param obj the given object to compare
     * @return true if the object is an Event and both the description and atTime equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Event) {
            Event o = (Event) obj;
            return description.equals(o.description) && atTime.isEqual(o.atTime);
        } else {
            return false;
        }
    }
}
