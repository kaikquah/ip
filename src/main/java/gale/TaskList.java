package gale;
import java.util.ArrayList;

/**
 * Represents a list of tasks using an ArrayList.
 *
 * @author kaikquah
 */
public class TaskList {
    private ArrayList<Task> taskList;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Constructs a task list with the input ArrayList of Tasks.
     *
     * @param taskList the ArrayList of Tasks
     */
    public TaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Adds a task to the task list.
     *
     * @param task the task to be added
     */
    public void addTask(Task task) {
        taskList.add(task);
    }

    /**
     * Deletes a task from the task list.
     *
     * @param index the index of the task to be deleted, starting from 0
     */
    public void deleteTask(int index) {
        taskList.remove(index);
    }

    /**
     * Retrieves a task from the task list.
     *
     * @param index the index of the task to be retrieved, starting from 0
     * @return the task at the specified index
     */
    public Task getTask(int index) {
        return taskList.get(index);
    }

    /**
     * Returns the number of tasks in the task list.
     * @return
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Returns whether the task list is empty as a boolean.
     * @return
     */
    public boolean isEmpty() {
        return taskList.isEmpty();
    }

    /**
     * Returns the task list as an ArrayList.
     * @return
     */
    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    /**
     * Marks a task as done or not done.
     *
     * @param index the index of the task to be marked, starting from 0
     * @param isDone whether the task is to be marked as done
     */
    public void markTask(int index, boolean isDone) {
        Task task = taskList.get(index);
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }

    /**
     * Finds tasks that contain the keyword in their description.
     * @param keyword the keyword the user is searching for
     * @return an ArrayList of tasks that contain the keyword
     */
    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> foundTasks = new ArrayList<>();
        for (Task task : this.taskList) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                foundTasks.add(task);
            }
        }
        return foundTasks;
    }
}
