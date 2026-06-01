package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {
    private final Main game;
    private final McButton continueButton = new McButton();
    private boolean loadStarted;
    private boolean assetsReady;

    public LoadingScreen(final Main game) {
        this.game = game;
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float btnW = w * 0.5f;
        float btnH = Math.max(UiSpacing.touchTarget(h), 0.5f);
        continueButton.set(cx - btnW / 2f, h * 0.2f, btnW, btnH, "CONTINUE");
    }

    @Override
    public void show() {
        game.setState(GameState.LOADING);
        loadStarted = false;
        assetsReady = false;
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        if (!loadStarted) {
            game.assets.loadAll();
            loadStarted = true;
        }
        game.assets.update();

        if (!assetsReady && game.assets.isLoaded()) {
            game.initUiAfterLoad();
            assetsReady = true;
            layoutUi();
        }

        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.updateMenuFontScale();

        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float progress = assetsReady ? 1f : game.assets.getProgress();

        game.batch.begin();
        game.batch.setColor(Color.WHITE);

        float y = h - UiSpacing.large(h);
        y = McUi.drawTitle(game, game.batch, "GiraffeRun", cx, y);

        float textY = h * 0.48f;
        if (assetsReady) {
            textY = McUi.drawSubtitle(game, game.batch, "Ready!", cx, textY);
            continueButton.draw(game, game.batch);
        } else {
            McUi.drawSubtitle(game, game.batch, "Loading " + Math.round(progress * 100) + "%", cx, textY);
        }
        game.batch.end();

        if (assetsReady) {
            continueButton.clearPressed();
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || continueButton.handleClick(game.viewport)) {
                game.setState(GameState.MENU);
                game.setScreen(new FirstScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.updateMenuFontScale();
        if (assetsReady) {
            layoutUi();
        }
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
