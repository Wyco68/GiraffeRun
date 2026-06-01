package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Menu labels via the same {@link BitmapFont} path as gameplay HUD (TeaVM-safe).
 */
public final class MenuText {
    private static final GlyphLayout layout = new GlyphLayout();

    private MenuText() {
    }

    public static float scale(Main game, float multiplier) {
        return game.viewport.getWorldHeight() / UiSpacing.REF_PIXEL_HEIGHT * multiplier;
    }

    public static float lineHeight(Main game, float multiplier) {
        BitmapFont font = game.font;
        float s = scale(game, multiplier);
        font.getData().setScale(s);
        float h = font.getLineHeight() * 1.15f;
        font.getData().setScale(scale(game, 1f));
        return h;
    }

    public static void drawCentered(Main game, Batch batch, String text,
                                    float centerX, float baselineY, float multiplier, Color color) {
        BitmapFont font = game.font;
        font.setUseIntegerPositions(false);
        float s = scale(game, multiplier);
        font.getData().setScale(s);
        font.setColor(color);
        layout.setText(font, text);
        font.draw(batch, layout, centerX - layout.width / 2f, baselineY);
    }
}
