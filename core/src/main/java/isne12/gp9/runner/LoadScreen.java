package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadScreen implements Screen {
    final Main game;
    private final McButton continueButton = new McButton();
    private UiMenuLayout.MenuLayout menuLayout;

    public LoadScreen(final Main game) {
        this.game = game;
        layoutUi();
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        menuLayout = UiMenuLayout.layoutSingleButton(game, w, h, continueButton, "CONTINUE", 2, 0);
    }

    @Override
    public void show() {
        game.setState(GameState.LEVEL_TRANSITION);
        if (game.settings.isAudioEnabled()) {
            game.music.fadeTo(0.2f, 0.5f);
        }
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
            y = McUi.drawTitle(game, game.batch, "Level " + game.getLevel(), cx, y);
            McUi.drawSubtitle(game, game.batch, "Get ready!", cx, y);
        }

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
