package moe.seikimo.ftc.exceptions;

/**
 * Exception thrown when no AprilTag is detected by the Limelight.
 */
public final class NoTagDetectedException extends LimelightException {
    public NoTagDetectedException() {
        super("No AprilTag was detected.");
    }
}
