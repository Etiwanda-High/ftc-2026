package moe.seikimo.ftc.robot.v2;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import lombok.Setter;
import lombok.val;
import lombok.var;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.game.GameManager;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class DriveSystem extends SubsystemBase {
    private final GameManager gameManager;
    private final Telemetry telemetry;

    private final MecanumDrive handle;
    private final Follower follower;

    // region Settings

    /**
     * When enabled, the inputs will be based on the robot's current heading.
     */
    @Setter
    private boolean relativeDrive = true;

    /**
     * The maximum speed of the drivetrain (0.0 - 1.0).
     */
    private double maxSpeed = 1.0;

    // endregion

    /**
     * Creates a new instance of the mecanum drivetrain.
     *
     * @param gameManager Game manager to use for initializing motors.
     */
    public DriveSystem(GameManager gameManager) {
        this.gameManager = gameManager;
        this.telemetry = gameManager.getTelemetry();

        val hwMap = gameManager.getHwMap();
        val frontLeft = new Motor(hwMap, Constants.DRIVE_FRONT_LEFT);
        val frontRight = new Motor(hwMap, Constants.DRIVE_FRONT_RIGHT);
        val backLeft = new Motor(hwMap, Constants.DRIVE_BACK_LEFT);
        val backRight = new Motor(hwMap, Constants.DRIVE_BACK_RIGHT);
        this.handle = new MecanumDrive(frontLeft, frontRight, backLeft, backRight);

        this.follower = Constants.FOLLOWER_FACTORY.apply(hwMap);
    }

    /**
     * Inputs movement commands to the drivetrain.
     *
     * @param translateX The translation in the X direction.
     * @param translateY The translation in the Y direction.
     * @param rotate The rotation amount.
     */
    public void input(double translateX, double translateY, double rotate) {
        // Stop the motor if all values are 0.

        if (!this.relativeDrive) {
            var heading = this.gameManager.getLocale().getHeading();

            // driveFieldCentric: Field-centric assumes that each push of the joystick is in relation to the global position
            // of the robot—this means that whenever the user pushes the drive stick forward, the robot will move away from
            // the driver no matter its orientation.
            this.handle.driveFieldCentric(translateX, translateY, rotate, heading);
        } else {
            // driveRobotCentric: Robot-centric assumes that each push of the joystick is in relation to the local position
            // of the robot—this means that whenever the user pushes the drive stick forward, the robot will drive in the
            // direction of its front-facing side.
            this.handle.driveRobotCentric(translateX, translateY, rotate);
        }
    }

    // region Accessors

    /** Inverts the right motor array. */
    public void invert() {
        val isInverted = this.handle.isRightSideInverted();
        this.handle.setRightSideInverted(!isInverted);
    }

    /** Simple accessor to increase drive speed. */
    public void increaseSpeed() {
        this.maxSpeed = Math.min(1.0, this.maxSpeed + 0.1);
    }

    /** Simple accessor to decrease drive speed. */
    public void decreaseSpeed() {
        this.maxSpeed = Math.max(0.0, this.maxSpeed - 0.1);
    }

    // endregion

    // region Subsystem Implementation

    @Override
    public void register() {
        super.register();

        this.handle.setRightSideInverted(false);
    }

    @Override
    public void periodic() {
        this.follower.update();
        this.handle.setMaxSpeed(this.maxSpeed);

        this.telemetry.addLine("\nDrivetrain:");
        this.telemetry.addData("- Max Speed", this.maxSpeed);
        this.telemetry.addData("- Relative Drive", this.relativeDrive);
        this.telemetry.addData("- Right invert?", this.handle.isRightSideInverted());
    }

    // endregion
}
