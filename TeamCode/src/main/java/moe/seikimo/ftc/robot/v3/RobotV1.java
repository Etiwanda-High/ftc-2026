package moe.seikimo.ftc.robot.v3;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.game.PlayerController;

import java.util.HashSet;
import java.util.Set;

public final class RobotV1 {
    private static final Set<MonoBehaviour> EXTRAS = new HashSet<>();
    public static OperationMode CURRENT_MODE = OperationMode.NONE;

    /** The default gamepad handles. */
    public static Gamepad user1, user2;
    /** The SolversLib gamepad handles. */
    public static GamepadEx gamepad1, gamepad2;
    /** The wrapper handles for gamepads. */
    public static PlayerController driver, operator;

    /**
     * Initializes the gamepads.
     *
     * @param gp1 The driver gamepad.
     * @param gp2 The operator gamepad.
     */
    public static void initializeGamepads(Gamepad gp1, Gamepad gp2) {
        RobotV1.user1 = gp1;
        RobotV1.user2 = gp2;
        RobotV1.gamepad1 = new GamepadEx(gp1);
        RobotV1.gamepad2 = new GamepadEx(gp2);
        RobotV1.driver = new PlayerController(RobotV1.gamepad1);
        RobotV1.operator = new PlayerController(RobotV1.gamepad2);

        EXTRAS.add(RobotV1.driver);
        EXTRAS.add(RobotV1.operator);
    }

    /**
     * Awakes all extra MonoBehaviours.
     */
    public static void awake() {
        EXTRAS.forEach(MonoBehaviour::awake);
    }

    /**
     * Pre-updates all extra MonoBehaviours.
     */
    public static void preUpdate() {
        EXTRAS.forEach(MonoBehaviour::preUpdate);
    }

    /**
     * Starts all extra MonoBehaviours.
     */
    public static void start() {
        EXTRAS.forEach(MonoBehaviour::start);
    }

    /**
     * Updates all extra MonoBehaviours.
     */
    public static void update() {
        EXTRAS.forEach(MonoBehaviour::update);
    }

    /**
     * Destroys all extra MonoBehaviours.
     */
    public static void destroy() {
        CURRENT_MODE = RobotV1.OperationMode.NONE;

        EXTRAS.forEach(MonoBehaviour::destroy);
        EXTRAS.clear();
    }

    /** The mode the robot is operating in. */
    public enum OperationMode {
        NONE,
        AUTONOMOUS,
        TELE_OP
    }
}
