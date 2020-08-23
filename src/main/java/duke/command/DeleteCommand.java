package duke.command;

import duke.task.Task;
import duke.component.*;

public class DeleteCommand extends Command {
    public DeleteCommand(String input) {
        super(input);
    }

    @Override
    public void execute(Ui ui, TaskList list, Storage storage) throws InvalidCommandException {
        int count = list.size();
        int m = Parser.isValidDelete(input, count) - 1;
        Task toDelete = list.get(m);
        list.remove(toDelete);
        storage.reWrite(list);
        ui.output("Noted. I've removed this task:\n\t    " + toDelete +
                "\n\t  Now you have " + list.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof DeleteCommand) {
            return input.equals(((DeleteCommand) obj).input);
        } else {
            return false;
        }
    }
}
