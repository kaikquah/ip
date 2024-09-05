package gale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void taskCommand_wrongCommandFormat_exceptionThrown() {
        try {
            Parser.parseTask("hello");
            fail();
        } catch (GaleException e) {
            assertEquals("Whoosh! The wind blew away your command. Please use 'todo', 'deadline' or 'event'.",
                    e.getMessage());
        }
    }

    @Test
    public void todoCommand_missingDescription_exceptionThrown() {
        try {
            Parser.parseToDo("todo");
            fail();
        } catch (GaleException e) {
            assertEquals("Oops! The wind blew away your to-do description. Please use: 'todo [description]'.",
                    e.getMessage());
        }
    }

    @Test
    public void parseDateTime_invalidFormat_exceptionThrown() {
        String invalidDate = "2023-12/31";
        assertThrows(DateTimeParseException.class, () -> Parser.parseDateTime(invalidDate));
    }

}
