package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {
    private final Main game;
    private final McButton continueButton = new McButton();
    private UiMenuLayout.MenuLayout menuLayout;
    private boolean loadStarted;
    private boolean assetsReady;

    public LoadingScreen(final Main game) {
        this.game = game;
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        menuLayout = UiMenuLayout.layoutSingleButton(game, w, h, continueButton, "CONTINUE", 2, 0);
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
            game.assets.loadMenuSplash();
            game.assets.loadAll();
            loadStarted = true;
        }
        if (loadStarted && game.assets.getProgress() >= 0.55f) {
            game.assets.loadDeferredTextures();
        }
        game.assets.update();

        if (!assetsReady && game.assets.isLoaded()) {
            if (game.assets.hasLoadErrors()) {
                WebLog.error("LoadingScreen", "Some assets failed to load; check console.");
            }
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

        if (game.assets.isMenuSplashLoaded()) {
            Texture menuBg = game.assets.getTexture(Assets.MENU);
            game.batch.draw(menuBg, 0f, 0f, w, h);
        }

        if (assetsReady && menuLayout != null) {
            float y = menuLayout.titleBaselineY;
            y = McUi.drawTitle(game, game.batch, "GiraffeRun", cx, y);
            McUi.drawSubtitle(game, game.batch, "Ready!", cx, y);
            continueButton.draw(game, game.batch);
        } else {
            float mid = (UiBounds.safeBottom(h) + UiBounds.safeTop(h)) * 0.5f;
            float y = mid + MenuText.lineHeight(game, McUi.TITLE_MULT) * 0.5f;
            y = McUi.drawTitle(game, game.batch, "GiraffeRun", cx, y);
            McUi.drawSubtitle(game, game.batch, "Loading " + Math.round(progress * 100) + "%", cx, y);
        }
        game.batch.end();

        if (assetsReady) {
            continueButton.clearPressed();
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)
                || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                || continueButton.handleClick(game.viewport)) {
                game.setState(GameState.MENU);
                game.setScreenAndDispose(new FirstScreen(game));
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
