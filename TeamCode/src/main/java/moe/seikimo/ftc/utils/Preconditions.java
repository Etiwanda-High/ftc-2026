package moe.seikimo.ftc.utils;

public interface Preconditions {
    /**
     * "I do assert that..."
     *
     * @param condition "this condition is true"
     * @param message "and here is why"
     */
    static void doAssert(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError("Assertion failed: " + message);
        }
    }

    /**
     * "Argument cannot be null: {message}"
     *
     * @param object The object to check for nullity.
     * @param message The message to include in the exception if the object is null.
     */
    static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException("Argument cannot be null: " + message);
        }
    }
}
