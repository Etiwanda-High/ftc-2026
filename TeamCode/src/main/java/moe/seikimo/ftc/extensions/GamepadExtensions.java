package moe.seikimo.ftc.extensions;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;

public final class GamepadExtensions {
    private static final Button SUPER_BUTTON = Button.BACK;

    /**
     * Checks if a button is "super pressed" (just pressed this frame).
     *
     * @param gamepad The gamepad to check.
     * @param button The button to check.
     * @return True if the button is super pressed, false otherwise.
     */
    public static boolean superPressed(GamepadEx gamepad, Button button) {
        return gamepad.isDown(SUPER_BUTTON) && gamepad.wasJustPressed(button);
    }
}
