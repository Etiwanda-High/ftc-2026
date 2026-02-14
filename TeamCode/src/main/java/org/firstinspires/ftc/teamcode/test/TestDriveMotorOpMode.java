package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import moe.seikimo.ftc.Constants;

@TeleOp(name = "Test Drive Motor", group = "Test")
public final class TestDriveMotorOpMode extends OpMode {
    private DcMotor leftFront, leftBack, rightFront, rightBack;
    private boolean lfReversed, lbReversed, rfReversed = true, rbReversed = true;

    private double power = 0.5;

    @Override
    public void init() {
        this.leftFront = this.hardwareMap.get(DcMotor.class, Constants.DRIVE_FRONT_LEFT);
        this.leftBack = this.hardwareMap.get(DcMotor.class, Constants.DRIVE_BACK_LEFT);
        this.rightFront = this.hardwareMap.get(DcMotor.class, Constants.DRIVE_FRONT_RIGHT);
        this.rightBack = this.hardwareMap.get(DcMotor.class, Constants.DRIVE_BACK_RIGHT);
    }

    @Override
    public void loop() {
        this.leftFront.setDirection(this.lfReversed ? Direction.REVERSE : Direction.FORWARD);
        this.leftBack.setDirection(this.lbReversed ? Direction.REVERSE : Direction.FORWARD);
        this.rightFront.setDirection(this.rfReversed ? Direction.REVERSE : Direction.FORWARD);
        this.rightBack.setDirection(this.rbReversed ? Direction.REVERSE : Direction.FORWARD);

        if (this.gamepad1.dpadUpWasPressed()) {
            this.power += 0.05;
        } else if (this.gamepad1.dpadDownWasPressed()) {
            this.power -= 0.05;
        }

        if (!this.gamepad1.left_bumper) {
            this.leftFront.setPower(this.gamepad1.a ? this.power : 0);
            this.leftBack.setPower(this.gamepad1.b ? this.power : 0);
            this.rightFront.setPower(this.gamepad1.x ? this.power : 0);
            this.rightBack.setPower(this.gamepad1.y ? this.power : 0);
        } else {
            if (this.gamepad1.aWasPressed()) {
                this.lfReversed = !this.lfReversed;
            }
            if (this.gamepad1.bWasPressed()) {
                this.lbReversed = !this.lbReversed;
            }
            if (this.gamepad1.xWasPressed()) {
                this.rfReversed = !this.rfReversed;
            }
            if (this.gamepad1.yWasPressed()) {
                this.rbReversed = !this.rbReversed;
            }
        }

        this.telemetry.addData("Power", this.power);
        this.telemetry.addLine();
        this.telemetry.addData("Left Front Reversed", this.lfReversed);
        this.telemetry.addData("Left Back Reversed", this.lbReversed);
        this.telemetry.addData("Right Front Reversed", this.rfReversed);
        this.telemetry.addData("Right Back Reversed", this.rbReversed);
    }
}
