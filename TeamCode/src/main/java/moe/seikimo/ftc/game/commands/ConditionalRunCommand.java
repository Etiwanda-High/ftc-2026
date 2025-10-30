package moe.seikimo.ftc.game.commands;

import com.seattlesolvers.solverslib.command.Command;
import com.seattlesolvers.solverslib.command.CommandBase;
import com.seattlesolvers.solverslib.command.CommandScheduler;
import lombok.RequiredArgsConstructor;

import java.util.function.BooleanSupplier;

@RequiredArgsConstructor
public final class ConditionalRunCommand extends CommandBase {
    private final BooleanSupplier condition;
    private final Command command;

    @Override
    public void schedule(boolean interruptible) {
        if (!this.condition.getAsBoolean()) {
            return;
        }

        CommandScheduler.getInstance().schedule(interruptible, this.command);
    }
}
