package moe.seikimo.ftc.robot.managers;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ftc.InvertedFTCCoordinates;
import com.pedropathing.geometry.PedroCoordinates;
import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import lombok.RequiredArgsConstructor;
import lombok.val;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.annotations.fields.Hardware;
import moe.seikimo.ftc.annotations.types.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.utils.Logger;

/**
 * Localization does odometry with a Limelight 3A and the goBILDA Pinpoint Computer.
 */
@Configurable
@RequiredArgsConstructor
@RobotSystem(RobotSystem.Priority.LOW)
public final class LocalizationSystem implements MonoBehaviour {
    // region Configurable Constants

    /** The maximum acceptable age for Limelight camera data. */
    public static long LIMELIGHT_MAX_AGE = 10;
    /** The maximum acceptable estimated heading (yaw) error. */
    @SuppressWarnings("unused")
    public static double HEADING_ACCEPTABLE_ERROR = 1.0;

    // endregion

    private final Logger logger;
    private final DriveSystem drive;

    /**
     * The Limelight API assumes:
     * - The angle `0deg` is facing toward the back. (away from the goals)
     * - The angle `180deg` is facing toward the front. (toward the goals)
     * - The (pos, pos) quadrant is at the back right. (blue loading zone)
     * - The (neg, neg) quadrant is at the front left. (blue goal)
     */
    @Hardware(Constants.SENSOR_LIMELIGHT)
    private Limelight3A camera;

    /**
     * Attempts to update the robot's pose using Limelight data.
     */
    private void tryUpdatePose() {
        // Get the current robot's pose using the follower.
        // We convert the Pedro Pathing coordinates into FTC coordinates. (what Limelight uses)
        // TODO: Investigate if we need to do value error handling with the heading.
        // TODO: Investigate whether this value returns the true current heading.
        //       If not, access the IMU directly.
        // TODO: Investigate whether we need to use `InvertedFTCCoordinates.INSTANCE` instead.
        //       "pedro docs say to use inverted coordinates"
        val currentPose = this.drive.getPose().getAsCoordinateSystem(InvertedFTCCoordinates.INSTANCE);
        // Update the robot's yaw (relative to the camera).
        this.camera.updateRobotOrientation(currentPose.getHeading());

        // Read the latest Limelight result.
        // We ignore data older than configured.
        if (this.camera.getTimeSinceLastUpdate() > LIMELIGHT_MAX_AGE) {
            return;
        }
        val result = this.camera.getLatestResult();
        if (result == null || !result.isValid()) {
            return;
        }

        // Estimate the pose from the Limelight result.
        val resultPose = result.getBotpose_MT2();
        if (resultPose == null) {
            return;
        }

        val pos = resultPose.getPosition();
        val rot = resultPose.getOrientation();
        val newPose = new Pose(pos.x, pos.y, rot.getYaw(), InvertedFTCCoordinates.INSTANCE);
        this.drive.setPose(newPose.getAsCoordinateSystem(PedroCoordinates.INSTANCE));
    }

    @Override
    public void preUpdate() {
        this.logger
            .section("Localization System")
            .log("Status", "Ready");
    }

    @Override
    public void start() {
        this.camera.setPollRateHz(Constants.LIMELIGHT_POLL_RATE);
        this.camera.start();
    }

    @Override
    public void update() {
        this.tryUpdatePose();

        val pose = this.drive.getPose();
        this.logger
            .section("Localization System")
            .log("Follower Position", "(%.2f, %.2f)",
                pose.getX(), pose.getY())
            .log("Follower Yaw", "%.2f°",
                pose.getHeading())
            .log("Heading Error", "%.2f°",
                this.drive.getFollower().getHeadingError());
    }
}
