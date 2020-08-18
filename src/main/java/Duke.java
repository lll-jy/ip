import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private static final String horizontalLine = "\t=================================================================================";
    private static int n = -1;
    private static int m = -1;

    private static String output(String message) {
        return horizontalLine + "\n\t  " + message + "\n" + horizontalLine + "\n";
    }

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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(output("Hello! I'm Duke\n\t  What can I do for you?"));
        List<Task> list = new ArrayList<>();
        int count = 0;
        boolean flag = true;
        while (flag) {
            try {
                String input = sc.nextLine();
                Command type;
                if (input.equals("bye")) {
                    type = Command.BYE;
                } else if (input.equals("list")) {
                    type = Command.LIST;
                } else if (isDeleteCommand(input, count)) {
                    type = Command.DELETE;
                } else if (isDoneCommand(input, count)) {
                    type = Command.DONE;
                } else {
                    type = Command.TASK;
                }
                switch (type) {
                    case LIST:
                        System.out.println(horizontalLine + "\n\t  " + "Here are the tasks in your list:");
                        for (int i = 0; i < count; i++) {
                            System.out.println("\t  " + (i + 1) + "." + list.get(i));
                        }
                        System.out.println(horizontalLine + "\n");
                        break;
                    case DONE:
                        list.get(n - 1).markAsDone();
                        System.out.println(output("Nice! I've marked this task as done:\n\t    " + list.get(n - 1)));
                        break;
                    case DELETE:
                        Task toDelete = list.get(m - 1);
                        list.remove(toDelete);
                        System.out.println(output("Noted. I've removed this task:\n\t    " + toDelete +
                                "\n\t  Now you have " + list.size()));
                        count--;
                        break;
                    case TASK:
                        Task task = generate(input);
                        list.add(count++, task);
                        System.out.println(output("Got it. I've added this task:\n\t    " + task +
                                "\n\t  Now you have " + count + " tasks in the list."));
                        break;
                    case BYE:
                        flag = false;
                        break;
                }
            } catch (InvalidCommandException e) {
                System.out.println(output(e.getMessage()));
            }
        }
        System.out.println(output("Bye. Hope to see you again soon!"));
    }
}
