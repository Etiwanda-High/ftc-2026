package moe.seikimo.ftc.robot.fsm;

/**
 * The various states a robot can be in.
 * <p>
 * States generally should not be overridden. A state is
 * a long-running task that is designed only to be interrupted by
 * something of higher priority.
 * <p>
 * Example: `INTAKING` is not a state. `MOVE_TO_POINT` is a state.
 */
public enum State {
    /**
     * Reverts the robot to the previous state.
     * <p>
     * If no state is available, the robot will enter an idle state.
     */
    PREVIOUS,
    IDLE
}
