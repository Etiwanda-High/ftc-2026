package moe.seikimo.ftc;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Button;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys.Trigger;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

@Getter
@Builder
public final class DriverProfile {
    public static final Button
        LAUNCH_CLOSE = Button.Y,
        LAUNCH_FAR = Button.B;
    public static final Button
        INTAKE_FORWARD = Button.X,
        INTAKE_REVERSE = Button.A,
        INTAKE_INCREASE = Button.DPAD_UP,
        INTAKE_DECREASE = Button.DPAD_DOWN;

    /** The default driver profile. */
    public static final DriverProfile DEFAULT = DriverProfile.builder()
        .name("Default")
        // Modifier keys.
        .superButton(Button.BACK)
        .intakeModifier(Button.LEFT_BUMPER)
        .launchModifier(Button.RIGHT_BUMPER)
        // Drive controls.
        .translateY(GamepadEx::getLeftY)
        .translateX(g -> -g.getLeftX())
        .rotate(GamepadEx::getRightX)
        // Analog inputs.
        .intakePower(g -> g.getTrigger(Trigger.LEFT_TRIGGER))
        .launchPower(g -> g.getTrigger(Trigger.RIGHT_TRIGGER))
        .build();

    /** The FlySky-like driver profile. */
    public static final DriverProfile FLYSKY = DriverProfile.builder()
        .name("FlySky")
        // Modifier keys.
        .superButton(Button.BACK)
        .intakeModifier(Button.LEFT_BUMPER)
        .launchModifier(Button.RIGHT_BUMPER)
        // Drive controls.
        .translateY(g -> -g.getRightY())
        .translateX(g -> -g.getRightX())
        .rotate(GamepadEx::getLeftX)
        // Analog inputs.
        .intakePower(g -> g.getTrigger(Trigger.LEFT_TRIGGER))
        .launchPower(g -> g.getTrigger(Trigger.RIGHT_TRIGGER))
        .build();

    private final String name;

    public Button superButton;
    public Button intakeModifier, launchModifier;

    public final Function<GamepadEx, Double>
        translateY, translateX, rotate;

    public final Function<GamepadEx, Double>
        intakePower, launchPower;
}
