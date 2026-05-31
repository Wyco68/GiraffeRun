package isne12.gp9.runner;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** Creates HUD fonts compatible with desktop and TeaVM/WebAssembly builds. */
public final class FontFactory {

    private FontFactory() {
    }

    public static BitmapFont createHudFont() {
        BitmapFont font = new BitmapFont();
        font.setColor(0f, 0.8f, 0.9f, 1f);
        return font;
    }
}
