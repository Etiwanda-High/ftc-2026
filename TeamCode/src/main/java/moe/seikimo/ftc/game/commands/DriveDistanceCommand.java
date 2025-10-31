package moe.seikimo.ftc.game.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.robot.v2.DriveSystem;

public final class DriveDistanceCommand extends CommandBase {
    private final DriveSystem drive;
    private final double x, y;

    /**
     * Drives a distance.
     *
     * @param gameManager The game manager.
     */
    public DriveDistanceCommand(GameManager gameManager, double x, double y) {
        this(gameManager.getDrive(), x, y);
    }

    /**
     * Drives a distance.
     *
     * @param drive The drive system.
     * @param x The distance to translate in the X.
     * @param y The distance to translate in the Y.
     */
    public DriveDistanceCommand(DriveSystem drive, double x, double y) {
        this.drive = drive;
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        this.drive.input(this.x, this.y, 0);
    }
}
