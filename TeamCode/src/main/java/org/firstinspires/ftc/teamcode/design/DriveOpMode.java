package org.firstinspires.ftc.teamcode.design;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.drivebase.DifferentialDrive;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.Motor.RunMode;
import lombok.val;

@TeleOp(name = "Basic Drive", group = "Design")
public final class DriveOpMode extends LinearOpMode {
    private GamepadEx player1;
    private Motor shootOne, shootTwo;
    private Motor leftDrive, rightDrive;
    private DifferentialDrive drive;

    /**
     * Runs when the op mode is first initialized.
     */
    private void awake() {
        // Get controller.
        this.player1 = new GamepadEx(this.gamepad1);

        // Configure motors.
        this.leftDrive = new Motor(this.hardwareMap, "left_drive");
        this.rightDrive = new Motor(this.hardwareMap, "right_drive");

        this.shootOne = new Motor(this.hardwareMap, "shooter_one");
        this.shootTwo = new Motor(this.hardwareMap, "shooter_two");

        // Set motors.
        this.shootOne.setRunMode(RunMode.RawPower);
        this.shootTwo.setRunMode(RunMode.RawPower);
        this.drive = new DifferentialDrive(this.leftDrive, this.rightDrive);
        this.drive.setMaxSpeed(0.5);

        this.telemetry.addData("Status", "Initialized");
    }

    /**
     * Runs repeatedly while the op mode is active.
     */
    private void update() {
        if (this.gamepad1.a || this.gamepad1.b) {
            val value = this.gamepad1.a ? 1 : -1;
            this.shootOne.set(value);
            this.shootTwo.set(-value);
        } else {
            this.shootOne.set(0);
            this.shootTwo.set(0);
        }

        this.drive.arcadeDrive(this.player1.getLeftY(), -this.player1.getRightX());

        this.telemetry.addData("Status", "Running");
        this.telemetry.addData("Test", "Hello world!");
    }

    @Override
    public void runOpMode() {
        this.awake();
        this.telemetry.update();

        this.waitForStart();
        this.resetRuntime();

        while (this.opModeIsActive()) {
            this.update();
            this.telemetry.update();
        }
    }
}
