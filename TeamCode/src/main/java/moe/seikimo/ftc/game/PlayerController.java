package moe.seikimo.ftc.game;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import lombok.Getter;
import lombok.Setter;

/**
 * An abstracted way to query controller inputs.
 */
@Setter
public final class PlayerController implements MonoBehaviour {
    @Getter private final GamepadEx handle;

    private ControllerProfile profile = ControllerProfile.DEFAULT;

    /**
     * Creates new instances of controller wrappers & states.
     *
     * @param gamepad The gamepad to wrap.
     */
    public PlayerController(Gamepad gamepad) {
        this.handle = new GamepadEx(gamepad);
    }

    // region Accessors

    /** @return The horizontal (X-axis) translation. */
    public double translateX() {
        return this.profile.translateX.apply(this.handle);
    }

    /** @return The vertical (Y-axis) translation. */
    public double translateY() {
        return this.profile.translateY.apply(this.handle);
    }

    /** @return The rotational axis. */
    public double rotate() {
        return this.profile.rotate.apply(this.handle);
    }

    // endregion

    // region MonoBehavior Implementation

    @Override
    public void awake() {
        // TODO: Register configuration button callbacks.
    }

    @Override
    public void preUpdate() {
        this.handle.readButtons();
    }

    @Override
    public void start() {
        // TODO: Register button callbacks.
    }

    @Override
    public void update() {
        this.handle.readButtons();
    }

    // endregion
}
