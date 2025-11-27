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
}
