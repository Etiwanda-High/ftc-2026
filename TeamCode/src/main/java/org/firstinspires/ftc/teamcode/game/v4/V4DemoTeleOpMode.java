package org.firstinspires.ftc.teamcode.game.v4;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import moe.seikimo.ftc.Constants;

public final class V4DemoTeleOpMode extends OpMode {
    private MecanumDrive drive;

    private float speed = 0.4f;

    @Override
    public void init() {
        this.drive = new MecanumDrive(
            new Motor(this.hardwareMap, Constants.DRIVE_FRONT_LEFT),
            new Motor(this.hardwareMap, Constants.DRIVE_FRONT_RIGHT),
            new Motor(this.hardwareMap, Constants.DRIVE_BACK_LEFT),
            new Motor(this.hardwareMap, Constants.DRIVE_BACK_RIGHT)
        );
    }

    @Override
    public void start() {
        this.drive.setMaxSpeed(this.speed);
    }

    @Override
    public void loop() {
        if (this.gamepad1.dpadUpWasPressed()) {
            this.speed = Math.min(1, this.speed + 0.1f);
            this.drive.setMaxSpeed(this.speed);
        }
        if (this.gamepad1.dpadDownWasPressed()) {
            this.speed = Math.max(0, this.speed - 0.1f);
            this.drive.setMaxSpeed(this.speed);
        }

        this.drive.driveRobotCentric(
            -this.gamepad1.left_stick_x,
            this.gamepad1.left_stick_y,
            -this.gamepad1.right_stick_x
        );
    }
}
