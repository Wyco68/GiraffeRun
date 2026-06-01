package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class McUi {
    public static final float TITLE_MULT = 1.45f;
    public static final float SUBTITLE_MULT = 1.15f;
    public static final float BODY_MULT = 1f;

    private McUi() {
    }

    public static void drawStoneButton(Batch batch, Texture whitePixel, Rectangle bounds, boolean pressed) {
        drawStoneButton(batch, whitePixel, bounds, pressed, null, null);
    }

    public static void drawStoneButton(Batch batch, Texture whitePixel, Rectangle bounds, boolean pressed,
                                       Color faceUp, Color facePressed) {
        float x = bounds.x;
        float y = bounds.y;
        float w = bounds.width;
        float h = bounds.height;
        float border = Math.max(w, h) * 0.05f;

        Color up = faceUp != null ? faceUp : McStyle.BUTTON_FACE;
        Color down = facePressed != null ? facePressed : McStyle.BUTTON_FACE_PRESSED;

        Color prev = batch.getColor().cpy();
        batch.setColor(McStyle.BORDER_DARK);
        batch.draw(whitePixel, x, y, w, h);

        float ix = x + border;
        float iy = y + border;
        float iw = w - border * 2f;
        float ih = h - border * 2f;
        batch.setColor(pressed ? down : up);
        batch.draw(whitePixel, ix, iy, iw, ih);

        float line = Math.max(border * 0.75f, 0.02f);
        if (!pressed) {
            batch.setColor(McStyle.BORDER_LIGHT);
            batch.draw(whitePixel, ix, iy + ih - line, iw, line);
            batch.draw(whitePixel, ix, iy, line, ih);
            batch.setColor(McStyle.BORDER_DARK);
            batch.draw(whitePixel, ix, iy, iw, line);
            batch.draw(whitePixel, ix + iw - line, iy, line, ih);
        }
        batch.setColor(prev);
    }

    /** Minecraft-style button with translucent face and borders (touch overlays). */
    public static void drawTransparentStoneButton(Batch batch, Texture whitePixel, Rectangle bounds,
                                                  boolean pressed, Color faceUp, Color facePressed,
                                                  float alpha) {
        float x = bounds.x;
        float y = bounds.y;
        float w = bounds.width;
        float h = bounds.height;
        float border = Math.max(w, h) * 0.05f;

        Color up = faceUp != null ? faceUp : McStyle.BUTTON_FACE;
        Color down = facePressed != null ? facePressed : McStyle.BUTTON_FACE_PRESSED;
        float faceAlpha = alpha * (pressed ? 0.55f : 0.35f);
        float edgeAlpha = alpha * 0.5f;

        Color prev = batch.getColor().cpy();
        batch.setColor(McStyle.BORDER_DARK.r, McStyle.BORDER_DARK.g, McStyle.BORDER_DARK.b, edgeAlpha);
        batch.draw(whitePixel, x, y, w, h);

        float ix = x + border;
        float iy = y + border;
        float iw = w - border * 2f;
        float ih = h - border * 2f;
        Color face = pressed ? down : up;
        batch.setColor(face.r, face.g, face.b, faceAlpha);
        batch.draw(whitePixel, ix, iy, iw, ih);

        float line = Math.max(border * 0.75f, 0.02f);
        if (!pressed) {
            batch.setColor(McStyle.BORDER_LIGHT.r, McStyle.BORDER_LIGHT.g, McStyle.BORDER_LIGHT.b, edgeAlpha);
            batch.draw(whitePixel, ix, iy + ih - line, iw, line);
            batch.draw(whitePixel, ix, iy, line, ih);
            batch.setColor(McStyle.BORDER_DARK.r, McStyle.BORDER_DARK.g, McStyle.BORDER_DARK.b, edgeAlpha);
            batch.draw(whitePixel, ix, iy, iw, line);
            batch.draw(whitePixel, ix + iw - line, iy, line, ih);
        }
        batch.setColor(prev);
    }

    public static void drawLabelInRect(Main game, SpriteBatch batch, String text, Rectangle bounds) {
        float baseline = bounds.y + bounds.height * 0.36f;
        MenuText.drawCentered(game, batch, text, bounds.x + bounds.width / 2f, baseline,
            BODY_MULT, McStyle.TEXT);
    }

    public static float drawTitle(Main game, SpriteBatch batch, String text, float centerX, float baselineY) {
        MenuText.drawCentered(game, batch, text, centerX, baselineY, TITLE_MULT, McStyle.TITLE);
        return baselineY - MenuText.lineHeight(game, TITLE_MULT);
    }

    public static float drawSubtitle(Main game, SpriteBatch batch, String text, float centerX, float baselineY) {
        MenuText.drawCentered(game, batch, text, centerX, baselineY, SUBTITLE_MULT, McStyle.SUBTITLE);
        return baselineY - MenuText.lineHeight(game, SUBTITLE_MULT);
    }

    public static float drawHint(Main game, SpriteBatch batch, String text, float centerX, float baselineY) {
        MenuText.drawCentered(game, batch, text, centerX, baselineY, BODY_MULT, McStyle.SUBTITLE);
        return baselineY - MenuText.lineHeight(game, BODY_MULT);
    }

    public static boolean isClicked(Viewport viewport, Rectangle bounds) {
        return UiTouch.justTouchedIn(viewport, bounds);
    }
}
