package moe.seikimo.ftc.robot.v3;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.annotations.Controller;
import moe.seikimo.ftc.annotations.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.game.PlayerController;
import moe.seikimo.ftc.utils.Logger;

@RobotSystem
public final class DriveSystem implements MonoBehaviour {
    private final Logger logger;
    private final Follower follower;

    @Controller(Controller.Player.DRIVER)
    private PlayerController driver;

    /**
     * Constructs the drive system.
     *
     * @param hwMap The hardware map.
     */
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

        // Robot-centric drive.
        this.follower.setTeleOpDrive(
            this.driver.translateY(),
            this.driver.translateX(),
            this.driver.rotate()
        );

        this.logger
            .section("Drive System")
            .log("Using heading?", "%s", this.follower.useHeading);
    }
}
