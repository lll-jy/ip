package duke.command;

import duke.InvalidCommandException;
import duke.TaskList;
import duke.component.Storage;
import duke.component.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HappensCommand extends Command {
    public HappensCommand(String input) {
        super(input);
    }

    @Override
    public void execute(Ui ui, TaskList list, Storage storage) throws InvalidCommandException {
        try {
            LocalDate date = LocalDate.parse(input.substring(11),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ui.printList(list, t -> t.happenOnDate(date), "happening on " +
                    date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + " ");
        } catch (Exception e) {
            throw new InvalidCommandException("Invalid date format. Please use yyyy-MM-dd");
        }
    }
}
