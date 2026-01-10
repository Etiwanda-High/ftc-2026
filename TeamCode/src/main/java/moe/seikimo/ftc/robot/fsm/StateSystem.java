package moe.seikimo.ftc.robot.fsm;

import lombok.RequiredArgsConstructor;
import lombok.val;
import moe.seikimo.ftc.annotations.types.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.robot.Robot;
import moe.seikimo.ftc.utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The state manager maintains order with finite state machines while allowing
 * overrides, cancellations, and scheduling.
 */
@RequiredArgsConstructor
@RobotSystem(RobotSystem.Priority.LOW)
public final class StateSystem implements MonoBehaviour {
    private final Logger logger;
    private final Robot robot;

    /**
     * The stack of previous states.
     */
    private final Deque<State> stateStack = new ArrayDeque<>();

    /**
     * The current state type.
     */
    private State stateType = null;

    /**
     * The current state instance.
     * <p>
     * A new state instance is created when transitioning.
     */
    private StateMachine currentState = null;

    /**
     * Sets the new state of the robot.
     *
     * @param stateType The new state type to set.
     * @param newState The new state to set.
     * @param returnCode The return code from the previous state.
     */
    public void changeState(State stateType, @NotNull StateMachine newState, int returnCode) {
        // Push the current state onto the stack.
        if (this.stateType != null) {
            this.stateStack.push(this.stateType);
        }
        this.stateType = stateType;

        // Set the previous return code.
        newState.setPrevious(returnCode);

        // Set the new state instance.
        if (this.currentState != null) {
            this.currentState.destroy();
        }
        this.currentState = newState;
        this.currentState.start();
    }

    /**
     * Transitions to the last state on the stack.
     */
    public void fallbackState() {
        val previousState = this.stateStack.isEmpty() ?
            State.IDLE :
            this.stateStack.pop();
        this.robot.changeState(previousState);
    }

    // region MonoBehavior Implementation

    @Override
    public void awake() {
        // Default to the idle state.
        this.robot.changeState(State.IDLE);
    }

    @Override
    public void update() {
        if (this.currentState == null) {
            this.fallbackState();
            return;
        }

        // Update the current state.
        this.currentState.update();

        // If the current state is finished, transition to the next state.
        if (this.currentState.isFinished()) {
            val returnCode = this.currentState.getReturnCode();
            val nextState = this.currentState.nextState();

            if (nextState == null) {
                // Return to the default idle state.
                this.robot.changeState(State.IDLE, returnCode);
            } else if (nextState == State.PREVIOUS) {
                this.fallbackState(returnCode);
            } else {
                this.robot.changeState(nextState, returnCode);
            }
        }

        // Push telemetry data.
        this.logger
            .section("State System")
            .log("Current State", this.stateType == null ? "None" : this.stateType.name());
    }

    // endregion
}
