package moe.seikimo.ftc;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.OTOSConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.function.Function;

/**
 * Robot constants.
 * <p>
 * Includes constants for Pedro Pathing.
 */
public interface Constants {
    // region Driver Profiles
    DriverProfile DRIVE_FLYSKY = DriverProfile.builder()
        .name("FlySky")
        .translateY(GamepadEx::getRightY)
        .translateX(GamepadEx::getRightX)
        .rotate(g -> -g.getLeftX())
        .build();
    // endregion

    // region Hardware Map & Sensor Constants
    String SENSOR_LIMELIGHT = "limelight";
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
