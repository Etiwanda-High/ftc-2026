package org.firstinspires.ftc.teamcode.game;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import moe.seikimo.ftc.robot.Robot;

/**
 * As of v3, this class acts as a game manager.
 */
@TeleOp(name = "Tele-Op", group = "Game")
public final class V3TeleOpMode extends Robot {
    @Override
    protected OperationMode getOperationType() {
        return OperationMode.TELE_OP;
    }
}
