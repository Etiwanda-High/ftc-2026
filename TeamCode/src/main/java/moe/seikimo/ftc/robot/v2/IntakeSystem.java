package moe.seikimo.ftc.robot.v2;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import lombok.val;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.game.PlayerController;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class IntakeSystem extends SubsystemBase {
    private final GameManager gameManager;
    private final Telemetry telemetry;

    private final PlayerController controller;
    private final Motor motor;

    /** Controls whether the intake is operating or not. */
    private boolean running = false;
    /** The speed to operate the motor at. */
    private double speed = 1f;

    /**
     * Creates a new instance of the intake system.
     *
     * @param gameManager Game manager to use for initializing motors.
     */
    public IntakeSystem(GameManager gameManager) {
        this.gameManager = gameManager;
        this.telemetry = gameManager.getTelemetry();

        this.controller = gameManager.getController();

        val hwMap = gameManager.getHwMap();
        this.motor = new Motor(hwMap, Constants.MOTOR_INTAKE);
    }

    /**
     * Directly sets the power of the motor.
     *
     * @param power The motor power.
     */
    public void input(double power) {
        if (this.running) return;
        this.motor.set(power);
    }

    /** Simple accessor to increase speed. */
    public void increaseSpeed() {
        this.speed = Math.min(1.0, this.speed + 0.1);
    }

    /** Simple accessor to decrease speed. */
    public void decreaseSpeed() {
        this.speed = Math.min(0.0, this.speed - 0.1);
    }

    /** Toggles the intake. */
    public void toggle() {
        if (this.running) {
            this.stop();
        } else {
            this.start();
        }
    }

    /** Starts the intake system. */
    public void start() {
        this.running = true;
        this.motor.set(this.speed);
    }

    /** Reverses the intake system. */
    public void reverse() {
        this.running = true;
        this.motor.set(-this.speed);
    }

    /** Stops the intake system. */
    public void stop() {
        this.running = false;
        this.motor.stopMotor();
    }

    // region Subsystem Implementation

    @Override
    public void periodic() {
        this.input(this.controller.intakePower());

        this.telemetry.addLine("\nIntake:");
        this.telemetry.addData("- Motor Power", this.motor.get());
        this.telemetry.addData("- Max Speed", this.speed);
        this.telemetry.addData("- Auto Running?", this.running);
    }

    // endregion
}
