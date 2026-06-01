package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

/**
 * Screen classification and UI scale derived from physical pixels.
 * Rebuilt on resize only — never per frame.
 */
public final class UiScreenProfile {
    public static final float DESIGN_WIDTH = 1280f;
    public static final float DESIGN_HEIGHT = 720f;

    public static final int MOBILE_MAX_WIDTH = 768;
    public static final int TABLET_MAX_WIDTH = 1200;

    /** Touch target size in design pixels (clamped min–max per form factor). */
    public static final float MOBILE_TOUCH_MIN_PX = 192f;
    public static final float MOBILE_TOUCH_MAX_PX = 272f;
    public static final float TABLET_TOUCH_MIN_PX = 128f;
    public static final float TABLET_TOUCH_MAX_PX = 224f;

    public final int screenWidth;
    public final int screenHeight;
    public final boolean isMobile;
    public final boolean isTablet;
    public final boolean isDesktop;
    public final boolean isPortrait;
    /** min(screenW/1280, screenH/720) — clamped so tiny windows stay usable. */
    public final float uiScale;
    public final float safePadPx;
    public final float touchButtonPx;

    private UiScreenProfile(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        isMobile = screenWidth < MOBILE_MAX_WIDTH;
        isTablet = screenWidth >= MOBILE_MAX_WIDTH && screenWidth < TABLET_MAX_WIDTH;
        isDesktop = screenWidth >= TABLET_MAX_WIDTH;
        isPortrait = screenHeight > screenWidth;

        uiScale = Math.max(0.65f, Math.min(screenWidth / DESIGN_WIDTH, screenHeight / DESIGN_HEIGHT));
        safePadPx = 10f * uiScale;

        if (isMobile) {
            touchButtonPx = MathUtils.clamp(236f * uiScale, MOBILE_TOUCH_MIN_PX, MOBILE_TOUCH_MAX_PX);
        } else if (isTablet) {
            touchButtonPx = MathUtils.clamp(168f * uiScale, TABLET_TOUCH_MIN_PX, TABLET_TOUCH_MAX_PX);
        } else {
            touchButtonPx = MathUtils.clamp(96f * uiScale, 80f, 112f);
        }
    }

    public static UiScreenProfile fromDisplay() {
        return fromSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static UiScreenProfile fromSize(int width, int height) {
        int w = Math.max(1, width);
        int h = Math.max(1, height);
        return new UiScreenProfile(w, h);
    }

    /** Mobile portrait: stacked HUD + bottom touch band. */
    public boolean useMobileHudLayout() {
        return isMobile && isPortrait;
    }

    public boolean touchUiEnabled() {
        return isMobile || isTablet;
    }

    public float safePadWorld(float uiWorldHeight) {
        return pxToUiWorld(safePadPx, uiWorldHeight);
    }

    public float touchButtonWorld(float uiWorldHeight) {
        return pxToUiWorld(touchButtonPx, uiWorldHeight);
    }

    /** Map design px to UI viewport world units (720 reference height). */
    public float pxToUiWorld(float px, float uiWorldHeight) {
        return px / DESIGN_HEIGHT * uiWorldHeight;
    }

    /** Map design px to gameplay viewport world units (480 ref via {@link UiSpacing}). */
    public float pxToGameplayWorld(float px, float gameplayWorldHeight) {
        return px / UiSpacing.REF_PIXEL_HEIGHT * gameplayWorldHeight * uiScale;
    }

    public float gameplaySafePad(float gameplayWorldHeight) {
        return pxToGameplayWorld(safePadPx, gameplayWorldHeight);
    }

    public float gameplayTouchSize(float gameplayWorldHeight) {
        return pxToGameplayWorld(touchButtonPx, gameplayWorldHeight);
    }
}
