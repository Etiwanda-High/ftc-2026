package org.firstinspires.ftc.teamcode.game.v2;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.*;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.game.commands.RelativeDriveCommand;

@TeleOp(name = "V2 TeleOp", group = "Game")
public final class V2TeleOpMode extends OpMode {
    private GameManager gameManager;

    // region Command System

    /**
     * Schedules {@link Command} objects to the scheduler
     */
    private void schedule(Command... commands) {
        CommandScheduler.getInstance().schedule(commands);
    }

    /**
     * Registers {@link Subsystem} objects to the scheduler
     */
    private void register(Subsystem... subsystems) {
        CommandScheduler.getInstance().registerSubsystem(subsystems);
    }

    // endregion

    // region OpMode Implementation

    @Override
    public void init() {
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
    public void init_loop() {
        this.gameManager.preUpdate();
        this.telemetry.update();
    }

    @Override
    public void start() {
        this.gameManager.start();
    }

    @Override
    public void loop() {
        this.gameManager.update();
        CommandScheduler.getInstance().run();
        this.telemetry.update();
    }

    @Override
    public void stop() {
        this.gameManager.destroy();
        this.telemetry.update();
    }

    // endregion
}
