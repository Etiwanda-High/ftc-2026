package moe.seikimo.ftc.game;

import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Trigger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.DriverProfile;

@RequiredArgsConstructor
public final class PlayerController implements MonoBehaviour {
    private final GamepadEx handle;

    /** The profile to use for adapting gamepad inputs into values. */
    @Getter @Setter private DriverProfile profile = DriverProfile.DEFAULT;

    /**
     * Runs the given action when the super button is pressed.
     *
     * @param action The action to run.
     */
    public void superPressed(Runnable action) {
        if (this.handle.isDown(this.profile.superButton)) {
            action.run();
        }
    }

    // region Handle Accessors

    /** @return The value of the right trigger. */
    public double rightTrigger() {
        return this.handle.getTrigger(Trigger.RIGHT_TRIGGER);
    }

    /** @return The value of the left trigger. */
    public double leftTrigger() {
        return this.handle.getTrigger(Trigger.LEFT_TRIGGER);
    }

    /**
     * Fetches the button object for the given button key.
     *
     * @param button The button key to fetch.
     * @return The button object.
     */
    public GamepadButton button(Button button) {
        return this.handle.getGamepadButton(button);
    }

    /**
     * Fetches the state of the given button.
     *
     * @param button The button to check.
     * @return The state of the button.
     */
    public boolean state(Button button) {
        return this.handle.getButton(button);
    }

    // endregion

    // region Driver Profile Accessors

    public String profileName() {
        return this.profile.getName();
    }

    public double translateX() {
        return this.profile.translateX.apply(this.handle);
    }

    public double translateY() {
        return this.profile.translateY.apply(this.handle);
    }

    public double rotate() {
        return this.profile.rotate.apply(this.handle);
    }

    public double fixedPower() {
        return this.profile.getFixedPower();
    }

    public double launchPower() {
        return this.profile.launchPower.apply(this.handle);
    }

    // endregion

    // region MonoBehavior Implementation

    @Override
    public void awake() {

    }

    @Override
    public void preUpdate() {
        this.handle.readButtons();

        if (this.handle.isDown(Button.B)) {
            this.profile = Constants.DRIVE_FLYSKY;
        } else if (this.handle.isDown(Button.A)) {
            this.profile = DriverProfile.DEFAULT;
        }
    }

    @Override
    public void update() {
        this.handle.readButtons();
    }

    // endregion
}
