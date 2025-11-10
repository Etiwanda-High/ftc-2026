package moe.seikimo.ftc.robot.v2;

import com.seattlesolvers.solverslib.command.SubsystemBase;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;
import lombok.Getter;
import lombok.Setter;
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

    private final ServoEx nuh, uh;

    /** Controls whether the intake is operating or not. */
    private boolean running = false;
    /** Flag for the gate's open state. */
    private boolean gateOpen = false;
    /** The speed to operate the motor at. */
    private double speed = 1f;

    @Setter @Getter
    private boolean useNuh = true;

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

        this.nuh = new ServoEx(hwMap, Constants.SERVO_NUH);
        this.uh = new ServoEx(hwMap, Constants.SERVO_UH);
        this.uh.setInverted(true);
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

    /** Toggles the ball intake gate. */
    public void toggleGate() {
        if (this.gateOpen) {
            this.closeGate();
        } else {
            this.openGate();
        }
        this.gateOpen = !this.gateOpen;
    }

    /** Opens the ball intake gate. */
    public void openGate() {
        this.nuh.set(Constants.SERVO_DOOR_OPENED);
        this.uh.set(Constants.SERVO_DOOR_OPENED);
    }

    /** Closes the ball intake gate. */
    public void closeGate() {
        this.nuh.set(Constants.SERVO_DOOR_CLOSED);
        this.uh.set(Constants.SERVO_DOOR_CLOSED);
    }

    // region Subsystem Implementation

    @Override
    public void periodic() {
        this.input(
            this.controller.intakePower() - this.controller.intakeReverse()
        );

        this.telemetry.addLine("\nIntake:");
        this.telemetry.addData("- Motor Power", this.motor.get());
        this.telemetry.addData("- Max Speed", this.speed);
        this.telemetry.addData("- Auto Running?", this.running);
        this.telemetry.addLine();
        this.telemetry.addData("- Gate Open?", this.gateOpen);
        this.telemetry.addData("- Nuh Position", this.nuh.get());
        this.telemetry.addData("- Uh Position", this.uh.get());
    }

    // endregion
}
