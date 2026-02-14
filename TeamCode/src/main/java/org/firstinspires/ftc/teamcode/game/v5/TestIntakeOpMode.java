package org.firstinspires.ftc.teamcode.game.v5;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import lombok.val;
import moe.seikimo.ftc.Constants;

@TeleOp(name = "Test Intake", group = "V5")
public final class TestIntakeOpMode extends OpMode {
    private MotorEx front, back;
    private boolean frontReversed = false, backReversed = true;
    private double power = 1d;

    @Override
    public void init() {
        val hwMap = this.hardwareMap;
        this.front = new MotorEx(hwMap, Constants.MOTOR_INTAKE_FRONT);
        this.back = new MotorEx(hwMap, Constants.MOTOR_INTAKE_BACK);
    }

    @Override
    public void loop() {
        if (this.gamepad1.a) {
            this.front.set(this.frontReversed ? -this.power : this.power);
            this.back.set(this.backReversed ? -this.power : this.power);
        } else {
            this.front.set(0.0);
            this.back.set(0.0);
        }

        if (this.gamepad1.xWasPressed()) {
            this.frontReversed = !this.frontReversed;
        }
        if (this.gamepad1.yWasPressed()) {
            this.backReversed = !this.backReversed;
        }

        if (this.gamepad1.dpadUpWasPressed()) {
            this.power = Math.min(1.0, this.power + 0.05);
        } else if (this.gamepad1.dpadDownWasPressed()) {
            this.power = Math.max(0.0, this.power - 0.05);
        }

        this.telemetry.addData("Front Intake", "%s (reversed: %s)", this.front.get(), this.frontReversed);
        this.telemetry.addData("Back Intake", "%s (reversed: %s)", this.back.get(), this.backReversed);
        this.telemetry.addData("Power", this.power);
    }
}
