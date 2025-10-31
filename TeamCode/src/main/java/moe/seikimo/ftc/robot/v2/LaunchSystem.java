package moe.seikimo.ftc.robot.v2;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import lombok.Setter;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.game.GameManager;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class LaunchSystem extends SubsystemBase {
    private final GameManager gameManager;
    private final Telemetry telemetry;

    private final Motor motor;

    /** State flag for whether the motor is running. */
    private boolean running = false;
    /** State flag for whether the motor is reversed. */
    @Setter
    private boolean reverse = false;
    /** The speed the motor should be going. */
    private double targetSpeed = 1f;

    /**
     * Creates a new instance of the launcher system.
     *
     * @param gameManager Game manager to use for initializing motors.
     */
    public LaunchSystem(GameManager gameManager) {
        this.gameManager = gameManager;
        this.telemetry = gameManager.getTelemetry();

        this.motor = new Motor(gameManager.getHwMap(), Constants.MOTOR_LAUNCH);
        this.motor.setInverted(true);
    }

    // region Setters

    /** Sets the motor (toggle) speed to the close constant. */
    public void speedClose() {
        this.targetSpeed = Constants.LAUNCH_POWER_CLOSE;
    }

    /** Sets the motor (toggle) speed to the far constant. */
    public void speedFar() {
        this.targetSpeed = Constants.LAUNCH_POWER_FAR;
    }

    /** Incrementally increases the motor speed. */
    public void speedUp() {
        this.targetSpeed = Math.min(1, this.targetSpeed + 0.05);
    }

    /** Incrementally decreases the motor speed. */
    public void speedDown() {
        this.targetSpeed = Math.max(0, this.targetSpeed - 0.05);
    }

    // endregion

    // region Toggles

    /** Toggles the state of the motor. */
    public void toggle() {
        this.running = !this.running;
        this.reverse = false;
    }

    // endregion

    // region Subsystem Implementation

    @Override
    public void periodic() {
        this.motor.set(this.running ? (
            this.reverse ? -this.targetSpeed : this.targetSpeed
            ) : 0);

        this.telemetry.addLine("\nLauncher:");
        this.telemetry.addData("- Target Speed", this.targetSpeed);
        this.telemetry.addData("- Motor Power", this.motor.get());
    }

    // endregion
}
