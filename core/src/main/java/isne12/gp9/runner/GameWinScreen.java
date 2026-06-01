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

    public GameWinScreen(final Main game) {
        this.game = game;
        backGround = game.assets.getTexture(Assets.GAME_WIN);
        layoutUi();
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float gap = UiSpacing.medium(h);
        float btnW = w * 0.5f;
        float btnH = Math.max(UiSpacing.touchTarget(h), 0.5f);

        float y = h * 0.58f;
        y -= MenuText.lineHeight(game, McUi.TITLE_MULT) + gap;
        y -= MenuText.lineHeight(game, McUi.SUBTITLE_MULT) + gap;
        float btnY = UiBounds.clampY(y - btnH, btnH, h);
        retryButton.set(UiBounds.clampX(cx - btnW / 2f, btnW, w, h), btnY, btnW, btnH, "PLAY AGAIN");
    }

    @Override
    public void show() {
        game.setState(GameState.GAME_WIN);
        game.music.setEnabled(game.settings.isMusicEnabled());
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
        float gap = UiSpacing.medium(h);

        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(backGround, 0, 0, w, h);
        UiBatch.drawDimFullscreen(game.batch, game.whitePixel, w, h);

        float y = h * 0.62f;
        y = McUi.drawTitle(game, game.batch, "You Win!", cx, y);
        y -= gap;
        McUi.drawSubtitle(game, game.batch, "High score " + game.gameData.getHighScore(), cx, y);
        retryButton.draw(game, game.batch);
        game.batch.end();

        retryButton.clearPressed();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || retryButton.handleClick(game.viewport)) {
            game.music.fadeTo(0.3f, 0.5f);
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
