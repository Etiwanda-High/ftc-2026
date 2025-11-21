package moe.seikimo.ftc.robot.fsm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import moe.seikimo.ftc.robot.states.IdleState;

import javax.annotation.Nullable;

/**
 * The various states a robot can be in.
 * <p>
 * States generally should not be overridden. A state is
 * a long-running task that is designed only to be interrupted by
 * something of higher priority.
 * <p>
 * Example: `INTAKING` is not a state. `MOVE_TO_POINT` is a state.
 */
@Getter
@RequiredArgsConstructor
public enum State {
    /**
     * Reverts the robot to the previous state.
     * <p>
     * If no state is available, the robot will enter an idle state.
     */
    PREVIOUS(null),
    IDLE(IdleState.class);

    @Nullable
    final Class<? extends StateMachine> type;
}
