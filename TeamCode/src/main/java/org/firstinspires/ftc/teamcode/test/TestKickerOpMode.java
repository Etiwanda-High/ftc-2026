package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;
import lombok.val;
import moe.seikimo.ftc.Constants;

@TeleOp(name = "Test Kicker", group = "Test")
public final class TestKickerOpMode extends OpMode {
    private ServoEx top, middle, back;

    private boolean isMutable = false;
    private boolean setTop, setMiddle, setBack;

    @Override
    public void init() {
        val hwMap = this.hardwareMap;
        this.top = new ServoEx(hwMap, Constants.SERVO_TOP);
        this.middle = new ServoEx(hwMap, Constants.SERVO_MIDDLE);
        this.back = new ServoEx(hwMap, Constants.SERVO_BOTTOM);
    }

    @Override
    public void loop() {
        if (this.gamepad1.aWasPressed()) {
            this.isMutable = !this.isMutable;
            this.setTop = false;
            this.setMiddle = false;
            this.setBack = false;
        }

        if (this.gamepad1.xWasPressed()) {
            this.setTop = true;
            this.setMiddle = false;
            this.setBack = false;
        } else if (this.gamepad1.yWasPressed()) {
            this.setTop = false;
            this.setMiddle = true;
            this.setBack = false;
        } else if (this.gamepad1.bWasPressed()) {
            this.setTop = false;
            this.setMiddle = false;
            this.setBack = true;
        }

        if (this.isMutable) {
            if (this.setTop) {
                this.top.set(this.gamepad1.right_trigger);
            } else if (this.setMiddle) {
                this.middle.set(this.gamepad1.right_trigger);
            } else if (this.setBack) {
                this.back.set(this.gamepad1.right_trigger);
            }
        }

        this.telemetry.addData("Mutable", this.isMutable);
        this.telemetry.addData("Top", this.top.get());
        this.telemetry.addData("Middle", this.middle.get());
        this.telemetry.addData("Back", this.back.get());
    }
}
