package moe.seikimo.ftc.robot.v2;

import android.annotation.SuppressLint;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import lombok.experimental.ExtensionMethod;
import lombok.val;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.Convert;
import moe.seikimo.ftc.exceptions.LimelightException;
import moe.seikimo.ftc.exceptions.NoResultException;
import moe.seikimo.ftc.exceptions.NoTagDetectedException;
import moe.seikimo.ftc.extensions.GamepadExtensions;
import moe.seikimo.ftc.game.GameManager;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@ExtensionMethod({GamepadExtensions.class})
public final class Localization extends SubsystemBase {
    private final GameManager gameManager;
    private final Telemetry telemetry;

    private final Rev9AxisImu imu;
    private final SparkFunOTOS otos;
    private final Limelight3A camera;

    /**
     * Creates a new instance of the localization tracker.
     *
     * @param gameManager Game manager to use for initializing sensors.
     */
    public Localization(GameManager gameManager) {
        this.gameManager = gameManager;
        this.telemetry = gameManager.getTelemetry();

        val hwMap = gameManager.getHwMap();
        this.imu = hwMap.get(Rev9AxisImu.class, Constants.SENSOR_IMU);
        this.otos = hwMap.get(SparkFunOTOS.class, Constants.SENSOR_OTOS);
        // this.camera = hwMap.get(Limelight3A.class, Constants.SENSOR_LIMELIGHT);

        this.camera = null;
    }

    /**
     * This function uses AprilTags to determine the robot's position on the field.
     *
     * @return The pose from the Limelight 3A.
     */
    public SparkFunOTOS.Pose2D localize() throws NoResultException, NoTagDetectedException {
        // Pass the yaw to the Limelight to help it determine the robot's orientation.
        this.camera.updateRobotOrientation(this.getHeading());

        // Read the robot's position from the Limelight.
        val result = this.camera.getLatestResult();
        if (result == null || !result.isValid()) {
            throw new NoResultException();
        }

        val pose = result.getBotpose_MT2();
        if (pose == null) {
            throw new NoTagDetectedException();
        }

        return Convert.convert(pose);
    }

    /**
     * Passthrough function to reset all localization techniques.
     */
    public void resetYaw() {
        this.imu.resetYaw();

        val pose = this.getPose();
        this.otos.setPosition(new SparkFunOTOS.Pose2D(
            pose.getX(), pose.getY(), 0
        ));
    }

    // region Subsystem Implementation

    @Override
    public void register() {
        super.register();

        this.configureOdometry(); // First pass configuration; sets settings.
        this.configureLimelight(); // First pass configuration; sets settings, starts reading.

        try {
            // Read the robot's position.
            val pose = this.localize();
            this.otos.setPosition(pose);
        } catch (LimelightException ex) {
            // ignored
        }
    }

    @Override
    public void periodic() {
        this.telemetry.addLine("\nLocalization:");
        this.telemetry.addData("Heading", this.getHeading());
        this.telemetry.addData("Pose", this.getPosition());
    }

    // endregion

    // region Getters

    /**
     * @return The robot's rotational heading in degrees.
     */
    public double getHeading() {
        return this.imu.getRobotYawPitchRollAngles().getYaw();
    }

    /**
     * @return THe robot's current pose as reported by the OTOS.
     */
    public Pose2d getPose() {
        return Convert.convert(this.otos.getPosition());
    }

    /**
     * @return A string-representation of the robot's current position.
     */
    @SuppressLint("DefaultLocale")
    public String getPosition() {
        val pose = this.getPose();
        return String.format("(%.3f, %.3f) @ %.3f",
            pose.getX(), pose.getY(), pose.getHeading());
    }

    // endregion

    // region Hardware Configuration

    /**
     * Configures the Limelight 3A camera.
     */
    private void configureLimelight() {
        this.camera.setPollRateHz(Constants.LIMELIGHT_POLL_RATE);
        this.camera.start();
    }

    /**
     * Configures the SparkFun OTOS (optical tracking odometry sensor).
     */
    private void configureOdometry() {
        this.otos.setLinearUnit(DistanceUnit.MM);
        this.otos.setAngularUnit(AngleUnit.DEGREES);

        // Calibrate the sensor by taking samples.
        // This is blocking and will wait until all samples are taken.
        this.otos.calibrateImu(Constants.OTOS_SAMPLES, true);
    }

    // endregion
}
