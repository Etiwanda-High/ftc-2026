package org.firstinspires.ftc.teamcode.game.v5;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.drivebase.MecanumDrive;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import lombok.val;
import lombok.var;
import moe.seikimo.ftc.Constants;

@TeleOp(name = "Test Drive", group = "V5")
public final class TestDriveOpMode extends OpMode {
    private MecanumDrive handle;
    private double maxSpeed = 0.8d;
    private boolean absoluteDrive = false;

    @Override
    public void init() {
        val hwMap = this.hardwareMap;
        val frontLeft = new Motor(hwMap, Constants.DRIVE_FRONT_LEFT);
        val frontRight = new Motor(hwMap, Constants.DRIVE_FRONT_RIGHT);
        val backLeft = new Motor(hwMap, Constants.DRIVE_BACK_LEFT);
        val backRight = new Motor(hwMap, Constants.DRIVE_BACK_RIGHT);
        this.handle = new MecanumDrive(false, frontLeft, frontRight, backLeft, backRight);
    }

    @Override
    public void loop() {
        // Change the speed according to the D-pad input.
        var modifier = 0d;
        if (this.gamepad1.dpadUpWasPressed()) {
            modifier = 0.1;
        } else if (this.gamepad1.dpadDownWasPressed()) {
            modifier = -0.1;
        }
        this.maxSpeed = Math.min(1.0, Math.max(0.0, this.maxSpeed + modifier));
        this.handle.setMaxSpeed(this.maxSpeed);

        // Set absolute drive mode according to the A button.
        if (this.gamepad1.aWasPressed()) {
            this.absoluteDrive = !this.absoluteDrive;
        }

        if (this.absoluteDrive) {
            // driveRobotCentric: Robot-centric assumes that each push of the joystick is in relation to the local position
            // of the robot—this means that whenever the user pushes the drive stick forward, the robot will drive in the
            // direction of its front-facing side.
            this.handle.driveRobotCentric(
                this.gamepad1.right_stick_x,
                -this.gamepad1.right_stick_y,
                this.gamepad1.left_stick_x
            );
        } else {
            // driveFieldCentric: Field-centric assumes that each push of the joystick is in relation to the global position
            // of the robot—this means that whenever the user pushes the drive stick forward, the robot will move away from
            // the driver no matter its orientation.
            this.handle.driveFieldCentric(
                this.gamepad1.right_stick_x,
                -this.gamepad1.right_stick_y,
                this.gamepad1.left_stick_x,
                0
            );
        }
    }
}
