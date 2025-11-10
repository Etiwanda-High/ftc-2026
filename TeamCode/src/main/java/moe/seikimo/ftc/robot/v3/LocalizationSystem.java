package moe.seikimo.ftc.robot.v3;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import lombok.RequiredArgsConstructor;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.annotations.Hardware;
import moe.seikimo.ftc.annotations.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.utils.Logger;

/**
 * Localization does odometry with a Limelight 3A and the SparkFun OTOS.
 */
@RequiredArgsConstructor
@RobotSystem(RobotSystem.Priority.HIGHEST)
public final class LocalizationSystem implements MonoBehaviour {
    private final Logger logger;

    @Hardware(Constants.SENSOR_LIMELIGHT)
    private Limelight3A limelight;

    @Hardware(Constants.SENSOR_OTOS)
    private SparkFunOTOS otos;

    @Override
    public void preUpdate() {
        this.logger
            .section("Localization System")
            .log("Status", "Ready");
    }

    @Override
    public void update() {
        this.logger
            .section("Localization System")
            .log("OTOS Position", "X: %.2f, Y: %.2f",
                this.otos.getPosition().x,
                this.otos.getPosition().y,
                this.otos.getPosition().h)
            .log("OTOS Heading", "%.2f", this.getHeading());
    }

    /** @return The robot heading (in degrees). */
    public float getHeading() {
        return (float)this.otos.getPosition().h;
    }
}
