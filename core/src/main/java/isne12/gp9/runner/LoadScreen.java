package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadScreen implements Screen {
    final Main game;
    Texture backGround;
    private final McButton continueButton = new McButton();

    public LoadScreen(final Main game) {
        this.game = game;
        backGround = game.assets.getTexture(Assets.LOAD);
        layoutUi();
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float gap = UiSpacing.medium(h);
        float btnW = w * 0.48f;
        float btnH = Math.max(UiSpacing.touchTarget(h), 0.5f);

        float y = h * 0.5f;
        y -= MenuText.lineHeight(game, McUi.TITLE_MULT) + gap;
        y -= MenuText.lineHeight(game, McUi.SUBTITLE_MULT) + gap;
        float btnY = UiBounds.clampY(y - btnH, btnH, h);
        continueButton.set(UiBounds.clampX(cx - btnW / 2f, btnW, w, h), btnY, btnW, btnH, "CONTINUE");
    }

    @Override
    public void show() {
        game.setState(GameState.LEVEL_TRANSITION);
        game.music.fadeTo(0.2f, 0.5f);
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
        y = McUi.drawTitle(game, game.batch, "Level " + game.getLevel(), cx, y);
        y -= gap;
        McUi.drawSubtitle(game, game.batch, "Get ready!", cx, y);
        continueButton.draw(game, game.batch);
        game.batch.end();

        continueButton.clearPressed();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
            || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || continueButton.handleClick(game.viewport)) {
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
