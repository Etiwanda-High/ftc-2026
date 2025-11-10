package org.firstinspires.ftc.teamcode.game.v3;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import kotlin.UninitializedPropertyAccessException;
import lombok.Getter;
import lombok.val;
import moe.seikimo.ftc.Discoverable;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.robot.v3.RobotV1;
import moe.seikimo.ftc.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * As of v3, this class acts as a game manager.x
 */
@TeleOp(name = "V3 TeleOp", group = "Game")
public final class V3TeleOpMode extends OpMode implements Logger, Discoverable {
    @Getter private final Map<Class<?>, MonoBehaviour> systems = new HashMap<>();

    private final TelemetryManager panels = PanelsTelemetry.INSTANCE.getTelemetry();

    // region Discoverable Implementation

    @Override
    public HardwareMap getHardwareMap() {
        return this.hardwareMap;
    }

    @Override
    public Logger getLogger() {
        return this;
    }

    // endregion

    // region OpMode Implementation

    /** Equivalent to {@link MonoBehaviour#awake()} */
    @Override
    public void init() {
        RobotV1.CURRENT_MODE = RobotV1.OperationMode.TELE_OP;

        RobotV1.initializeGamepads(this.gamepad1, this.gamepad2);
        Discoverable.discover(this);
        this.systems.values().forEach(MonoBehaviour::awake);
        RobotV1.awake();

        this.telemetry.setMsTransmissionInterval(50);
        this.push();
    }

    /** Equivalent to {@link MonoBehaviour#preUpdate()} */
    @Override
    public void init_loop() {
        this.systems.values().forEach(MonoBehaviour::preUpdate);
        RobotV1.preUpdate();

        this.push();
    }

    /** Equivalent to {@link MonoBehaviour#start()} */
    @Override
    public void start() {
        this.systems.values().forEach(MonoBehaviour::start);
        RobotV1.start();

        this.push();
    }

    /** Equivalent to {@link MonoBehaviour#update()} */
    @Override
    public void loop() {
        this.systems.values().forEach(MonoBehaviour::update);
        RobotV1.update();

        this.push();
    }

    /** Equivalent to {@link MonoBehaviour#destroy()} */
    @Override
    public void stop() {
        this.systems.values().forEach(MonoBehaviour::destroy);
        RobotV1.destroy();

        this.push();
    }

    // endregion

    // region Logger Implementation

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

    // endregion
}
