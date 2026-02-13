package org.firstinspires.ftc.teamcode.game.v4;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;
import lombok.val;
import moe.seikimo.ftc.Constants;

@TeleOp(name = "V4 Intake Demo", group = "V4")
public final class V4IntakeDemoTeleOpMode extends OpMode {
    private final static double LIMIT = 0.333;

    private Motor intakeMotor;

    private ServoEx kickLeft, kickRightTop, kickRightBottom;
    private boolean leftKicking, rightTopKicking, rightBottomKicking;

    @Override
    public void init() {
        this.intakeMotor = new Motor(this.hardwareMap, Constants.MOTOR_INTAKE_FRONT);

        this.kickLeft = new ServoEx(this.hardwareMap, Constants.SERVO_KICKER_LEFT);
        this.kickRightTop = new ServoEx(this.hardwareMap, Constants.SERVO_KICKER_RIGHT_TOP);
        this.kickRightBottom = new ServoEx(this.hardwareMap, Constants.SERVO_KICKER_RIGHT_BOTTOM);
    }

    @Override
    public void loop() {

        if (this.gamepad1.xWasPressed()) {
            this.leftKicking = !this.leftKicking;
        }
        if (this.gamepad1.yWasPressed()) {
            this.rightTopKicking = !this.rightTopKicking;
        }
        if (this.gamepad1.bWasPressed()) {
            this.rightBottomKicking = !this.rightBottomKicking;
        }

        val power = this.gamepad1.right_trigger;
        this.kickLeft.set(this.leftKicking ? LIMIT : 0);
        this.kickRightTop.set(this.rightTopKicking ? LIMIT : 0);
        this.kickRightBottom.set(this.rightBottomKicking ? LIMIT : 0);

        if (this.gamepad1.left_trigger > 0) {
            this.intakeMotor.set(this.gamepad1.left_trigger);
        } else {
            this.intakeMotor.set(this.gamepad1.left_bumper ? 1 : 0);
        }

        this.telemetry.addData("Intake Power", this.kickRightBottom.get());
        this.telemetry.addData("Intake Power 2", power);
    }
}
