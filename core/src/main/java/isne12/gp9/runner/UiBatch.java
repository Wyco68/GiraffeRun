package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** Draw UI icons and panels with the game {@link SpriteBatch} (TeaVM-safe). */
public final class UiBatch {
    private UiBatch() {
    }

    public static void drawIcon(SpriteBatch batch, UiIcons icons, UiIcons.Kind kind,
                                float x, float y, float size, boolean pressed) {
        TextureRegionDrawable drawable = pressed ? icons.pressed(kind) : icons.normal(kind);
        Color old = batch.getColor().cpy();
        batch.setColor(Color.WHITE);
        drawable.draw(batch, x, y, size, size);
        batch.setColor(old);
    }

    public static void drawDimFullscreen(SpriteBatch batch, Texture whitePixel,
                                         float worldWidth, float worldHeight) {
        Color old = batch.getColor().cpy();
        batch.setColor(UiColors.PANEL_DIM);
        batch.draw(whitePixel, 0f, 0f, worldWidth, worldHeight);
        batch.setColor(old);
    }
}
