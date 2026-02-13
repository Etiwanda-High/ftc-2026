package org.firstinspires.ftc.teamcode.game.v4;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import moe.seikimo.ftc.Constants;

public final class V4DriveTeleOpMode extends OpMode {
    private Follower follower;
    private TelemetryManager panels;

    @Override
    public void init() {
        this.panels = PanelsTelemetry.INSTANCE.getTelemetry();

        this.follower = Constants.FOLLOWER_FACTORY.apply(this.hardwareMap);
        this.follower.update();
    }

    @Override
    public void start() {
        this.follower.startTeleOpDrive(true);
    }

    @Override
    public void loop() {
        this.follower.update();
        this.panels.update();

        this.follower.setTeleOpDrive(
            -this.gamepad1.left_stick_y,
            -this.gamepad1.left_stick_x,
            -this.gamepad1.right_stick_x,
            true
        );
    }
}
