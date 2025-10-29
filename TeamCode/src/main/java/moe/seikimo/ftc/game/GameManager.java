package moe.seikimo.ftc.game;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.PerpetualCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import moe.seikimo.ftc.robot.v2.*;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Manages the game and its state.
 */
@Getter
@RequiredArgsConstructor
public final class GameManager implements MonoBehaviour {
    private final TelemetryManager panels = PanelsTelemetry.INSTANCE.getTelemetry();
    private final Telemetry telemetry;

    private final HardwareMap hwMap;
    private final Gamepad gp1, gp2;

    private PlayerController driver, controller;
    private Localization locale;
    private DriveSystem drive;
    private LaunchSystem launch;

    /** Reads the hardware map. */
    private void configureHardware() {
        this.driver = new PlayerController(new GamepadEx(this.gp1));
        this.controller = new PlayerController(new GamepadEx(this.gp2));

        this.locale = new Localization(this);
        this.drive = new DriveSystem(this);
        this.launch = new LaunchSystem(this);
    }

    /** Adds command bindings for driver controllers. */
    private void configureDriverCommands() {
        this.driver
            .button(Button.DPAD_UP)
            .whenPressed(this.drive::increaseSpeed);
        this.driver
            .button(Button.DPAD_DOWN)
            .whenPressed(this.drive::decreaseSpeed);
        this.driver
            .button(Button.Y)
            .whenPressed(() -> this.controller.superPressed(this.locale::resetYaw));
        this.driver.awake();
    }

    /** Adds command bindings for controller gamepads. */
    private void configureControllerCommands() {
        this.controller
            .button(Button.RIGHT_BUMPER)
            .whenHeld(new RunCommand(() -> this.launch.input(this.controller.fixedPower()), this.launch))
            .whenReleased(new InstantCommand(() -> this.launch.input(0), this.launch));

//        this.launch.setDefaultCommand(
//            new PerpetualCommand(
//                new RunCommand(() -> this.launch.input(this.controller.launchPower()), this.launch)
//            )
//        );

        this.controller.awake();
    }

    // region Telemetry Extensions

    /**
     * Adds a formatted log entry to telemetry.
     *
     * @param caption The caption for the log entry.
     * @param args The arguments to format into the caption.
     * @return The game manager instance.
     */
    public GameManager log(String caption, String message, Object... args) {
        if (args.length == 0) {
            this.telemetry.addData(caption, message);
            this.panels.debug(String.format("%s: %s", caption, message));
        } else {
            val formatted = String.format(message, args);
            this.telemetry.addData(caption, formatted);
            this.panels.debug(String.format("%s: %s", caption, formatted));
        }

        return this;
    }

    // endregion

    // region MonoBehavior Implementation

    @Override
    public void awake() {
        this.telemetry.addData("Status", "Initializing");

        this.configureHardware();
        this.configureDriverCommands();
        this.configureControllerCommands();
    }

    @Override
    public void preUpdate() {
        this.telemetry.addData("Status", "Initialized");

        // Add driver profile status text.
        this.telemetry.addLine("\nDriver Profile Selection:");
        this.telemetry.addData(" - Player 1", this.driver.profileName());
        this.telemetry.addData(" - Player 2", this.controller.profileName());
        this.telemetry.addLine("Press [A] for Default profile.");
        this.telemetry.addLine("Press [B] for FlySky profile.");

        this.telemetry.update();

        this.driver.preUpdate();
        this.controller.preUpdate();
        this.panels.update();
    }

    @Override
    public void update() {
        this.telemetry.addData("Status", "Running");

        this.driver.update();
        this.controller.update();
        this.panels.update();
    }

    // endregion
}
