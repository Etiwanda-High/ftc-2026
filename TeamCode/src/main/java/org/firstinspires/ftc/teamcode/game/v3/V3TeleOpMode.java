package org.firstinspires.ftc.teamcode.game.v3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import moe.seikimo.ftc.robot.Robot;

/**
 * As of v3, this class acts as a game manager.
 */
@Autonomous(name = "V3 Tele-Op", group = "Game")
public final class V3TeleOpMode extends Robot {
    @Override
    protected OperationMode getOperationType() {
        return OperationMode.TELE_OP;
    }
}
