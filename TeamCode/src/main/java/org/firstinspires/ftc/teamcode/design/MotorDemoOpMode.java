package org.firstinspires.ftc.teamcode.design;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.hardware.motors.Motor.RunMode;
import com.seattlesolvers.solverslib.hardware.servos.ServoEx;

@TeleOp(name = "Motor Demo", group = "Design")
public final class MotorDemoOpMode extends LinearOpMode {
    private GamepadEx player1;

    private Motor motor1, motor2;
    private ServoEx servo1;

    /**
     * Runs when the op mode is first initialized.
     */
    private void awake() {
        // Get controller.
        this.player1 = new GamepadEx(this.gamepad1);

        // Configure motors.
        this.motor1 = new Motor(this.hardwareMap, "motor1");
        this.motor2 = new Motor(this.hardwareMap, "motor2");
        this.servo1 = new ServoEx(this.hardwareMap, "servo1", 0, 270);

        // Set motors.
        this.motor1.setRunMode(RunMode.RawPower);
        this.motor2.setRunMode(RunMode.RawPower);

        this.telemetry.addData("Status", "Initialized");
    }

    /**
     * Runs repeatedly while the op mode is active.
     */
    private void update() {
        this.motor1.set(this.gamepad1.left_trigger);
        this.motor2.set(this.gamepad1.right_trigger);

        if (this.player1.getButton(Button.DPAD_LEFT)) {
            this.servo1.set(0);
        } else if (this.player1.getButton(Button.DPAD_UP)) {
            this.servo1.set(180);
        } else if (this.player1.getButton(Button.DPAD_RIGHT)) {
            this.servo1.set(270);
        }

        this.telemetry.addData("Status", "Running");
        this.telemetry.addData("Motor 1 Power", this.motor1.get());
        this.telemetry.addData("Motor 2 Power", this.motor2.get());
        this.telemetry.addData("Servo 1 Position", this.servo1.get());
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
