package moe.seikimo.ftc.game;

import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import lombok.Builder;

import java.util.function.Function;

@Builder
public final class ControllerProfile {
    public static final ControllerProfile DEFAULT = ControllerProfile.builder().build();

    /** The Fly-Sky driver profile is close to that of the Fly-Sky drone controller. */
    public static final ControllerProfile FLYSKY = ControllerProfile.builder()
        // Drive controls.
        .translateY(g -> -g.getRightY())
        .translateX(GamepadEx::getRightX)
        .rotate(GamepadEx::getLeftX)
        .build();

    @Builder.Default
    public Function<GamepadEx, Double>
        translateX = GamepadEx::getLeftX,
        translateY = GamepadEx::getLeftY,
        rotate = GamepadEx::getRightX;
}
