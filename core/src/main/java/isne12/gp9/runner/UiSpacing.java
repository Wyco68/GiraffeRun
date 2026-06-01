package isne12.gp9.runner;

/**
 * 8px-based spacing system mapped to world units for the 8×5 {@link com.badlogic.gdx.utils.viewport.FitViewport}.
 * Reference: 480px design height (matches {@link Main#updateFontScale()}).
 */
public final class UiSpacing {
    public static final float REF_PIXEL_HEIGHT = 480f;
    public static final float UNIT_PX = 8f;
    public static final float SMALL_PX = 8f;
    public static final float MEDIUM_PX = 16f;
    public static final float LARGE_PX = 24f;
    public static final float XL_PX = 32f;
    /** Minimum touch target at 480px ref (see {@link UiScreenProfile} for device clamps). */
    public static final float TOUCH_MIN_PX = 64f;
    public static final float ICON_PX = 56f;

    private UiSpacing() {
    }

    public static float pxToWorld(float px, float worldHeight) {
        return px / REF_PIXEL_HEIGHT * worldHeight;
    }

    public static float small(float worldHeight) {
        return pxToWorld(SMALL_PX, worldHeight);
    }

    public static float medium(float worldHeight) {
        return pxToWorld(MEDIUM_PX, worldHeight);
    }

    public static float large(float worldHeight) {
        return pxToWorld(LARGE_PX, worldHeight);
    }

    public static float xl(float worldHeight) {
        return pxToWorld(XL_PX, worldHeight);
    }

    public static float touchTarget(float worldHeight) {
        return pxToWorld(TOUCH_MIN_PX, worldHeight);
    }

    public static float iconSize(float worldHeight) {
        return pxToWorld(ICON_PX, worldHeight);
    }
}
