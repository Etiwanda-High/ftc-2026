package moe.seikimo.ftc.robot.states;

import moe.seikimo.ftc.annotations.fields.Controller;
import moe.seikimo.ftc.annotations.fields.Injected;
import moe.seikimo.ftc.annotations.types.FiniteState;
import moe.seikimo.ftc.game.PlayerController;
import moe.seikimo.ftc.robot.Robot;
import moe.seikimo.ftc.robot.fsm.State;
import moe.seikimo.ftc.robot.fsm.StateMachine;
import moe.seikimo.ftc.robot.managers.DriveSystem;

@FiniteState(State.IDLE)
public final class IdleState extends StateMachine {
    @Injected private Robot robot;
    @Injected private DriveSystem drive;

    @Controller(Controller.Player.DRIVER)
    private PlayerController driver;

    @Override
    public void start() {
        if (this.robot.isAuto()) {
            // TODO: Determine what to do in autonomous mode at the start.
        } else {
            this.drive.getFollower().startTeleOpDrive(true);
        }
    }

    @Override
    public void update() {
        if (this.robot.isAuto()) {
            return;
        }

        // Forward controller inputs to the drive system.
        this.drive.getFollower().setTeleOpDrive(
            this.driver.translateY(),
            this.driver.translateX(),
            this.driver.rotate()
        );
    }
}
