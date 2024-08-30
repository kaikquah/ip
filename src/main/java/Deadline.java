public class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by + ")";
    }

    @Override
    public String toFileString() {
        return String.format("D | %d | %s | %s", super.status() ? 1 : 0, getDescription(), by);
    }
}
