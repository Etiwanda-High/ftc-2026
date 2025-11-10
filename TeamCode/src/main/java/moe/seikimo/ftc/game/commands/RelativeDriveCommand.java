package moe.seikimo.ftc.game.commands;

import com.seattlesolvers.solverslib.command.CommandBase;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.game.PlayerController;
import moe.seikimo.ftc.robot.v2.DriveSystem;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.function.DoubleSupplier;

public final class RelativeDriveCommand extends CommandBase {
    private final Telemetry telemetry;

    private final DriveSystem drive;
    private final DoubleSupplier translateX, translateY, rotate;

    /**
     * Game manager constructor for the relative drive command.
     *
     * @param gameManager The game manager to use for getting subsystems.
     */
    public RelativeDriveCommand(GameManager gameManager) {
        this(gameManager.getTelemetry(), gameManager.getDrive(), gameManager.getDriver());
    }

    /**
     * Creates a new instance of the relative drive command.
     *
     * @param telemetry The telemetry to use for debugging.
     * @param drive The drive system to control.
     * @param controller The player controller to read inputs from.
     */
    public RelativeDriveCommand(Telemetry telemetry, DriveSystem drive, PlayerController controller) {
        this.telemetry = telemetry;
        this.drive = drive;
        this.translateX = controller::translateX;
        this.translateY = controller::translateY;
        this.rotate = controller::rotate;

        this.addRequirements(drive);
    }

    @Override
    public void execute() {
        this.telemetry.addLine("\nController:");
        this.telemetry.addData("- Translate X", this.translateX.getAsDouble());
        this.telemetry.addData("- Translate Y", this.translateY.getAsDouble());
        this.telemetry.addData("- Rotate", this.rotate.getAsDouble());

//        this.drive.input(
//            this.translateX.getAsDouble(),
//            this.translateY.getAsDouble(),
//            -this.rotate.getAsDouble()
//        );
//        this.drive.update();
    }
}
