package moe.seikimo.ftc;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.EncoderDirection;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver.GoBildaOdometryPods;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
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
    String MOTOR_INTAKE_FRONT = "intake_front";

    String SENSOR_PINPOINT = "pinpoint";
    String SENSOR_LIMELIGHT = "Limelight";
    int LIMELIGHT_POLL_RATE = 100;

    String SERVO_KICKER_LEFT = "kicker_left";
    String SERVO_KICKER_RIGHT_TOP = "kicker_right_top";
    String SERVO_KICKER_RIGHT_BOTTOM = "kicker_right_bottom";

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

    PinpointConstants LOCALIZER = new PinpointConstants()
        .hardwareMapName(SENSOR_PINPOINT)
        .distanceUnit(DistanceUnit.MM)
        .encoderResolution(GoBildaOdometryPods.goBILDA_4_BAR_POD)
        .forwardPodY(-119.594)
        .forwardEncoderDirection(EncoderDirection.FORWARD)
        .strafePodX(-69.906)
        .strafeEncoderDirection(EncoderDirection.FORWARD);

    PathConstraints CONSTRAINTS = new PathConstraints(0.99, 100, 1, 1);

    /**
     * Factory function for creating a Follower instance.
     */
    Function<HardwareMap, Follower> FOLLOWER_FACTORY = hwMap ->
        new FollowerBuilder(FOLLOW, hwMap)
            .mecanumDrivetrain(DRIVE)
            .pathConstraints(CONSTRAINTS)
            .pinpointLocalizer(LOCALIZER)
            .build();
    // endregion
}
