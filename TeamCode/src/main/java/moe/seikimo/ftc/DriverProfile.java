package moe.seikimo.ftc;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

@Getter
@Builder
public final class DriverProfile {
    /** The default driver profile. */
    public static final DriverProfile DEFAULT = DriverProfile.builder()
        .name("Default")
        .translateY(g -> -g.getLeftY())
        .translateX(GamepadEx::getLeftX)
        .rotate(g -> -g.getRightX())
        .build();

    private final String name;

    public final Function<GamepadEx, Double>
        translateY, translateX, rotate;
}
