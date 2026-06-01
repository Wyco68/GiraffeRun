package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/** Touch UI icons (normal + pressed); separate from gameplay shield/teleport sprites. */
public class UiIcons implements Disposable {
    public enum Kind {
        SETTINGS("ui/iconSettings.png", "ui/iconSettingsPressed.png"),
        SOUND("ui/iconSound.png", "ui/iconSoundPressed.png"),
        PAUSE("ui/iconPause.png", "ui/iconPausePressed.png"),
        BACK("ui/iconBack.png", "ui/iconBackPressed.png"),
        RETRY("ui/iconRetry.png", "ui/iconRetryPressed.png");

        final String normalPath;
        final String pressedPath;

        Kind(String normalPath, String pressedPath) {
            this.normalPath = normalPath;
            this.pressedPath = pressedPath;
        }
    }

    private final ObjectMap<Kind, TextureRegionDrawable> normal = new ObjectMap<>();
    private final ObjectMap<Kind, TextureRegionDrawable> pressed = new ObjectMap<>();

    public void load(Assets assets) {
        for (Kind kind : Kind.values()) {
            Texture up = assets.getTexture(kind.normalPath);
            Texture down = assets.getTexture(kind.pressedPath);
            normal.put(kind, new TextureRegionDrawable(new TextureRegion(up)));
            pressed.put(kind, new TextureRegionDrawable(new TextureRegion(down)));
        }
    }

    public TextureRegionDrawable normal(Kind kind) {
        return normal.get(kind);
    }

    public TextureRegionDrawable pressed(Kind kind) {
        return pressed.get(kind);
    }

    @Override
    public void dispose() {
        normal.clear();
        pressed.clear();
    }
}
