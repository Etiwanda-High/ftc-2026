package moe.seikimo.ftc.robot.managers;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.annotations.fields.Hardware;
import moe.seikimo.ftc.annotations.types.RobotSystem;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.utils.Logger;

@RobotSystem
@Configurable
@RequiredArgsConstructor
public final class LauncherSystem implements MonoBehaviour {
    public static boolean REVERSE = false;
    public static double FEEDFORWARD = 0, PROPORTIONAL = 0;

    public static double VELOCITY_CLOSE = 0.7;
    public static double VELOCITY_FAR = 0.79;

    /// ---------------------------------------------------------------------- \\\

    private final Logger logger;

    @Hardware(Constants.MOTOR_LAUNCH)
    private DcMotorEx motor;

    private double targetVelocity = 0;

    /**
     * Updates the PIDF coefficients of the launcher motor using the current values of the static fields.
     */
    public void updateConstants() {
        val pidf = new PIDFCoefficients(PROPORTIONAL, 0, 0, FEEDFORWARD);
        this.motor.setPIDFCoefficients(RunMode.RUN_USING_ENCODER, pidf);
    }

    /**
     * Sets the velocity of the launcher motor.
     *
     * @param velocity The target velocity for the launcher motor in ticks per second.
     */
    public void setVelocity(double velocity) {
        this.targetVelocity = velocity;
        this.motor.setVelocity(velocity);
    }

    /**
     * Sets the launcher motor to a preset velocity.
     *
     * @param preset The preset to set the launcher motor to.
     */
    public void setVelocity(Preset preset) {
        this.setVelocity(preset.power);
    }

    @Override
    public void awake() {
        this.motor.setMode(RunMode.RUN_USING_ENCODER);
        this.motor.setDirection(REVERSE ? Direction.REVERSE : Direction.FORWARD);

        this.updateConstants();
    }

    @Override
    public void update() {
        val current = this.motor.getVelocity();
        val error = this.targetVelocity - current;

        this.logger
            .section("Launcher System")
            .log("Target Velocity", "%.2f tps", this.targetVelocity)
            .log("Current Velocity", "%.2f tps", current)
            .log("Velocity Error", "%.2f tps", error);
    }

    @Getter
    @RequiredArgsConstructor
    public enum Preset {
        CLOSE(VELOCITY_CLOSE),
        FAR(VELOCITY_FAR);

        private final double power;
    }
}
