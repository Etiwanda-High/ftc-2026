package moe.seikimo.legacy.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.legacy.game.LegacyController;

import java.util.HashSet;
import java.util.Set;

public final class LegacyRobot {
    private static final Set<MonoBehaviour> EXTRAS = new HashSet<>();
    public static OperationMode CURRENT_MODE = OperationMode.NONE;

    /** The default gamepad handles. */
    public static Gamepad user1, user2;
    /** The SolversLib gamepad handles. */
    public static GamepadEx gamepad1, gamepad2;
    /** The wrapper handles for gamepads. */
    public static LegacyController driver, operator;

    /**
     * Initializes the gamepads.
     *
     * @param gp1 The driver gamepad.
     * @param gp2 The operator gamepad.
     */
    public static void initializeGamepads(Gamepad gp1, Gamepad gp2) {
        LegacyRobot.user1 = gp1;
        LegacyRobot.user2 = gp2;
        LegacyRobot.gamepad1 = new GamepadEx(gp1);
        LegacyRobot.gamepad2 = new GamepadEx(gp2);
        LegacyRobot.driver = new LegacyController(LegacyRobot.gamepad1);
        LegacyRobot.operator = new LegacyController(LegacyRobot.gamepad2);

        EXTRAS.add(LegacyRobot.driver);
        EXTRAS.add(LegacyRobot.operator);
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
        CURRENT_MODE = LegacyRobot.OperationMode.NONE;

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
