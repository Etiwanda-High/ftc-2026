package moe.seikimo.ftc.exceptions;

/**
 * Exception thrown when no Limelight result is found.
 */
public final class NoResultException extends LimelightException {
    public NoResultException() {
        super("No Limelight result was found.");
    }
}
