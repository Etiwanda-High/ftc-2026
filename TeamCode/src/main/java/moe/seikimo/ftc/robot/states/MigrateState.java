package moe.seikimo.ftc.robot.states;

import lombok.val;
import moe.seikimo.ftc.annotations.fields.FromPrompt;
import moe.seikimo.ftc.annotations.types.FiniteState;
import moe.seikimo.ftc.robot.fsm.State;
import moe.seikimo.ftc.robot.fsm.StateMachine;
import moe.seikimo.ftc.utils.MigrateHelper;

/**
 * Moves the robot from the designated starting position to the best launch point.
 */
@FiniteState(State.MIGRATE)
public final class MigrateState extends StateMachine {
    @FromPrompt("start_loc")
    public StartingLocation starting = StartingLocation.BACK_LAUNCH;

    @Override
    public void start() {
        val opcode = MigrateHelper.opcode(this.previous);
        val flags = MigrateHelper.flags(this.previous);
    }

    @Override
    public void destroy() {
    }

    @Override
    public State nextState() {
        return State.LAUNCHER_SHOOT;
    }

    public enum StartingLocation {
        /**
         * The back launch zone. This is the small triangle.
         */
        BACK_LAUNCH,
        /**
         * The front launch zone. This is against the wall in the large triangle.
         */
        FRONT_LAUNCH
    }
}
