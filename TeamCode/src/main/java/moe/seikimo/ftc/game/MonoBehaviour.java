package moe.seikimo.ftc.game;

/**
 * Unity-like behavior script for FTC.
 */
public interface MonoBehaviour {
    /**
     * Called once when the op mode is initialized.
     */
    void awake();

    /**
     * Called every loop cycle while the op mode is in the init phase.
     */
    default void preUpdate() {}

    /**
     * Called every loop cycle while the op mode is active.
     */
    void update();

    /**
     * Called when the op mode is stopped.
     */
    default void destroy() {}
}
