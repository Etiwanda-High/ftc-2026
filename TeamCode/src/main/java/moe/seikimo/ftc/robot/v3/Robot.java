package moe.seikimo.ftc.robot.v3;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import lombok.Getter;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.robot.fsm.State;
import org.jetbrains.annotations.NotNull;

/** Base {@link OpMode} including state management & systems. */
@Getter
public abstract class Robot extends OpMode implements MonoBehaviour {
    private OperationMode opMode = OperationMode.NONE;
    private State currentState = null;

    /**
     * Sets the new state of the robot.
     *
     * @param newState The new state to set.
     */
    public void changeState(@NotNull State newState) {
        if (this.currentState != null) {
            this.currentState.destroy();
        }

        this.currentState = newState;
        this.currentState.start();
    }

    // region OpMode Implementation

    @Override
    public void init() {

    }

    // endregion

    /** The mode the robot is operating in. */
    public enum OperationMode {
        NONE,
        AUTONOMOUS,
        TELE_OP
    }
}
