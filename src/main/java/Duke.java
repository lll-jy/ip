import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Duke {
    private static int n = -1;
    private static int m = -1;

    private static boolean isDoneCommand(String cmd, int count) throws InvalidCommandException {
        if (cmd.startsWith("done ")) {
            if (cmd.length() < 6) {
                throw new InvalidCommandException("\u2639 OOPS!!! The task to mark as done cannot be empty.");
            }
            try {
                n = Integer.parseInt(cmd.substring(5));
                if (n < 1) {
                    throw new InvalidCommandException("\u2639 OOPS!!! The task index should be a positive integer.");
                } else if (n > count) {
                    throw new InvalidCommandException("\u2639 OOPS!!! The task index does not exist.");
                }
                return true;
            } catch (NumberFormatException e) {
                throw new InvalidCommandException("\u2639 OOPS!!! The task index should be a number.");
            }
        } else {
            return false;
        }
    }

    private static boolean isDeleteCommand(String cmd, int count) throws InvalidCommandException {
        if (cmd.startsWith("delete ")) {
            if (cmd.length() < 8) {
                throw new InvalidCommandException("\u2639 OOPS!!! The task to mark to delete cannot be empty.");
            }
            try {
                m = Integer.parseInt(cmd.substring(7));
                if (m < 1) {
                    throw new InvalidCommandException("\u2639 OOPS!!! The task index should be a positive integer.");
                } else if (m > count) {
                    throw new InvalidCommandException("\u2639 OOPS!!! The task index does not exist.");
                }
                return true;
            } catch (NumberFormatException e) {
                throw new InvalidCommandException("\u2639 OOPS!!! The task index should be a number.");
            }
        } else {
            return false;
        }
    }

    private static Task generate(String cmd) throws InvalidCommandException {
        if (cmd.startsWith("todo")) {
            if (cmd.length() < 5) {
                throw new InvalidCommandException("\u2639 OOPS!!! The description of a todo cannot be empty.");
            } else if (cmd.charAt(4) != ' ') {
                throw new InvalidCommandException("Do you mean 'todo " + cmd.substring(4) + "'");
            } else if (cmd.length() < 6) {
                throw new InvalidCommandException("\u2639 OOPS!!! The description of a todo cannot be empty.");
            }
            return new ToDo(cmd.substring(5));
        } else if (cmd.startsWith("deadline")) {
            if (cmd.length() < 9) {
                throw new InvalidCommandException("\u2639 OOPS!!! The description of a deadline cannot be empty.");
            } else if (cmd.charAt(8) != ' ') {
                throw new InvalidCommandException("Do you mean 'deadline " + cmd.substring(8) + "'");
            } else if (cmd.length() < 10) {
                throw new InvalidCommandException("\u2639 OOPS!!! The description of a deadline cannot be empty.");
            }
            String description = cmd.substring(9);
            int s = description.indexOf("/by");
            if (s == -1) {
                throw new InvalidCommandException("\u2639 OOPS!!! Time should be specified");
            }
            if (description.length() - s < 4) {
                throw new InvalidCommandException("The time specification cannot be empty.");

            }
            String time = description.substring(s + 4);
            description = description.substring(0, s - 1);
            return new Deadline(description, time);
        } else if (cmd.startsWith("event")) {
            if (cmd.length() < 6) {
                throw new InvalidCommandException("\u2639 OOPS!!! The description of a event cannot be empty.");
            } else if (cmd.charAt(5) != ' ') {
                throw new InvalidCommandException("Do you mean 'event " + cmd.substring(5) + "'");
            } else if (cmd.length() < 7) {
                throw new InvalidCommandException("\u2639 OOPS!!! The description of a event cannot be empty.");
            }
            String description = cmd.substring(6);
            int s = description.indexOf("/at");
            if (s == -1) {
                throw new InvalidCommandException("\u2639 OOPS!!! Time should be specified");
            }
            if (description.length() - s < 4) {
                throw new InvalidCommandException("The time specification cannot be empty.");

            }
            String time = description.substring(s + 4);
            description = description.substring(0, s - 1);
            return new Event(description, time);
        } else {
            throw new InvalidCommandException();
        }
    }

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
                switch (type) {
                    case LIST:
                        ui.printList(count, list, t -> true, "");
                        break;
                    case DONE:
                        list.get(n - 1).markAsDone();
                        ui.output("Nice! I've marked this task as done:\n\t    " + list.get(n - 1));
                        break;
                    case DELETE:
                        Task toDelete = list.get(m - 1);
                        list.remove(toDelete);
                        storage.deleteTask(list);
                        ui.output("Noted. I've removed this task:\n\t    " + toDelete +
                                "\n\t  Now you have " + list.size());
                        count--;
                        break;
                    case HAPPENS:
                        try {
                            LocalDate date = LocalDate.parse(input.substring(11),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            ui.printList(count, list, t -> t.happenOnDate(date), "happening on " +
                                    date.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + " ");
                        } catch (Exception e) {
                            throw new InvalidCommandException("Invalid date format. Please use yyyy-MM-dd");
                        }
                        break;
                    case TASK:
                        Task task = generate(input);
                        storage.addToList(task);
                        list.add(count++, task);
                        String temp = count <= 1 ? " task" : " tasks";
                        ui.output("Got it. I've added this task:\n\t    " + task +
                                "\n\t  Now you have " + count + temp + " in the list.");
                        break;
                    case BYE:
                        flag = false;
                        break;
                }
            } catch (InvalidCommandException e) {
                ui.output(e.getMessage());
            }
        }
        ui.output("Bye. Hope to see you again soon!");
    }
}
