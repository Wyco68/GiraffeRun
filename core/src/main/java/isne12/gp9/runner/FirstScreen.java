package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class FirstScreen implements Screen {
    final Main game;

    private final McButton playButton = new McButton();
    private final McButton soundButton = new McButton();
    private UiMenuLayout.MenuLayout menuLayout;

    public FirstScreen(final Main game) {
        this.game = game;
        layoutUi();
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        menuLayout = UiMenuLayout.layoutSingleButton(game, w, h, playButton, "PLAY", 2, 1);
        UiMenuLayout.layoutSoundToggle(game, soundButton, w, h);
    }

    @Override
    public void show() {
        game.setState(GameState.MENU);
        game.applyAudioSettings();
        game.music.play(game.assets.getMusic(Assets.THEME_AUDIO), 0.25f, true);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.updateMenuFontScale();

        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;

        game.batch.begin();
        game.batch.setColor(Color.WHITE);

        if (menuLayout != null) {
            float y = menuLayout.titleBaselineY;
            y = McUi.drawTitle(game, game.batch, "GiraffeRun", cx, y);
            y = McUi.drawSubtitle(game, game.batch, "Collect 5 crystals per level", cx, y);
        }

        playButton.draw(game, game.batch);
        soundButton.draw(game, game.batch);

        if (menuLayout != null) {
            McUi.drawHint(game, game.batch,
                "A/D move  S shield  Tap field to teleport  On-screen buttons on mobile", cx,
                menuLayout.hintBaselineY);
        }
        game.batch.end();

        playButton.clearPressed();
        soundButton.clearPressed();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || playButton.handleClick(game.viewport)) {
            beginGame();
        } else if (soundButton.handleClick(game.viewport)) {
            game.settings.toggleAudio();
            game.applyAudioSettings();
            layoutUi();
        }
    }

    private void beginGame() {
        if (game.settings.isAudioEnabled()) {
            game.music.fadeTo(0.3f, 0.5f);
        }
        game.setScreen(new GameScreen(game));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.updateMenuFontScale();
        layoutUi();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
