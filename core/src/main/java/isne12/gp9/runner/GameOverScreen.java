package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    final Main game;
    Texture backGround;
    private final McButton retryButton = new McButton();
    private UiMenuLayout.MenuLayout menuLayout;

    public GameOverScreen(final Main game) {
        this.game = game;
        backGround = game.assets.getTexture(Assets.GAME_OVER);
        layoutUi();
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        menuLayout = UiMenuLayout.layoutSingleButton(game, w, h, retryButton, "RETRY", 2, 0);
    }

    @Override
    public void show() {
        game.setState(GameState.GAME_OVER);
        game.applyAudioSettings();
        game.music.play(game.assets.getMusic(Assets.THEME_AUDIO), 0.12f, true);
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
        game.batch.draw(backGround, 0, 0, w, h);
        UiBatch.drawDimFullscreen(game.batch, game.whitePixel, w, h);

        if (menuLayout != null) {
            float y = menuLayout.titleBaselineY;
            y = McUi.drawTitle(game, game.batch, "Game Over", cx, y);
            McUi.drawSubtitle(game, game.batch, "Try again!", cx, y);
        }

        retryButton.draw(game, game.batch);
        game.batch.end();

        retryButton.clearPressed();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || retryButton.handleClick(game.viewport)) {
            game.resetRun();
            if (game.settings.isAudioEnabled()) {
                game.music.fadeTo(0.3f, 0.5f);
            }
            game.setScreen(new GameScreen(game));
            dispose();
        }
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
