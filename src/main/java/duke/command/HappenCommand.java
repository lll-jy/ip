package duke.command;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import duke.component.Storage;
import duke.component.TaskList;
import duke.component.CliUi;
import duke.task.Task;

/**
 * Represents a command for finding tasks with the given time constraint.
 */
public class HappenCommand extends Command {
    /**
     * Creates a command for filtering tasks on when it happens.
     * @param input the input command classified as HappenCommand, starting with "happen "
     */
    public HappenCommand(String input) {
        super(input);
    }

    /**
     * Executes the command, prints the filtered result on ui.
     * @param ui the user interface object that is currently running
     * @param list the current list of tasks
     * @param storage the storage-writing object that is currently running
     * @return the string of the input time constrain with a number representing the size of the resulting task list
     * @throws InvalidCommandException if the input cannot be processed correctly or does not make sense
     */
    @Override
    public String execute(CliUi ui, TaskList list, Storage storage) throws InvalidCommandException {
        String description = input.substring(7);
        String[] detail = description.split(" ");
        DateTimeFormatter parse = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter output = DateTimeFormatter.ofPattern("MMM d yyyy");
        try {
            if (detail[0].equals("on") && detail.length == 2) {
                if (detail[1].equals("today")) {
                    return ui.printList(list, Task::isHappeningToday, "happening today ");
                } else {
                    LocalDate date = LocalDate.parse(detail[1], parse);
                    return ui.printList(list, t -> t.isHappeningOn(date), "happening on " + date.format(output) + " ");
                }
            } else if (detail[0].equals("before") && detail.length == 2) {
                if (detail[1].equals("today")) {
                    return ui.printList(list, Task::hasHappenedBeforeToday, "happening before today ");
                } else {
                    LocalDate date = LocalDate.parse(detail[1], parse);
                    return ui.printList(list, t -> t.hasHappenedBefore(date),
                            "happening before " + date.format(output) + " ");
                }
            } else if (detail[0].equals("after") && detail.length == 2) {
                if (detail[1].equals("today")) {
                    return ui.printList(list, Task::isHappeningAfterToday, "happening after today ");
                } else {
                    LocalDate date = LocalDate.parse(detail[1], parse);
                    return ui.printList(list, t -> t.isHappeningAfter(date),
                            "happening after " + date.format(output) + " ");
                }
            } else if (detail.length == 3 && detail[0].equals("in") && detail[2].equals("days")) {
                int n = Integer.parseInt(detail[1]);
                if (n <= 0) {
                    throw new InvalidCommandException("Please input a positive integer for happen in command.");
                }
                return ui.printList(list, t -> t.willHappenInDays(n), "happening in " + n + " days ");
            } else if (detail[0].equals("between") && detail.length == 3) {
                LocalDate date1 = LocalDate.parse(detail[1], parse);
                LocalDate date2 = LocalDate.parse(detail[2], parse);
                if (!date1.isBefore(date2)) {
                    throw new InvalidCommandException("Latter date is before former date for happen between.");
                }
                return ui.printList(list, t -> t.isHappeningBetween(date1, date2),
                        "happening between " + date1.format(output) + " and " + date2.format(output) + " ");
            } else {
                throw new InvalidCommandException("Invalid happen command input.");
            }
        } catch (InvalidCommandException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidCommandException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    /**
     * Checks whether a command equals this one.
     * @param obj the Object to compare
     * @return true if obj is a HappenCommand and it has the same input as this one
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof HappenCommand) {
            return input.equals(((HappenCommand) obj).input);
        } else {
            return false;
        }
    }
}
