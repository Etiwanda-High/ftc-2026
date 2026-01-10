package moe.seikimo.ftc.game.prompt;

import lombok.Data;

import java.util.function.Function;

@Data
public final class Prompt<T> {
    public static Function<String, Integer> INTEGER_PARSER = Integer::parseInt;
    public static Function<String, Double> DOUBLE_PARSER = Double::parseDouble;
    public static <T extends Enum<T>> Function<String, T> ENUM_PARSER(Class<T> enumClass) {
        return input -> Enum.valueOf(enumClass.asSubclass(Enum.class), input);
    }

    private final String title;
    private final T value;

    private final Function<String, T> parser;
}
