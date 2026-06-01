package isne12.gp9.runner;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/** Keeps UI rectangles inside the 8×5 world viewport with consistent safe margins. */
public final class UiBounds {
    private UiBounds() {
    }

    public static float margin(float worldHeight) {
        return UiSpacing.small(worldHeight);
    }

    public static float safeLeft(float worldHeight) {
        return margin(worldHeight);
    }

    public static float safeRight(float worldWidth, float worldHeight) {
        return worldWidth - margin(worldHeight);
    }

    public static float safeBottom(float worldHeight) {
        return margin(worldHeight);
    }

    public static float safeTop(float worldHeight) {
        return worldHeight - margin(worldHeight);
    }

    public static float clampX(float x, float width, float worldWidth, float worldHeight) {
        float left = safeLeft(worldHeight);
        float right = safeRight(worldWidth, worldHeight) - width;
        return MathUtils.clamp(x, left, Math.max(left, right));
    }

    public static float clampY(float y, float height, float worldHeight) {
        float bottom = safeBottom(worldHeight);
        float top = safeTop(worldHeight) - height;
        return MathUtils.clamp(y, bottom, Math.max(bottom, top));
    }

    /** Pin rectangle inside the safe area (position and size unchanged). */
    public static void clampRect(Rectangle rect, float worldWidth, float worldHeight) {
        rect.x = clampX(rect.x, rect.width, worldWidth, worldHeight);
        rect.y = clampY(rect.y, rect.height, worldHeight);
    }

    /** Right-align a group (e.g. bar + icon) so nothing extends past the safe edges. */
    public static float rightAlignGroup(float worldWidth, float worldHeight, float groupWidth) {
        float right = safeRight(worldWidth, worldHeight);
        float left = safeLeft(worldHeight);
        float x = right - groupWidth;
        return Math.max(left, x);
    }
}
