package moe.seikimo.ftc.robot;

import androidx.core.util.Preconditions;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import lombok.Getter;
import lombok.val;
import moe.seikimo.ftc.Discoverable;
import moe.seikimo.ftc.annotations.fields.Controller;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.game.PlayerController;
import moe.seikimo.ftc.robot.fsm.State;
import moe.seikimo.ftc.robot.fsm.StateMachine;
import moe.seikimo.ftc.robot.fsm.StateSystem;
import moe.seikimo.ftc.utils.Logger;
import moe.seikimo.ftc.utils.RobotLogger;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/** Base {@link OpMode} including state management & systems. */
@Getter
public abstract class Robot extends OpMode implements Discoverable {
    private final Logger logger = new RobotLogger(this);
    private final Map<State, Class<?>> states = new ConcurrentHashMap<>();
    private final Map<Class<?>, MonoBehaviour> systems = new ConcurrentHashMap<>();

    private boolean initialized = false;
    private OperationMode opMode = OperationMode.NONE;
    private PlayerController driver, operator;

    /** @return The mode of operation. */
    protected abstract OperationMode getOperationType();

    /** @return Whether the robot is in autonomous mode. */
    public final boolean isAuto() {
        return this.opMode == OperationMode.AUTONOMOUS;
    }

    /**
     * Changes the robot to the specified state.
     *
     * @param state The state to change to.
     */
    public final void changeState(State state) {
        Preconditions.checkArgument(this.opMode != Robot.OperationMode.NONE, "Robot is not initialized!");

        // Get the state for the current operation mode.
        if (!this.states.containsKey(state)) {
            throw new IllegalArgumentException("State not found: " + state);
        } else {
            Class<?> stateClass = this.states.get(state);
            Objects.requireNonNull(stateClass);

            try {
                val instance = (StateMachine) this.instantiate(stateClass);
                this.getStateManager().changeState(state, instance);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to instantiate state: " + stateClass.getName(), ex);
            }
        }
    }

    /** @return The state manager system. */
    public StateSystem getStateManager() {
        return (StateSystem) this.systems.get(StateSystem.class);
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
