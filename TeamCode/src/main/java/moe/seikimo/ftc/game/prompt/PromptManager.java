package moe.seikimo.ftc.game.prompt;

import lombok.val;
import moe.seikimo.ftc.game.MonoBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Multi-prompt configuration system.
 */
public final class PromptManager implements MonoBehaviour {
    /** Map of label to prompt. */
    private final Map<String, Prompt<?>> prompts = new HashMap<>();

    private int currentIndex = 0;

    /**
     * Gets the prompt value by label.
     *
     * @param label The label of the prompt.
     * @return The prompt.
     * @param <T> The type of the prompt.
     */
    public <T> Prompt<T> getValue(String label) {
        @SuppressWarnings("unchecked")
        val prompt = (Prompt<T>) this.prompts.get(label);
        if (prompt == null) {
            throw new IllegalArgumentException("No prompt found with label: " + label);
        }
        return prompt;
    }

    /**
     * Adds an enum prompt to the manager.
     *
     * @param label The label of the prompt. (used for referencing later)
     * @param title The title of the prompt.
     * @param defaultValue The default value of the prompt.
     * @return The current prompt manager.
     * @param <T> The enum type.
     */
    public <T extends Enum<T>> PromptManager add(String label, String title, T defaultValue) {
        return this.add(label, title, defaultValue, Prompt.ENUM_PARSER(defaultValue.getClass()));
    }

    /**
     * Adds a prompt to the manager.
     *
     * @param label The label of the prompt. (used for referencing later)
     * @param title The title of the prompt.
     * @param defaultValue The default value of the prompt.
     * @param parser The parser function for the prompt.
     * @return The current prompt manager.
     * @param <T> The type of the prompt.
     */
    public <T> PromptManager add(String label, String title, T defaultValue, Function<String, T> parser) {
        val prompt = new Prompt<>(title, defaultValue, parser);
        this.prompts.put(label, prompt);
        return this;
    }
}
