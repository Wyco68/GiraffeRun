package isne12.gp9.runner;

import com.badlogic.gdx.math.Rectangle;

/**
 * Gameplay-viewport rectangles for touch controls, derived from the adaptive HUD table.
 * Updated only when {@link AdaptiveHudLayout#rebuildLayout()} runs.
 */
public final class HudLayoutMetrics {
    public final Rectangle leftMove = new Rectangle();
    public final Rectangle rightMove = new Rectangle();
    public final Rectangle shieldTouch = new Rectangle();
    public final Rectangle pause = new Rectangle();
    /** Touches above this Y may trigger teleport (gameplay world units). */
    public float teleportMinWorldY;

    public void copyFrom(HudLayoutMetrics other) {
        leftMove.set(other.leftMove);
        rightMove.set(other.rightMove);
        shieldTouch.set(other.shieldTouch);
        pause.set(other.pause);
        teleportMinWorldY = other.teleportMinWorldY;
    }
}
