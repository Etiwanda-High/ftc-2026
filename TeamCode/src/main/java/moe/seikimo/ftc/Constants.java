package moe.seikimo.ftc;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
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

    String SENSOR_LIMELIGHT = "Limelight";
    /** 250Hz is the max poll rate for the Limelight 3A. */
    int LIMELIGHT_POLL_RATE = 250;

    String SENSOR_IMU = "imu";
    String SENSOR_PINPOINT = "pinpoint";

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
        // These values are taken from the OnShape model as of 11/21/2025.
        .forwardPodY(-100.8) // 4 inches backward
        .strafePodX(-76.7)   // 3 inches left
        .encoderResolution(GoBildaOdometryPods.goBILDA_4_BAR_POD)
        // TODO: Evaluate whether these directions are correct.
        //       They are both going to be FORWARD, or REVERSE.
        .forwardEncoderDirection(EncoderDirection.FORWARD)
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
