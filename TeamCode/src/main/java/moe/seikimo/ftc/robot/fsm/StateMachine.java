package moe.seikimo.ftc.robot.fsm;

import lombok.Setter;
import moe.seikimo.ftc.game.MonoBehaviour;

/**
 * The state of a robot.
 * <p>
 * Implements {@link MonoBehaviour} for lifecycle management.
 */
public abstract class StateMachine implements MonoBehaviour {
    /**
     * The return code from the previous state.
     */
    @Setter
    protected int previous = 0;

    /**
     * By default, state machines are endless and require manual transition.
     * <p>
     * The state manager will transition back to the last state if this is true.
     * <p>
     * States can change which state is transitioned to by overriding {@link StateMachine#nextState()}
     *
     * @return True if the state is finished and should transition.
     */
    public boolean isFinished() {
        return false;
    }

    /**
     * The next state to transition to when this state is finished.
     *
     * @return The next state enum, or null to return to the previous state.
     */
    public State nextState() {
        return null;
    }

    /**
     * The return code of the state.
     * This value is passed to the next state.
     *
     * @return The return code.
     */
    public int getReturnCode() {
        return 0;
    }

    // region Overrides for MonoBehavior

    @Override
    public final void awake() {
        this.start();
    }

    @Override
    public final void preUpdate() {
        this.update();
    }

    // endregion
}
