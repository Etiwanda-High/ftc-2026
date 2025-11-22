package moe.seikimo.ftc.robot.managers;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;
import lombok.Getter;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.annotations.types.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.utils.Logger;

@RobotSystem(RobotSystem.Priority.HIGHEST)
public final class DriveSystem implements MonoBehaviour {
    private final Logger logger;

    /**
     * The Pedro Pathing follower is like its own system.
     * It's a batteries-included odometry and path following manager.
     * <p>
     * Exposing it here allows us to modify the pose using the localization system.
     */
    @Getter private final Follower follower;

    /** Dependency injection constructor. */
    public DriveSystem(Logger logger, HardwareMap hwMap) {
        this.logger = logger;
        this.follower = Constants.FOLLOWER_FACTORY.apply(hwMap);
    }

    /** @return The bot's pose from the follower. */
    public Pose getPose() {
        return this.follower.getPose();
    }

    /** Sets the follower's pose. */
    public void setPose(Pose pose) {
        this.follower.setPose(pose);
    }

    @Override
    public void preUpdate() {
        this.logger
            .section("Drive System")
            .log("Status", "Ready");
    }

    @Override
    public void update() {
        this.follower.update();

        this.logger
            .section("Drive System")
            .log("Using heading?", "%s", this.follower.useHeading);
    }
}
