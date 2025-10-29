package org.firstinspires.ftc.teamcode.game;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.robot.v2.DriveSystem;

@Autonomous(name = "V2 Auto", group = "Game")
public final class V2AutoOpMode extends CommandOpMode {
    private GameManager gameManager;

    // region OpMode Implementation

    @Override
    public void initialize() {
        this.gameManager = new GameManager(
            this.telemetry, this.hardwareMap, this.gamepad1, this.gamepad2);

        this.gameManager.awake();
        this.telemetry.update();

        this.schedule(
            new SequentialCommandGroup()
        );

        this.register(
            new DriveSystem(this.gameManager)
        );
    }

    @Override
    public void initialize_loop() {
        this.gameManager.preUpdate();
        this.telemetry.update();
    }

    @Override
    public void run() {
        this.gameManager.update();
        super.run();
        this.telemetry.update();
    }

    @Override
    public void end() {
        this.gameManager.destroy();
        this.telemetry.update();
    }

    // endregion
}
