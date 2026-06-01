package isne12.gp9.runner;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    public SpriteBatch batch;
    /** @deprecated use {@link #fonts#hud} */
    public BitmapFont font;
    public Fonts fonts;
    public UiIcons uiIcons;
    public GameSettings settings;
    public FitViewport viewport;
    public Assets assets;
    public GameData gameData;
    public MusicController music;
    public Texture whitePixel;

    private GameState state = GameState.LOADING;
    private int level = 1;
    private float globalSpeed = 3f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        assets = new Assets();
        gameData = new GameData();
        music = new MusicController();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();

        fonts = new Fonts();
        font = fonts.getFont();
        uiIcons = new UiIcons();
        settings = new GameSettings();
        updateFontScale();

        enterFullscreenIfSupported();
        syncViewportToDisplay();

        setScreen(new LoadingScreen(this));
    }

    /** Desktop: native fullscreen. Web: canvas uses full window (see {@code WebLauncher}). */
    public void enterFullscreenIfSupported() {
        if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

    public void syncViewportToDisplay() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        if (w > 0 && h > 0) {
            viewport.update(w, h, true);
            updateHudFontScale();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (viewport != null && width > 0 && height > 0) {
            viewport.update(width, height, true);
            updateHudFontScale();
        }
        super.resize(width, height);
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public int getLevel() {
        return level;
    }

    public float getGlobalSpeed() {
        return globalSpeed;
    }

    public float getMoveSpeed() {
        return globalSpeed + level;
    }

    public void advanceLevel() {
        level = Math.min(level + 1, LevelConfig.MAX_LEVEL);
    }

    public int getMaxLevel() {
        return LevelConfig.MAX_LEVEL;
    }

    public void resetRun() {
        level = 1;
    }

    public void initUiAfterLoad() {
        uiIcons.load(assets);
    }

    public void updateFontScale() {
        updateMenuFontScale();
    }

    public void updateMenuFontScale() {
        updateHudFontScale();
    }

    public void updateHudFontScale() {
        fonts.updateHudScale(viewport.getWorldHeight());
        font = fonts.getFont();
    }

    public void applyMusicSetting() {
        if (!settings.isMusicEnabled()) {
            music.setVolume(0f);
        }
    }

    @Override
    public void render() {
        music.update(Gdx.graphics.getDeltaTime());
        super.render();
    }

    @Override
    public void dispose() {
        Screen screen = getScreen();
        if (screen != null) {
            screen.dispose();
        }
        if (assets != null) {
            assets.dispose();
        }
        if (whitePixel != null) {
            whitePixel.dispose();
        }
        if (uiIcons != null) {
            uiIcons.dispose();
        }
        if (fonts != null) {
            fonts.dispose();
        }
        batch.dispose();
    }
}
