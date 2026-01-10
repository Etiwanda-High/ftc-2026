package org.firstinspires.ftc.teamcode.game.v4;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import moe.seikimo.ftc.Constants;

@TeleOp(name = "V4 Test TeleOp", group = "V4")
public final class V4TestTeleOpMode extends OpMode {
    private Motor frontLeft, frontRight, backLeft, backRight;

    private double startTime = 0;

    /** Time in milliseconds. */
    private static final long TIME = 192;

    @Override
    public void init() {
        this.frontLeft = new Motor(this.hardwareMap, Constants.DRIVE_FRONT_LEFT, Motor.GoBILDA.RPM_312);
        this.frontLeft.setRunMode(Motor.RunMode.RawPower);
        this.frontLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.frontLeft.setInverted(true);

        this.frontRight = new Motor(this.hardwareMap, Constants.DRIVE_FRONT_RIGHT, Motor.GoBILDA.RPM_312);
        this.frontRight.setRunMode(Motor.RunMode.RawPower);
        this.frontRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        this.backLeft = new Motor(this.hardwareMap, Constants.DRIVE_BACK_LEFT, Motor.GoBILDA.RPM_312);
        this.backLeft.setRunMode(Motor.RunMode.RawPower);
        this.backLeft.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        this.backLeft.setInverted(true);

        this.backRight = new Motor(this.hardwareMap, Constants.DRIVE_BACK_RIGHT, Motor.GoBILDA.RPM_312);
        this.backRight.setRunMode(Motor.RunMode.RawPower);
        this.backRight.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
    }

    @Override
    public void start() {
        this.backRight.set(1);
        this.backLeft.set(1);
        this.frontRight.set(1);
        this.frontLeft.set(1);

        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void loop() {
        if (System.currentTimeMillis() > this.startTime + TIME) {
            this.frontLeft.stopMotor();
            this.frontRight.stopMotor();
            this.backLeft.stopMotor();
            this.backRight.stopMotor();
        }
    }
}
