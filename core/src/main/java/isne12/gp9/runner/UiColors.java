package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;

/** Shared UI palette (TeaVM-safe, no HTML/CSS). */
public final class UiColors {
    public static final Color TITLE = new Color(0.95f, 0.98f, 1f, 1f);
    public static final Color SUBTITLE = new Color(0.75f, 0.88f, 0.95f, 1f);
    public static final Color BODY = new Color(0.85f, 0.92f, 0.96f, 1f);
    public static final Color HUD = new Color(0f, 0.8f, 0.9f, 1f);
    public static final Color MUTED = new Color(0.55f, 0.65f, 0.72f, 1f);
    public static final Color ACCENT = new Color(0.2f, 0.95f, 0.85f, 1f);
    public static final Color PANEL_DIM = new Color(0f, 0f, 0f, 0.55f);

    public static final Color HEALTH_FULL = new Color(0.25f, 0.88f, 0.35f, 1f);
    public static final Color HEALTH_MID = new Color(0.95f, 0.82f, 0.15f, 1f);
    public static final Color HEALTH_LOW = new Color(0.92f, 0.22f, 0.2f, 1f);
    public static final Color BAR_BG = new Color(0.08f, 0.1f, 0.12f, 0.85f);
    public static final Color BAR_EMPTY = new Color(0.2f, 0.24f, 0.28f, 0.9f);
    public static final Color CRYSTAL_FILL = new Color(0.15f, 0.75f, 0.95f, 1f);

    private UiColors() {
    }
}
