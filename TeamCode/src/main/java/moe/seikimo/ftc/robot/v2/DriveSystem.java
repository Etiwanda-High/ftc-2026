package moe.seikimo.ftc.robot.v2;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.seattlesolvers.solverslib.command.SubsystemBase;
import lombok.val;
import moe.seikimo.ftc.Constants;
import moe.seikimo.ftc.game.GameManager;
import moe.seikimo.ftc.game.MonoBehaviour;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public final class DriveSystem extends SubsystemBase implements MonoBehaviour {
    private final GameManager gameManager;
    private final Telemetry telemetry;

    private final Follower follower;

    // region Settings

    /**
     * The maximum speed of the drivetrain (0.0 - 1.0).
     */
    private double maxSpeed = 1.0;

    // endregion

    /**
     * Creates a new instance of the mecanum drivetrain.
     *
     * @param gameManager Game manager to use for initializing motors.
     */
    public DriveSystem(GameManager gameManager) {
        this.gameManager = gameManager;
        this.telemetry = gameManager.getTelemetry();

        val hwMap = gameManager.getHwMap();
        this.follower = Constants.FOLLOWER_FACTORY.apply(hwMap);

        this.follower.setPose(new Pose());
    }

    /**
     * Inputs movement commands to the drivetrain.
     *
     * @param translateX The translation in the X direction.
     * @param translateY The translation in the Y direction.
     * @param rotate The rotation amount.
     */
    public void input(double translateX, double translateY, double rotate) {
        this.follower.setTeleOpDrive(translateY, translateX, rotate, true);
    }

    // region Accessors

    /** Simple accessor to increase drive speed. */
    public void increaseSpeed() {
        this.maxSpeed = Math.min(1.0, this.maxSpeed + 0.1);
        this.follower.setMaxPower(this.maxSpeed);
    }

    /** Simple accessor to decrease drive speed. */
    public void decreaseSpeed() {
        this.maxSpeed = Math.max(0.0, this.maxSpeed - 0.1);
        this.follower.setMaxPower(this.maxSpeed);
    }

    // endregion

    // region MonoBehavior & Subsystem Implementation

    @Override
    public void start() {
        // TODO: Replace SolversLib mecanum drive with Pedro Pathing drive.
        this.follower.startTeleOpDrive(true);
    }

    @Override
    public void periodic() {
        // this.follower.update();

        this.telemetry.addLine("\nDrivetrain:");
        this.telemetry.addData("- Max Speed", this.maxSpeed);
    }

    @Override
    public void update() {
        this.follower.update();
    }

    // endregion
}
