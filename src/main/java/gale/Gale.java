package gale;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class Gale {
    private Storage storage;
    private TaskList taskList;
    private Ui ui;


    public Gale() {
        this.ui = new Ui();
        this.storage = new Storage("src/main/java/data/galeTasks.txt");
        try {
            this.taskList = new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            this.taskList = new TaskList();
        }
    }

    public Gale(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        try {
            this.taskList = new TaskList(storage.loadTasks());
        } catch (IOException e) {
            ui.showLoadingError();
            this.taskList = new TaskList();
        }
    }

    public static void main(String[] args) {
        Gale gale = new Gale();
        gale.run();
    }

    protected void saveTasks() {
        try {
            storage.saveTasks(taskList.getTaskList());
        } catch (IOException e) {
            ui.showException("Oops! The wind interfered with saving your tasks. Please try again.");
        }
    }

    public void run() {
        ui.greet();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input;
            try {
                input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("bye")) {
                    ui.exit();
                    break;
                } else if (input.equalsIgnoreCase("list")) {
                    ui.displayTaskList(taskList);
                } else if (input.startsWith("mark") || input.startsWith("unmark")) {
                    handleTaskMarking(input);
                } else if (input.startsWith("delete")) {
                    deleteTask(input);
                } else if (input.startsWith("find")) {
                    findTasks(input);
                } else {
                    Task task = Parser.parseTask(input);
                    taskList.addTask(task);
                    ui.printAddedTask(task, taskList.size());
                }
                saveTasks();
            } catch (GaleException e) {
                ui.showException(e.getMessage());
            }
        }
        scanner.close();
    }

    public void deleteTask(String input) throws GaleException {
        String[] strA = input.split(" ");
        if (strA.length != 2) {
            throw new GaleException("Your task number got lost in the wind. Please use 'delete [task number]'");
        }
        int index = Integer.parseInt(strA[1]) - 1;
        Task task = taskList.getTask(index);
        taskList.deleteTask(index);
        ui.showDeletedTask(task, taskList.size());
    }

    public void handleTaskMarking(String input) throws GaleException {
        String[] strA = input.split(" ");
        int index = Integer.parseInt(strA[1]) - 1;
        boolean isDone = strA[0].equals("mark");
        if (index >= 0 && index < taskList.size()) {
            Task task = taskList.getTask(index);
            if (task.status() == isDone) {
                throw new GaleException("Oops! This task is already marked as " + (isDone ? "done." : "not done."));
            } else {
                taskList.markTask(index, isDone);
                ui.showMarkedTask(task, isDone);
            }
        } else {
            throw new GaleException("Oops! That task number is lost in the wind. Try again?");
        }
    }

    /**
     * Finds tasks that contain the keyword in their description.
     * @param input the user input in the form of 'find (keyword)'
     * @throws GaleException if the keyword is missing
     */
    public void findTasks(String input) throws GaleException {
        String[] strA = input.split(" ", 2);
        if (strA.length < 2 || strA[1].trim().isEmpty()) {
            throw new GaleException("Oops! Your keyword is lost in the wind. Please use 'find [keyword]'.");
        }
        String keyword = strA[1].trim();
        ArrayList<Task> foundTasks = taskList.findTasks(keyword);
        ui.showFoundTasks(foundTasks, keyword);
    }
}
