import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class Ui {
    private static final String horizontalLine = "\t=================================================================================";
    private final Scanner sc;

    public Ui() {
        sc = new Scanner(System.in);
    }

    public void print(String str) {
        System.out.println(str);
    }

    private String output(String message) {
        return horizontalLine + "\n\t  " + message + "\n" + horizontalLine + "\n";
    }

    private static void printList(int count, List<Task> list, Predicate<Task> predicate, String note) {
        System.out.println(horizontalLine + "\n\t  " + "Here are the tasks " + note + "in your list:");
        for (int i = 0; i < count; i++) {
            Task task = list.get(i);
            if (predicate.test(task)) {
                System.out.println("\t  " + (i + 1) + "." + list.get(i));
            }
        }
        System.out.println(horizontalLine + "\n");
    }
}
