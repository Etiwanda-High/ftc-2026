package org.firstinspires.ftc.teamcode.game.v2;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import moe.seikimo.ftc.Constants;

@Autonomous(name = "V21 Auto", group = "Game")
public final class V21AutoOpMode extends OpMode {
    private final Timer timer = new Timer();
    private Follower follower;

    @Override
    public void init() {
        this.telemetry.update();

        this.follower = Constants.FOLLOWER_FACTORY.apply(this.hardwareMap);
        this.timer.resetTimer();
    }

    @Override
    public void init_loop() {
        this.telemetry.update();
    }

    @Override
    public void start() {
        this.timer.resetTimer();
        this.follower.startTeleOpDrive(true);
    }

    @Override
    public void loop() {
        if (this.timer.getElapsedTimeSeconds() < 5) {
            this.follower.setTeleOpDrive(1, 0, 0, false);
        } else {
            this.follower.setTeleOpDrive(0, 0, 0, false);
        }

        this.follower.update();
        this.telemetry.update();
    }

    @Override
    public void stop() {
        this.telemetry.update();
    }
}
