package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class Fonts implements Disposable {
    public enum Tier {
        TITLE(FontFactory.TITLE_SCALE),
        SUBTITLE(FontFactory.SUBTITLE_SCALE),
        BODY(FontFactory.BODY_SCALE),
        HUD(FontFactory.HUD_SCALE);

        final float scale;

        Tier(float scale) {
            this.scale = scale;
        }
    }

    private final BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private float hudBaseScale = 1f;
    private float menuBaseScale = 1f;

    public Fonts() {
        font = FontFactory.createGameFont();
    }

    public BitmapFont getFont() {
        return font;
    }

    public GlyphLayout getLayout() {
        return layout;
    }

    /** Gameplay HUD only (original scale). */
    public void updateHudScale(float worldHeight) {
        hudBaseScale = worldHeight / UiSpacing.REF_PIXEL_HEIGHT;
        font.getData().setScale(hudBaseScale);
        font.setUseIntegerPositions(false);
    }

    /** Menu / overlay screens — snapped scale for crisp, non-overlapping lines. */
    public void updateMenuScale(float worldHeight) {
        float screenH = Gdx.graphics.getHeight();
        if (screenH < 1f) {
            screenH = UiSpacing.REF_PIXEL_HEIGHT;
        }
        float unitsPerPixel = worldHeight / screenH;
        float targetCapPx = 20f;
        float raw = targetCapPx * unitsPerPixel / 15f;
        menuBaseScale = Math.max(0.02f, Math.round(raw * 40f) / 40f);
        font.getData().setScale(menuBaseScale);
        font.setUseIntegerPositions(true);
    }

    private float menuScale(Tier tier) {
        return menuBaseScale * tier.scale;
    }

    private float hudScale(Tier tier) {
        return hudBaseScale * tier.scale;
    }

    public float menuLineHeight(Tier tier) {
        font.getData().setScale(menuScale(tier));
        float lh = font.getLineHeight() * 1.2f;
        font.getData().setScale(menuBaseScale);
        return lh;
    }

    public void drawMenuCentered(SpriteBatch batch, Tier tier, Color color, String text,
                                 float centerX, float baselineY) {
        font.getData().setScale(menuScale(tier));
        layout.setText(font, text);
        float x = Math.round(centerX - layout.width / 2f);
        float y = Math.round(baselineY);
        font.setColor(color);
        font.draw(batch, layout, x, y);
        font.getData().setScale(menuBaseScale);
        font.setColor(Color.WHITE);
    }

    public void drawHud(SpriteBatch batch, Tier tier, Color color, CharSequence text, float x, float y) {
        font.getData().setScale(hudScale(tier));
        font.setColor(color);
        font.draw(batch, text, x, y);
        font.getData().setScale(hudBaseScale);
        font.setColor(Color.WHITE);
    }

    public void drawHudLayout(SpriteBatch batch, Tier tier, Color color, float x, float y) {
        font.getData().setScale(hudScale(tier));
        font.setColor(color);
        font.draw(batch, layout, x, y);
        font.getData().setScale(hudBaseScale);
        font.setColor(Color.WHITE);
    }

    public GlyphLayout measureHud(Tier tier, CharSequence text) {
        font.getData().setScale(hudScale(tier));
        layout.setText(font, text);
        font.getData().setScale(hudBaseScale);
        return layout;
    }

    public GlyphLayout measureMenu(Tier tier, CharSequence text) {
        font.getData().setScale(menuScale(tier));
        layout.setText(font, text);
        font.getData().setScale(menuBaseScale);
        return layout;
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
