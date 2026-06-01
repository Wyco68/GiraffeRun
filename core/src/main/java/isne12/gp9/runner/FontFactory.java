package isne12.gp9.runner;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public final class FontFactory {
    public static final float TITLE_SCALE = 1.35f;
    public static final float SUBTITLE_SCALE = 1.1f;
    public static final float BODY_SCALE = 1f;
    public static final float HUD_SCALE = 0.85f;
    public static final float LINE_HEIGHT_FACTOR = 1.28f;

    private FontFactory() {
    }

    public static BitmapFont createGameFont() {
        BitmapFont font = new BitmapFont();
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setUseIntegerPositions(false);
        return font;
    }

    public static float lineHeight(BitmapFont font) {
        return font.getLineHeight() * LINE_HEIGHT_FACTOR;
    }
}
