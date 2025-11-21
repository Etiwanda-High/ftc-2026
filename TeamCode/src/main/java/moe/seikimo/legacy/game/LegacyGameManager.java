package moe.seikimo.legacy.game;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import moe.seikimo.legacy.LegacyProfile;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.legacy.utils.LegacyLogger;
import moe.seikimo.legacy.robot.v2.DriveSystem;
import moe.seikimo.legacy.robot.v2.IntakeSystem;
import moe.seikimo.legacy.robot.v2.LaunchSystem;
import moe.seikimo.legacy.robot.v2.Localization;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Manages the game and its state.
 */
@Getter
@RequiredArgsConstructor
public final class LegacyGameManager implements MonoBehaviour {
    private final Telemetry telemetry;
    private final LegacyLogger logger = new LegacyLogger(this);

    private final HardwareMap hwMap;
    private final Gamepad gp1, gp2;

    private LegacyController driver, controller;
    private Localization locale;
    private DriveSystem drive;
    private LaunchSystem launch;
    private IntakeSystem intake;

    /** Reads the hardware map. */
    private void configureHardware() {
        this.driver = new LegacyController(new GamepadEx(this.gp1));
        this.controller = new LegacyController(new GamepadEx(this.gp2));

        this.locale = new Localization(this);
        this.drive = new DriveSystem(this);
        this.launch = new LaunchSystem(this);
        this.intake = new IntakeSystem(this);
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

        this.driver
            .button(Button.LEFT_STICK_BUTTON)
            .whenPressed(this.locale::resetYaw);
        this.driver
            .button(Button.RIGHT_STICK_BUTTON)
            .whenPressed(this.locale::reset);

        this.driver.awake();
    }

    /** Adds command bindings for controller gamepads. */
    private void configureControllerCommands() {
        // region Button Controls
        this.controller
            .button(LegacyProfile.SET_SPEED_CLOSE)
            .whenPressed(this.launch::speedClose);
        this.controller
            .button(LegacyProfile.SET_SPEED_FAR)
            .whenPressed(this.launch::speedFar);
        this.controller
            .button(LegacyProfile.LAUNCH_SPEED_INCREASE)
            .whenPressed(this.launch::speedUp);
        this.controller
            .button(LegacyProfile.LAUNCH_SPEED_DECREASE)
            .whenPressed(this.launch::speedDown);

        this.controller
            .button(LegacyProfile.INTAKE_GATE_TOGGLE)
            .whenPressed(this.intake::toggleGate);
        this.controller
            .button(Button.A)
            .whenPressed(() -> this.intake.setUseNuh(!this.intake.isUseNuh()));
        // endregion

        // region Triggers
        this.controller
            .button(LegacyProfile.LAUNCH_TOGGLE)
            .whenPressed(this.launch::toggle);
        this.controller
            .button(LegacyProfile.LAUNCH_REVERSE)
            .whenPressed(() -> this.launch.setReverse(true))
            .whenReleased(() -> this.launch.setReverse(false));
        // endregion

        this.controller.awake();
    }

    // region MonoBehavior Implementation

    @Override
    public void awake() {
        this.telemetry.addData("Status", "Initializing");

        this.configureHardware();
        this.configureDriverCommands();
        this.configureControllerCommands();

        this.drive.awake();
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

        // Update controllers.
        this.driver.preUpdate();
        this.controller.preUpdate();

        // Update systems.
        this.drive.preUpdate();

        // Update telemetry.
        this.logger.push();
    }

    @Override
    public void start() {
        // Start controllers.
        this.driver.start();
        this.controller.start();

        // Start systems.
        this.drive.start();

        // Update telemetry.
        this.logger.push();
    }

    @Override
    public void update() {
        this.telemetry.addData("Status", "Running");

        // Update controllers.
        this.driver.update();
        this.controller.update();

        // NOTE: Systems do not get updated.
        //       They are configured to receive `periodic()` calls from the CommandScheduler.
        this.drive.input(
            this.driver.translateX(),
            this.driver.translateY(),
            -this.driver.rotate()
        );
        this.drive.update();

        // Update telemetry.
        this.logger.push();
    }

    // endregion
}
