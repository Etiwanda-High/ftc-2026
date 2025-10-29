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
    /** The default driver profile. */
    public static final DriverProfile DEFAULT = DriverProfile.builder()
        .name("Default")
        .superButton(Button.BACK)
        .fixedPower(0.75)
        .translateY(g -> -g.getLeftY())
        .translateX(GamepadEx::getLeftX)
        .rotate(g -> -g.getRightX())
        .launchPower(g -> g.getTrigger(Trigger.RIGHT_TRIGGER))
        .build();

    private final String name;

    public Button superButton;

    public double fixedPower;

    public final Function<GamepadEx, Double>
        translateY, translateX, rotate;
    public final Function<GamepadEx, Double>
        launchPower;
}
