package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import moe.seikimo.ftc.game.prompt.PromptManager;
import moe.seikimo.ftc.robot.states.MigrateState.StartingLocation;

@Autonomous(name = "Prompt Manager", group = "Test")
public final class TestPromptOpMode extends OpMode {
    private final PromptManager promptManager = new PromptManager();

    @Override
    public void init() {
        this.promptManager
            .add("start_1", "Primary Starting Location", StartingLocation.FRONT_LAUNCH)
            .add("start_2", "Secondary Starting Location", StartingLocation.BACK_LAUNCH);
    }

    @Override
    public void init_loop() {
        this.promptManager.render(this.gamepad1, this.telemetry);
    }

    @Override
    public void start() {
        this.telemetry.addData("Primary Starting Location", this.promptManager.getValue("start_1").name());
        this.telemetry.addData("Secondary Starting Location", this.promptManager.getValue("start_2").name());
        this.telemetry.update();
    }

    @Override
    public void loop() {

    }
}
