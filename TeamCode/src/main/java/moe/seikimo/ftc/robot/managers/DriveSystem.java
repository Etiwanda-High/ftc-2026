package moe.seikimo.ftc.robot.managers;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import lombok.Getter;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.annotations.types.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.utils.Logger;

@RobotSystem
public final class DriveSystem implements MonoBehaviour {
    private final Logger logger;
    @Getter private final Follower follower;
    /** Dependency injection constructor. */
    public DriveSystem(Logger logger, HardwareMap hwMap) {
        this.logger = logger;
        this.follower = Constants.FOLLOWER_FACTORY.apply(hwMap);
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
