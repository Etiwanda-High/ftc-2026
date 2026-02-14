package org.firstinspires.ftc.teamcode.game.v5;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.hardware.motors.MotorEx;
import lombok.val;
import moe.seikimo.ftc.Constants;

@TeleOp(name = "Test Launcher", group = "V5")
public final class TestLauncherOpMode extends OpMode {
    private MotorEx launcher;

    private double power = 0.25d;

    @Override
    public void init() {
        val hwMap = this.hardwareMap;
        this.launcher = new MotorEx(hwMap, Constants.MOTOR_LAUNCH);
    }

    @Override
    public void loop() {
        if (this.gamepad1.a) {
            this.launcher.set(this.power);
        } else {
            this.launcher.set(this.gamepad1.right_trigger);
        }

        if (this.gamepad1.dpadUpWasPressed()) {
            this.power = Math.min(1.0, this.power + 0.05);
        } else if (this.gamepad1.dpadDownWasPressed()) {
            this.power = Math.max(0.0, this.power - 0.05);
        }

        this.telemetry.addData("Launcher Power", this.launcher.get());
    }
}
