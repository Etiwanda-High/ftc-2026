package moe.seikimo.ftc.utils;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import kotlin.UninitializedPropertyAccessException;
import lombok.val;
import moe.seikimo.ftc.robot.Robot;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * General-purpose robot logger.
 */
public final class RobotLogger implements Logger {
    private final Telemetry telemetry;
    private final TelemetryManager panels;

    /**
     * Creates a new instance of the robot logger.
     *
     * @param robot The robot to log for.
     */
    public RobotLogger(Robot robot) {
        this.telemetry = robot.telemetry;
        this.panels = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    @Override
    public void push() {
        this.telemetry.update();

        try {
            this.panels.update();
        } catch (UninitializedPropertyAccessException ignored) {
            // ignored
        }
    }

    @Override
    public Logger section(String title) {
        this.telemetry.addLine("\n" + title);
        return this;
    }

    @Override
    public Logger line(String message) {
        this.telemetry.addLine(message);
        return this;
    }

    @Override
    public Logger log(String caption, String message, Object... args) {
        if (args.length == 0) {
            this.telemetry.addData(caption, message);
            this.panels.debug(String.format("%s: %s", caption, message));
        } else {
            val formatted = String.format(message, args);
            this.telemetry.addData(caption, formatted);
            this.panels.debug(String.format("%s: %s", caption, formatted));
        }

        return this;
    }
}
