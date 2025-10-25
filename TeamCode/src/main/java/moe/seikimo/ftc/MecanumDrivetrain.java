package moe.seikimo.ftc;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import lombok.Setter;
import lombok.val;
import lombok.var;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class MecanumDrivetrain {
    private final Telemetry telemetry;
    private final Localization locale;

    private final MecanumDrive handle;

    /**
     * When enabled, the inputs will be based on the robot's current heading.
     */
    @Setter
    private boolean relativeDrive = false;

    /**
     * The maximum speed of the drivetrain (0.0 - 1.0).
     */
    private double maxSpeed = 1.0;

    /**
     * Creates a new instance of the mecanum drivetrain.
     *
     * @param hwMap Hardware map to use for initializing motors.
     */
    public MecanumDrivetrain(Telemetry telemetry, Localization locale, HardwareMap hwMap) {
        this.telemetry = telemetry;
        this.locale = locale;

        val frontLeft = new Motor(hwMap, Constants.DRIVE_FRONT_LEFT);
        val frontRight = new Motor(hwMap, Constants.DRIVE_FRONT_RIGHT);
        val backLeft = new Motor(hwMap, Constants.DRIVE_BACK_LEFT);
        val backRight = new Motor(hwMap, Constants.DRIVE_BACK_RIGHT);
        this.handle = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);
    }

    /**
     * Invoked once when the op mode is first initialized.
     */
    public void awake() {
    }

    /**
     * Invoked every loop cycle while the op mode is active.
     */
    public void update() {
        this.handle.setMaxSpeed(this.maxSpeed);

        this.telemetry.addLine("\nDrivetrain:");
        this.telemetry.addData("- Max Speed", this.maxSpeed);
        this.telemetry.addData("- Relative Drive", this.relativeDrive);
    }

    /**
     * Handles input from the drive gamepad.
     *
     * @param gamepad Gamepad to read input from.
     */
    public void input(GamepadEx gamepad, DriverProfile profile) {
        if (this.relativeDrive) {
            // driveRobotCentric: Robot-centric assumes that each push of the joystick is in relation to the local position
            // of the robot—this means that whenever the user pushes the drive stick forward, the robot will drive in the
            // direction of its front-facing side.
            this.handle.driveRobotCentric(
                profile.translateX.apply(gamepad),
                profile.translateY.apply(gamepad),
                profile.rotate.apply(gamepad)
            );
        } else {
            // driveFieldCentric: Field-centric assumes that each push of the joystick is in relation to the global position
            // of the robot—this means that whenever the user pushes the drive stick forward, the robot will move away from
            // the driver no matter its orientation.
            this.handle.driveFieldCentric(
                profile.translateX.apply(gamepad),
                profile.translateY.apply(gamepad),
                profile.rotate.apply(gamepad),
                this.locale.getHeading()
            );
        }

        // Change the speed according to the D-pad input.
        var modifier = 0d;
        if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
            modifier = 0.1;
        } else if (gamepad.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
            modifier = -0.1;
        }
        this.maxSpeed = Math.min(1.0, Math.max(0.0, this.maxSpeed + modifier));
    }
}
