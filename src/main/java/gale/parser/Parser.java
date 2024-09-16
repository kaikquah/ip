package gale.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import gale.exception.GaleException;
import gale.task.Deadline;
import gale.task.Event;
import gale.task.Priority;
import gale.task.Task;
import gale.task.ToDo;

/**
 * Represents a parser that parses user input into tasks.
 *
 * @author kaikquah
 */
public class Parser {

    private static ArrayList<DateTimeFormatter> formatters = new ArrayList<>(
            List.of(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                    DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"))
    );

    /**
     * Returns the list of DateTimeFormatters used by the parser.
     *
     * @return the list of DateTimeFormatters
     */
    public static ArrayList<DateTimeFormatter> getFormatters() {
        return formatters;
    }

    /**
     * Parses the input string into a LocalDateTime object.
     *
     * @param by the input string to be parsed
     * @return the LocalDateTime object parsed from the input string
     * @throws DateTimeParseException if the input string does not match any of the DateTimeFormatters
     */
    public static LocalDateTime parseDateTime(String by) throws DateTimeParseException {
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(by, formatter);
            } catch (DateTimeParseException e) {
                continue;
            }
        }
        throw new DateTimeParseException("Unable to parse date & time", by, 0);
    }

    /**
     * Parses the input string into a Task object.
     * <p>The method calls one of the other methods parseToDo, parseDeadline, or parseEvent
     * based on the task specified in the input.</p>
     *
     * @param input the input string to be parsed
     * @return the Task object parsed from the input string
     * @throws GaleException if the input string does not match any of the task types
     */
    public static Task parseTask(String input) throws GaleException {
        if (input.startsWith("todo")) {
            return parseToDo(input);
        } else if (input.startsWith("deadline")) {
            return parseDeadline(input);
        } else if (input.startsWith("event")) {
            return parseEvent(input);
        } else {
            throw new GaleException("Whoosh! The wind blew away your command. "
                + "Please use 'todo', 'deadline' or 'event'.");
        }
    }

    /**
     * Parses the input string into a ToDo object.
     *
     * @param input the input string to be parsed
     * @return the ToDo object parsed from the input string
     * @throws GaleException if the input string does not match the ToDo format
     */
    public static ToDo parseToDo(String input) throws GaleException {
        String exceptionMsg = "Oops! The wind blew away your to-do description. "
            + "Please use: 'todo [priority] [description]'.";
        if (input.length() <= 5) {
            throw new GaleException(exceptionMsg);
        }
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new GaleException(exceptionMsg);
        }
        if (description.startsWith("high")) {
            description = description.substring(5).trim();
            return new ToDo(description, Priority.HIGH);
        } else if (description.startsWith("medium")) {
            description = description.substring(7).trim();
            return new ToDo(description, Priority.MEDIUM);
        } else if (description.startsWith("low")) {
            description = description.substring(4).trim();
            return new ToDo(description, Priority.LOW);
        } else {
            return new ToDo(description, Priority.NONE);
        }
    }

    /**
     * Parses the input string into a Deadline object.
     *
     * @param input the input string to be parsed
     * @return the Deadline object parsed from the input string
     * @throws GaleException if the input string does not match the Deadline format
     */
    public static Deadline parseDeadline(String input) throws GaleException {
        String exceptionMsg = "Your deadline got tossed by the wind! "
            + "Please use 'deadline [description] /by [date]'.";
        if (input.length() <= 9) {
            throw new GaleException(exceptionMsg);
        }
        String[] strA = input.substring(9).split("/by");
        if (strA.length != 2 || strA[0].trim().isEmpty() || strA[1].trim().isEmpty()) {
            throw new GaleException(exceptionMsg);
        }
        String description = strA[0].trim();
        String by = strA[1].trim();
        Priority priority;
        if (description.startsWith("high")) {
            description = description.substring(5).trim();
            priority = Priority.HIGH;
        } else if (description.startsWith("medium")) {
            description = description.substring(7).trim();
            priority = Priority.MEDIUM;
        } else if (description.startsWith("low")) {
            description = description.substring(4).trim();
            priority = Priority.LOW;
        } else {
            priority = Priority.NONE;
        }
        try {
            return new Deadline(description, by, priority);
        } catch (DateTimeParseException e) {
            throw new GaleException("Oops! The wind blew away your date. "
                + "Please use 'yyyy-MM-dd HH:mm' or 'd/M/yyyy HH:mm'.");
        }
    }

    /**
     * Parses the input string into an Event object.
     *
     * @param input the input string to be parsed
     * @return the Event object parsed from the input string
     * @throws GaleException if the input string does not match any of the task types
     */
    public static Event parseEvent(String input) throws GaleException {
        String exceptionMsg = "Your event is lost in the wind! "
            + "Please use 'event [description] /from [start] /to [end]'.";
        if (input.length() <= 6) {
            throw new GaleException(exceptionMsg);
        }
        String[] strA = input.substring(6).split("/from|/to");
        if (strA.length != 3 || strA[0].trim().isEmpty() || strA[1].trim().isEmpty()
                || strA[2].trim().isEmpty()) {
            throw new GaleException(exceptionMsg);
        }
        String description = strA[0].trim();
        String from = strA[1].trim();
        String to = strA[2].trim();
        Priority priority;
        if (description.startsWith("high")) {
            description = description.substring(5).trim();
            priority = Priority.HIGH;
        } else if (description.startsWith("medium")) {
            description = description.substring(7).trim();
            priority = Priority.MEDIUM;
        } else if (description.startsWith("low")) {
            description = description.substring(4).trim();
            priority = Priority.LOW;
        } else {
            priority = Priority.NONE;
        }
        try {
            return new Event(description, from, to, priority);
        } catch (DateTimeParseException e) {
            throw new GaleException("Oops! The wind blew away your date. "
                + "Please use 'yyyy-MM-dd HH:mm' or 'd/M/yyyy HH:mm'.");
        }
    }

    public static int parseIndex(String input, String command) throws GaleException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new GaleException("Your task number got lost in the wind. "
                + "Please use '" + command + " [task number]'");
        }
        try {
            int index = Integer.parseInt(parts[1]) - 1;
            if (index < 0) {
                throw new GaleException("Oops! That task number is lost in the wind. Try again?");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new GaleException("Swoosh! The wind thinks that's not a number!");
        }
    }

    public static String parseKeyword(String input) throws GaleException {
        String[] parts = input.split(" ", 2);
        String keyword = parts[1].trim();
        if (parts.length < 2 || keyword.isEmpty()) {
            throw new GaleException("The wind blew away your keyword. Please use 'find [keyword]'.");
        }
        return keyword;
    }
}
