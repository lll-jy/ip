package duke;

/**
 * The Main class of this program.
 * @author Li Jiayu
 * @version 0.1
 */

import duke.command.Command;
import duke.command.InvalidCommandException;
import duke.component.*;

public class Duke {
    private Storage storage;
    private TaskList list;
    private Ui ui;

    /**
     * Creates a running Duke, initialize the list with data in the input file, if input file is not found,
     * initialize the list with an empty list.
     * @param filePath The file path of the data file holding all existing tasks.
     */
    public Duke(String filePath) {
        ui = new Ui();
        try {
            storage = new ActualStorage(filePath);
            list = storage.getList();
        } catch (Exception e) {
            ui.output(e.getMessage());
            list = new TaskList();
        }
    }

    /**
     * Runs the Duke program.
     */
    public void run() {
        boolean flag = true;
        ui.greeting();
        while (flag) {
            try {
                String input = ui.readInput();
                Command c = Parser.parse(input);
                c.execute(ui, list, storage);
                flag = !c.isExit();
            } catch (InvalidCommandException e) {
                ui.output(e.getMessage());
            }
        }
        ui.close();
    }

    public static void main(String[] args) {
        String path = "/Users/lijiayu/Desktop/cs2103/ip/data/tasks.txt";
        new Duke(path).run();
    }
}
