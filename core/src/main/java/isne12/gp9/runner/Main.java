package isne12.gp9.runner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;
    public Assets assets;
    public GameData gameData;
    public MusicController music;
    public Texture whitePixel;

    private GameState state = GameState.LOADING;
    private int level = 1;
    private float globalSpeed = 3f;
    private FreeTypeFontGenerator fontGenerator;

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

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 24;
        param.color = new Color(0f, 0.8f, 0.9f, 1f);
        font = fontGenerator.generateFont(param);
        font.setUseIntegerPositions(false);
        updateFontScale();

        setScreen(new LoadingScreen(this));
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
        level = Math.min(level + 1, 3);
    }

    public void resetRun() {
        level = 1;
    }

    public void updateFontScale() {
        font.getData().setScale(viewport.getWorldHeight() / 480f);
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
        if (fontGenerator != null) {
            fontGenerator.dispose();
        }
        if (whitePixel != null) {
            whitePixel.dispose();
        }
        batch.dispose();
        font.dispose();
    }
}
