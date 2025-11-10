package moe.seikimo.ftc.utils;

public interface Logger {
    /**
     * Pushes telemetry updates to all systems.
     */
    void push();

    /**
     * Adds a section header to telemetry.
     *
     * @param title The title of the section.
     * @return The logger instance.
     */
    Logger section(String title);

    /**
     * Adds a line to telemetry.
     *
     * @param message The message to add.
     * @return The logger instance.
     */
    Logger line(String message);

    /**
     * Adds a formatted log entry to telemetry.
     *
     * @param caption The caption for the log entry.
     * @param message The message for the log entry.
     * @param args The arguments to format into the message.
     * @return The logger instance.
     */
    Logger log(String caption, String message, Object... args);
}
