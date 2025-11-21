package moe.seikimo.ftc.robot;

import androidx.core.util.Preconditions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import lombok.Getter;
import moe.seikimo.ftc.Discoverable;
import moe.seikimo.ftc.annotations.Controller;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.game.PlayerController;
import moe.seikimo.ftc.robot.fsm.State;
import moe.seikimo.ftc.utils.Logger;
import moe.seikimo.ftc.utils.RobotLogger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Base {@link OpMode} including state management & systems. */
@Getter
public abstract class Robot extends OpMode implements Discoverable {
    private final Logger logger = new RobotLogger(this);
    private final Map<Class<?>, MonoBehaviour> systems = new ConcurrentHashMap<>();

    private boolean initialized = false;
    private State currentState = null;
    private OperationMode opMode = OperationMode.NONE;
    private PlayerController driver, operator;

    /** @return The mode of operation. */
    protected abstract OperationMode getOperationType();

    /**
     * Sets the new state of the robot.
     *
     * @param newState The new state to set.
     */
    public final void changeState(@NotNull State newState) {
        Preconditions.checkArgument(this.opMode != OperationMode.NONE, "Robot is not initialized!");

        if (this.currentState != null) {
            this.currentState.destroy();
        }

        this.currentState = newState;
        this.currentState.start();
    }

    // region Discoverable Implementation

    /**
     * Fetches the raw gamepad handle for the specified player.
     *
     * @param player The player to fetch the gamepad for.
     * @return The raw gamepad handle.
     */
    public final Gamepad getRawHandle(Controller.Player player) {
        Preconditions.checkArgument(this.opMode != OperationMode.NONE, "Robot is not initialized!");

        // "it's unfortunate we can't use switch-case pattern matching :/"
        switch (player) {
            case DRIVER: return this.gamepad1;
            case OPERATOR: return this.gamepad2;
            default: throw new IllegalArgumentException("Unknown player: " + player);
        }
    }

    /**
     * Fetches the extended gamepad handle for the specified player.
     *
     * @param player The player to fetch the gamepad for.
     * @return The extended gamepad handle.
     */
    public final GamepadEx getControllerHandle(Controller.Player player) {
        Preconditions.checkArgument(this.opMode != OperationMode.NONE, "Robot is not initialized!");

        // "it's unfortunate we can't use switch-case pattern matching :/"
        switch (player) {
            case DRIVER: return this.driver.getHandle();
            case OPERATOR: return this.operator.getHandle();
            default: throw new IllegalArgumentException("Unknown player: " + player);
        }
    }

    /**
     * Fetches the controller instance for the specified player.
     *
     * @param player The player to fetch the controller for.
     * @return The controller instance.
     */
    public final PlayerController getController(Controller.Player player) {
        Preconditions.checkArgument(this.opMode != OperationMode.NONE, "Robot is not initialized!");

        // "it's unfortunate we can't use switch-case pattern matching :/"
        switch (player) {
            case DRIVER: return this.driver;
            case OPERATOR: return this.operator;
            default: throw new IllegalArgumentException("Unknown player: " + player);
        }
    }

    @Override
    public void discover() {
        // Discover controllers.
        this.driver = new PlayerController(this.gamepad1);
        this.operator = new PlayerController(this.gamepad2);

        // Discover systems.
        Discoverable.super.discover();
    }

    @Override
    public Robot asRobot() {
        return this;
    }

    // endregion

    // region OpMode Implementation

    /** Equivalent to {@link MonoBehaviour#awake()} */
    @Override
    public void init() {
        Preconditions.checkArgument(!this.initialized, "Robot is already initialized!");

        this.opMode = this.getOperationType();

        this.discover();
        this.systems.values().forEach(MonoBehaviour::awake);

        this.telemetry.setMsTransmissionInterval(50);
        this.logger.push();

        this.initialized = true;
    }

    /** Equivalent to {@link MonoBehaviour#preUpdate()} */
    @Override
    public void init_loop() {
        this.systems.values().forEach(MonoBehaviour::preUpdate);

        this.logger.push();
    }

    /** Equivalent to {@link MonoBehaviour#start()} */
    @Override
    public void start() {
        this.systems.values().forEach(MonoBehaviour::start);

        this.logger.push();
    }

    /** Equivalent to {@link MonoBehaviour#update()} */
    @Override
    public void loop() {
        this.systems.values().forEach(MonoBehaviour::update);

        this.logger.push();
    }

    /** Equivalent to {@link MonoBehaviour#destroy()} */
    @Override
    public void stop() {
        this.opMode = OperationMode.NONE;

        this.systems.values().forEach(MonoBehaviour::destroy);

        this.logger.push();
    }

    // endregion

    /** The mode the robot is operating in. */
    public enum OperationMode {
        NONE,
        AUTONOMOUS,
        TELE_OP
    }
}
