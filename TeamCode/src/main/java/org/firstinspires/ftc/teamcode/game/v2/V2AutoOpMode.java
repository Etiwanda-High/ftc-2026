package org.firstinspires.ftc.teamcode.game.v2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import moe.seikimo.ftc.game.GameManager;

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

        this.register(
            this.gameManager.getDrive()
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
