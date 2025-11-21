package moe.seikimo.legacy.game.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.geometry.Pose2d;
import moe.seikimo.legacy.game.LegacyGameManager;
import moe.seikimo.legacy.robot.v2.DriveSystem;

public final class DriveToCommand extends CommandBase {
    private final DriveSystem drive;
    private final Pose2d target;

    /**
     * Game manager constructor for the drive to command.
     *
     * @param gameManager The game manager to use for getting subsystems.
     * @param target The target pose to drive to.
     */
    public DriveToCommand(LegacyGameManager gameManager, Pose2d target) {
        this(gameManager.getDrive(), target);
    }

    /**
     * Creates a new instance of the drive to command.
     *
     * @param drive The drive system to control.
     * @param target The target pose to drive to.
     */
    public DriveToCommand(DriveSystem drive, Pose2d target) {
        this.drive = drive;
        this.target = target;

        this.addRequirements(drive);
    }

    @Override
    public void execute() {

    }
}
