package org.firstinspires.ftc.teamcode.game;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.DriverProfile;
import moe.seikimo.ftc.robot.v1.Localization;
import moe.seikimo.ftc.robot.v1.MecanumDrivetrain;

@TeleOp(name = "V1", group = "Game")
public final class V1TeleOpMode extends OpMode {
    private GamepadEx player1, player2;
    private DriverProfile profile1, profile2;

    private Localization locale;
    private MecanumDrivetrain drive;

    private Motor motor;

    /**
     * Sets the hardware variables in the op mode.
     */
    private void configureHardware() {
        // Configure players.
        this.player1 = new GamepadEx(this.gamepad1);
        this.profile1 = DriverProfile.DEFAULT;
        this.player2 = new GamepadEx(this.gamepad2);
        this.profile2 = DriverProfile.DEFAULT;

        // Configure drivetrain.
        this.locale = new Localization(this.telemetry, this.hardwareMap);
        this.drive = new MecanumDrivetrain(this.telemetry, this.locale, this.hardwareMap);
        this.drive.setRelativeDrive(true);

        this.motor = new Motor(this.hardwareMap, "intake");
    }

    /**
     * Invoked once when the op mode is selected and the init button is pressed.
     */
    private void awake() {
        this.telemetry.addData("Status", "Starting...");
        this.telemetry.update();

        // this.locale.awake();
        this.drive.awake();

        this.telemetry.addData("Status", "Initialized");
    }

    /**
     * Invoked every loop cycle while the op mode is in the init phase.
     * <p>
     * TODO: Pre-match configuration system?
     */
    private void preUpdate() {
        this.telemetry.addData("Status", "Configuration");

        // Add driver profile status text.
        this.telemetry.addLine("\nDriver Profile Selection:");
        this.telemetry.addData(" - Player 1", this.profile1.getName());
        this.telemetry.addData(" - Player 2", this.profile2.getName());
        this.telemetry.addLine("Press [A] for Default profile.");
        this.telemetry.addLine("Press [B] for FlySky profile.");

        // TODO: "maybe abstract/separate this into another handle\class?"
        this.profile1 = this.updateProfile(this.player1, this.profile1);
        this.profile2 = this.updateProfile(this.player2, this.profile2);

        // Update system.
        this.locale.preUpdate();
        this.locale.preInput(this.player1);
    }

    /**
     * Invoked every loop cycle while the op mode is active.
     */
    private void update() {
        this.telemetry.addData("Status", "Running");

        // Update systems.
        this.locale.update();
        this.locale.input(this.player1);
        this.drive.update();
        this.drive.input(this.player1, this.profile1);

        this.motor.set(this.gamepad1.left_trigger);
    }

    // region Prompt & Selection

    /**
     * Updates the driver profile based on gamepad input.
     *
     * @param gamepad The gamepad to read input from.
     * @param current The current driver profile.
     * @return The updated driver profile.
     */
    private DriverProfile updateProfile(GamepadEx gamepad, DriverProfile current) {
        if (gamepad.wasJustPressed(Button.B)) {
            return Constants.DRIVE_FLYSKY;
        }
        if (gamepad.wasJustPressed(Button.A)) {
            return DriverProfile.DEFAULT;
        }

        return current;
    }

    // endregion

    // region OpMode Implementation

    @Override
    public void init() {
        this.configureHardware();
        this.awake();
        this.telemetry.update();
    }

    @Override
    public void init_loop() {
        // Update controllers.
        this.player1.readButtons();
        this.player2.readButtons();

        this.preUpdate();
        this.telemetry.update();
    }

    @Override
    public void loop() {
        // Update controllers.
        this.player1.readButtons();
        this.player2.readButtons();

        this.update();
        this.telemetry.update();
    }

    // endregion
}
