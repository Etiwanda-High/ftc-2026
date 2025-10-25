package moe.seikimo.ftc;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.Rev9AxisImu;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.HardwareMap;
import lombok.val;
import moe.seikimo.ftc.exceptions.LimelightException;
import moe.seikimo.ftc.exceptions.NoResultException;
import moe.seikimo.ftc.exceptions.NoTagDetectedException;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public final class Localization {
    private final Telemetry telemetry;

    private final Rev9AxisImu imu;
    private final SparkFunOTOS otos;
    private final Limelight3A camera;

    /**
     * Creates a new instance of the localization tracker.
     *
     * @param hwMap Hardware map to use for initializing sensors.
     */
    public Localization(Telemetry telemetry, HardwareMap hwMap) {
        this.telemetry = telemetry;

        this.imu = hwMap.get(Rev9AxisImu.class, Constants.SENSOR_IMU);
//        this.otos = hwMap.get(SparkFunOTOS.class, Constants.SENSOR_OTOS);
//        this.camera = hwMap.get(Limelight3A.class, Constants.SENSOR_LIMELIGHT);
        this.otos = null;
        this.camera = null;
    }

    /**
     * Invoked once when the op mode is first initialized.
     */
    public void awake() {
        this.configureOdometry(); // First pass configuration; sets settings.
        this.configureLimelight(); // First pass configuration; sets settings, starts reading.

        try {
            // Read the robot's position.
            val pose = this.localize();
            this.otos.setPosition(pose);
        } catch (LimelightException ex) {

        }
    }

    /**
     * Invoked every loop cycle while the op mode is active.
     */
    public void update() {
        this.telemetry.addLine("\nLocalization:");
        this.telemetry.addData("Heading", this.getHeading());
    }

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
     * @return The robot's rotational heading in degrees.
     */
    public double getHeading() {
        return this.imu.getRobotYawPitchRollAngles().getYaw();
    }
}
