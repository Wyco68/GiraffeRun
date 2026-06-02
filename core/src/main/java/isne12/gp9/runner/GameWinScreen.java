package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameWinScreen implements Screen {
    final Main game;
    Texture backGround;
    private final McButton retryButton = new McButton();
    private UiMenuLayout.MenuLayout menuLayout;

    public GameWinScreen(final Main game) {
        this.game = game;
        backGround = game.assets.getTextureOrLoad(Assets.GAME_WIN);
        layoutUi();
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        menuLayout = UiMenuLayout.layoutSingleButton(game, w, h, retryButton, "PLAY AGAIN", 2, 0);
    }

    @Override
    public void show() {
        game.setState(GameState.GAME_WIN);
        game.applyAudioSettings();
        game.music.play(game.assets.getMusic(Assets.THEME_AUDIO), 0.2f, true);
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
        if (backGround != null) {
            game.batch.draw(backGround, 0, 0, w, h);
        }
        UiBatch.drawDimFullscreen(game.batch, game.whitePixel, w, h);

        if (menuLayout != null) {
            float y = menuLayout.titleBaselineY;
            y = McUi.drawTitle(game, game.batch, "You Win!", cx, y);
            McUi.drawSubtitle(game, game.batch, "All levels complete!", cx, y);
        }

        retryButton.draw(game, game.batch);
        game.batch.end();

        retryButton.clearPressed();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || retryButton.handleClick(game.viewport)) {
            if (game.settings.isAudioEnabled()) {
                game.music.fadeTo(0.3f, 0.5f);
            }
            game.setScreenAndDispose(new GameScreen(game));
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
