package moe.seikimo.ftc;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.OTOSConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.function.Function;

/**
 * Robot constants.
 * <p>
 * Includes constants for Pedro Pathing.
 */
public interface Constants {
    // region Launch Constants

    double LAUNCH_POWER_CLOSE = 0.7;
    double LAUNCH_POWER_FAR = 0.79;

    // endregion

    // region Hardware Map & Sensor Constants
    String MOTOR_LAUNCH = "launcher";
    String MOTOR_INTAKE = "intake";

    String SERVO_NUH = "nuh";
    float SERVO_DOOR_OPENED = 0.65f;
    float SERVO_DOOR_CLOSED = 0.35f;
    String SERVO_UH = "uh";

    String SENSOR_LIMELIGHT = "Limelight";
    int LIMELIGHT_POLL_RATE = 100;

    String SENSOR_IMU = "imu";
    String SENSOR_OTOS = "otos";
    int OTOS_SAMPLES = 512;

    String DRIVE_FRONT_LEFT = "left_front";
    String DRIVE_FRONT_RIGHT = "right_front";
    String DRIVE_BACK_LEFT = "left_back";
    String DRIVE_BACK_RIGHT = "right_back";
    // endregion

    // region Pedro's Pathing
    FollowerConstants FOLLOW = new FollowerConstants();

    MecanumConstants DRIVE = new MecanumConstants()
        .maxPower(1)
        .useBrakeModeInTeleOp(true)
        .rightFrontMotorName(DRIVE_FRONT_RIGHT)
        .leftFrontMotorName(DRIVE_FRONT_LEFT)
        .rightRearMotorName(DRIVE_BACK_RIGHT)
        .leftRearMotorName(DRIVE_BACK_LEFT)
        .leftFrontMotorDirection(Direction.REVERSE)
        .leftRearMotorDirection(Direction.REVERSE)
        .rightFrontMotorDirection(Direction.FORWARD)
        .rightRearMotorDirection(Direction.FORWARD);

    OTOSConstants LOCALIZER = new OTOSConstants()
        .hardwareMapName(SENSOR_OTOS)
        .linearUnit(DistanceUnit.INCH)
        .angleUnit(AngleUnit.RADIANS);

    PathConstraints CONSTRAINTS = new PathConstraints(0.99, 100, 1, 1);

    /**
     * Factory function for creating a Follower instance.
     */
    Function<HardwareMap, Follower> FOLLOWER_FACTORY = hwMap ->
        new FollowerBuilder(FOLLOW, hwMap)
            .mecanumDrivetrain(DRIVE)
            .pathConstraints(CONSTRAINTS)
            .OTOSLocalizer(LOCALIZER)
            .build();
    // endregion
}
