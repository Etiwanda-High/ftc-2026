package moe.seikimo.ftc.game.prompt;

import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Data
public final class Prompt<T extends Enum<T>> {
    private final String title;
    private final T value;
    private final List<T> options;
}
