package duke.component;

import duke.task.Task;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Represents lists of tasks.
 */
public class TaskList extends ArrayList<Task> {
    /**
     * Creates a list of tasks.
     */
    public TaskList() {
        super();
    }

    /**
     * Prints a filtered list based on this list with the given predicate.
     * @param predicate the condition for a task to be printed
     * @return the number of tasks printed
     */
    public int print(Predicate<Task> predicate) {
        int n = size();
        int count = 0;
        for (int i = 0; i < n; i++) {
            Task task = get(i);
            if (predicate.test(task)) {
                System.out.println("\t  " + (i + 1) + "." + task);
                count++;
            }
        }
        return count;
    }
}
