package moe.seikimo.ftc.game.prompt;

import com.qualcomm.robotcore.hardware.Gamepad;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import moe.seikimo.ftc.game.MonoBehaviour;
import moe.seikimo.ftc.utils.Preconditions;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.*;

/**
 * Multi-prompt configuration system.
 */
@RequiredArgsConstructor
public final class PromptManager implements MonoBehaviour {
    private final List<String> labels = new ArrayList<>();
    private final Map<String, Prompt<? extends Enum<?>>> prompts = new HashMap<>();
    private final Map<String, Integer> promptIndices = new HashMap<>();

    private int currentIndex = 0;

    /**
     * Gets the prompt value by label.
     *
     * @param label The label of the prompt.
     * @return The prompt.
     * @param <T> The type of the prompt.
     */
    public <T extends Enum<T>> Prompt<T> getPrompt(String label) {
        @SuppressWarnings("unchecked")
        val prompt = (Prompt<T>) this.prompts.get(label);
        if (prompt == null) {
            throw new IllegalArgumentException("No prompt found with label: " + label);
        }
        return prompt;
    }

    /**
     * Gets the prompt value by label.
     *
     * @param label The label of the prompt.
     * @return The prompt value.
     * @param <T> The type of the prompt.
     */
    public <T extends Enum<T>> T getValue(String label) {
        val prompt = this.<T>getPrompt(label);
        val index = this.promptIndices.getOrDefault(label, 0);
        if (index == null) {
            throw new IllegalArgumentException("No prompt index found for label: " + label);
        }
        return prompt.getOptions().get(index);
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
    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> PromptManager add(String label, String title, T defaultValue) {
        return this.add(label, title, defaultValue, EnumSet.allOf(defaultValue.getClass()));
    }

    /**
     * Adds an enum prompt to the manager with specific options.
     *
     * @param label The label of the prompt. (used for referencing later)
     * @param title The title of the prompt.
     * @param defaultValue The default value of the prompt.
     * @param options The options for the prompt.
     * @return The current prompt manager.
     * @param <T> The enum type.
     */
    public <T extends Enum<T>> PromptManager add(String label, String title, T defaultValue, EnumSet<T> options) {
        this.labels.add(label);
        this.prompts.put(label, new Prompt<>(title, defaultValue, new ArrayList<>(options)));
        return this;
    }

    /**
     * Renders the prompts to telemetry.
     *
     * @param telemetry The telemetry to render to.
     */
    public void render(Gamepad gamepad, Telemetry telemetry) {
        if (this.prompts.isEmpty()) return;

        // Change the selected option index using the D-Pad buttons.
        if (gamepad.dpadUpWasPressed()) {
            this.currentIndex = (this.currentIndex - 1 + this.prompts.size()) % this.prompts.size();
        } else if (gamepad.dpadDownWasPressed()) {
            this.currentIndex = (this.currentIndex + 1) % this.prompts.size();
        }

        // Change the selected value index using the left and right D-Pad buttons.
        val currentLabel = this.labels.get(this.currentIndex);
        val currentPrompt = this.prompts.get(currentLabel);
        Preconditions.notNull(currentPrompt, "Current prompt should not be null");

        val currentValueIndex = this.promptIndices.getOrDefault(currentLabel, 0);
        Preconditions.notNull(currentValueIndex, "Label value should not be null");

        if (gamepad.dpadLeftWasPressed() || gamepad.bWasPressed()) {
            this.promptIndices.put(currentLabel, (currentValueIndex - 1 + currentPrompt.getOptions().size()) % currentPrompt.getOptions().size());
        } else if (gamepad.dpadRightWasPressed() || gamepad.aWasPressed()) {
            this.promptIndices.put(currentLabel, (currentValueIndex + 1) % currentPrompt.getOptions().size());
        }

        // Render the prompts to telemetry.
        for (var i = 0; i < this.labels.size(); i++) {
            val label = this.labels.get(i);
            val prompt = this.prompts.get(label);
            Preconditions.notNull(prompt, "Prompt should not be null for label: " + label);

            val valueIndex = this.promptIndices.getOrDefault(label, 0);
            Preconditions.notNull(valueIndex, "Label value should not be null for label: " + label);

            val isSelected = i == this.currentIndex;
            telemetry.addData((isSelected ? "> " : "") + prompt.getTitle(), prompt.getOptions().get(valueIndex));
        }

        telemetry.update();
    }
}
