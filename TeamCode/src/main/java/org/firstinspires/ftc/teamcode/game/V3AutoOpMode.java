package org.firstinspires.ftc.teamcode.game;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import lombok.val;
import moe.seikimo.ftc.robot.Robot;
import moe.seikimo.ftc.robot.states.MigrateState.StartingLocation;

/**
 * As of v3, this class acts as a game manager.
 */
@Autonomous(name = "Autonomous", group = "Game")
public final class V3AutoOpMode extends Robot {
    public V3AutoOpMode() {
        // Add prompt definitions here if needed.
        val pm = this.getPromptManager();
        pm
            .add("start_loc", "Starting Location", StartingLocation.BACK_LAUNCH);
    }

    @Override
    protected OperationMode getOperationType() {
        return OperationMode.AUTONOMOUS;
    }
}
