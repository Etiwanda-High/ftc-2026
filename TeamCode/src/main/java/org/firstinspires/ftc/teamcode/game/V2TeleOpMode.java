package org.firstinspires.ftc.teamcode.game;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.PerpetualCommand;
import com.seattlesolvers.solverslib.command.Robot;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.game.commands.RelativeDriveCommand;

@TeleOp(name = "V2 TeleOp", group = "Game")
public final class V2TeleOpMode extends CommandOpMode {
    private GameManager gameManager;

    // region OpMode Implementation

    @Override
    public void initialize() {
        Robot.enable();

        this.gameManager = new GameManager(
            this.telemetry, this.hardwareMap, this.gamepad1, this.gamepad2);

        this.gameManager.awake();
        this.telemetry.update();

        this.schedule(
            new PerpetualCommand(new RelativeDriveCommand(this.gameManager))
        );

        this.register(
            this.gameManager.getDrive(),
            this.gameManager.getLocale(),
            this.gameManager.getLaunch(),
            this.gameManager.getIntake()
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
