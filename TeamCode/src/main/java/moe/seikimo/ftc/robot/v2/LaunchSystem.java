package moe.seikimo.ftc.robot.v2;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.game.PlayerController;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class LaunchSystem extends SubsystemBase {
    private final GameManager gameManager;
    private final Telemetry telemetry;

    private final PlayerController controller;
    private final Motor motor;

    /**
     * Creates a new instance of the launcher system.
     *
     * @param gameManager Game manager to use for initializing motors.
     */
    public LaunchSystem(GameManager gameManager) {
        this.gameManager = gameManager;
        this.telemetry = gameManager.getTelemetry();

        this.controller = gameManager.getController();
        this.motor = new Motor(gameManager.getHwMap(), Constants.MOTOR_LAUNCH);
        this.motor.setInverted(true);
    }

    /**
     * Inputs power to the launcher motor.
     *
     * @param power The power to set the motor to.
     */
    public void input(double power) {
        this.motor.set(power);
    }

    /**
     * Stops the launcher motor.
     */
    public void stop() {
        this.motor.stopMotor();
    }

    /**
     * Performs one launch cycle, including recovery time.
     */
    public void launch() {

    }

    // region Subsystem Implementation

    @Override
    public void periodic() {
        // TODO: Change to command system.
        // TODO: Figure out why commands aren't dispatching
        this.input(this.controller.launchPower());

        this.telemetry.addLine("\nLauncher:");
        this.telemetry.addData("- Motor Power", this.motor.get());
    }

    // endregion
}
