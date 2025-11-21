package moe.seikimo.legacy.utils;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import lombok.val;
import moe.seikimo.legacy.game.LegacyGameManager;
import moe.seikimo.ftc.game.MonoBehaviour;

public final class LegacyLogger implements MonoBehaviour {
    private final LegacyGameManager gameManager;
    private final TelemetryManager panels;

    /**
     * Creates a new logger instance.
     *
     * @param gameManager Game manager to use for telemetry.
     */
    public LegacyLogger(LegacyGameManager gameManager) {
        this.gameManager = gameManager;
        this.panels = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    /**
     * Updates all telemetry systems simultaneously.
     */
    public void push() {
        this.gameManager.getTelemetry().update();
        this.panels.update();
    }

    /**
     * Adds a formatted log entry to telemetry.
     *
     * @param caption The caption for the log entry.
     * @param args The arguments to format into the caption.
     * @return The game manager instance.
     */
    public LegacyLogger log(String caption, String message, Object... args) {
        if (args.length == 0) {
            this.gameManager.getTelemetry().addData(caption, message);
            this.panels.debug(String.format("%s: %s", caption, message));
        } else {
            val formatted = String.format(message, args);
            this.gameManager.getTelemetry().addData(caption, formatted);
            this.panels.debug(String.format("%s: %s", caption, formatted));
        }

        return this;
    }
}
