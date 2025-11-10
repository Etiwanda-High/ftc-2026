package moe.seikimo.ftc.robot.fsm;

import moe.seikimo.ftc.game.MonoBehaviour;

/**
 * The state of a robot.
 * <p>
 * Implements {@link MonoBehaviour} for lifecycle management.
 */
public abstract class State implements MonoBehaviour {
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
