package moe.seikimo.ftc.robot.managers;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import lombok.Getter;
import lombok.val;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.annotations.fields.Hardware;
import moe.seikimo.ftc.annotations.types.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;

@Getter
@RobotSystem
@Configurable
public final class IntakeSystem implements MonoBehaviour {
    public static boolean FRONT_MOTOR_REVERSED = false, BACK_MOTOR_REVERSED = false;

    @Hardware(Constants.MOTOR_INTAKE_FRONT)
    private DcMotor front;

    /* Intake motor should spin in reverse. (negative) */
    @Hardware(Constants.MOTOR_INTAKE_BACK)
    private DcMotor back;

    private boolean running = false;
    private double motorSpeed = 1d;

    /**
     * Sets the intake running state.
     *
     * @param running Whether the intake should be running.
     */
    public void setRunning(boolean running) {
        this.running = running;

        val frontPower = this.running ? this.motorSpeed : 0.0;
        this.front.setPower(frontPower * (FRONT_MOTOR_REVERSED ? -1 : 1));

        val backPower = this.running ? this.motorSpeed : 0.0;
        this.back.setPower(backPower * (BACK_MOTOR_REVERSED ? -1 : 1));
    }

    /**
     * Sets the intake motor speed.
     * This will update the motor powers if the intake is currently running.
     *
     * @param speed The new speed for the intake motors (0.0 to 1.0).
     */
    public void setMotorSpeed(double speed) {
        this.motorSpeed = speed;
        if (this.running) {
            this.setRunning(true); // Update motor powers with new speed
        }
    }
}
