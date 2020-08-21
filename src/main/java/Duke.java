import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Duke {
    public static void main(String[] args) throws IOException, InvalidCommandException {
        Ui ui = new Ui();
        Storage storage = new Storage("../data/tasks.txt");
        List<Task> list = new ArrayList<>();
        int count = 0;
        boolean flag = true;
        while (flag) {
            try {
                String input = ui.readInput();
                Input type = Parser.getType(input);
                flag = Parser.dealCommand(type, ui, storage, input, count, list);
            } catch (InvalidCommandException e) {
                ui.output(e.getMessage());
            }
        }
        ui.output("Bye. Hope to see you again soon!");
    }
}
