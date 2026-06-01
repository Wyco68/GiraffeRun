package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** HUD text helpers (gameplay only). */
public final class UiText {
    private UiText() {
    }

    public static void drawCentered(SpriteBatch batch, Fonts fonts, Fonts.Tier tier, Color color,
                                    String text, float centerX, float baselineY) {
        GlyphLayout layout = fonts.measureHud(tier, text);
        fonts.drawHudLayout(batch, tier, color, centerX - layout.width / 2f, baselineY);
    }

    public static void drawRight(SpriteBatch batch, Fonts fonts, Fonts.Tier tier, Color color,
                                 String text, float rightX, float baselineY) {
        GlyphLayout layout = fonts.measureHud(tier, text);
        fonts.drawHudLayout(batch, tier, color, rightX - layout.width, baselineY);
    }
}
