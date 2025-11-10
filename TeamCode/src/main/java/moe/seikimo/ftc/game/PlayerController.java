package moe.seikimo.ftc.game;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Trigger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import moe.seikimo.ftc.DriverProfile;
import moe.seikimo.ftc.game.commands.ConditionalRunCommand;

@RequiredArgsConstructor
public final class PlayerController implements MonoBehaviour {
    @Getter private final GamepadEx handle;

    /** The profile to use for adapting gamepad inputs into values. */
    @Getter @Setter private DriverProfile profile = DriverProfile.DEFAULT;

    // region Modifiers

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

    /**
     * Runs the given action when the launch modifier is pressed.
     *
     * @param action The action to run.
     */
    public void intake(Runnable action) {
        if (this.handle.isDown(this.profile.intakeModifier)) {
            action.run();
        }
    }

    /**
     * Runs the given action when the launch modifier is pressed.
     *
     * @param action The action to run.
     */
    public void launch(Runnable action) {
        if (this.handle.isDown(this.profile.launchModifier)) {
            action.run();
        }
    }

    /** @return True if the intake modifier is down. */
    public boolean intake() {
        return this.handle.isDown(this.profile.intakeModifier);
    }

    /** @return True if the launch modifier is down. */
    public boolean launch() {
        return this.handle.isDown(this.profile.launchModifier);
    }

    /**
     * Adds a binding for the intake action.
     *
     * @param button The button to listen for.
     * @param press The action to complete.
     * @return This instance for method chaining.
     */
    public PlayerController intake(Button button, Command press) {
        return this.intake(button, press, null);
    }

    /**
     * Adds a binding for the intake action.
     *
     * @param button The button to listen for.
     * @param press The action to complete.
     * @param release The action to complete on release.
     * @return This instance for method chaining.
     */
    public PlayerController intake(Button button, Command press, Command release) {
        val handle = this.button(button);
        if (press != null) {
            handle.whenHeld(new ConditionalRunCommand(this::intake, press), true);
        }
        if (release != null) {
            handle.whenReleased(new ConditionalRunCommand(this::intake, release), false);
        }
        return this;
    }

    /**
     * Adds a binding for the launch action.
     *
     * @param button The button to listen for.
     * @param press The action to complete.
     * @param release The action to complete on release.
     * @return This instance for method chaining.
     */
    public PlayerController launch(Button button, Command press, Command release) {
        this.button(button)
            .whenHeld(new ConditionalRunCommand(this::launch, press), true)
            .whenReleased(new ConditionalRunCommand(this::launch, release), false);
        return this;
    }

    // endregion

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

    public double intakePower() {
        return this.profile.intakePower.apply(this.handle);
    }

    public double intakeReverse() {
        return this.profile.intakeReverse.apply(this.handle);
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
            this.profile = DriverProfile.FLYSKY;
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
