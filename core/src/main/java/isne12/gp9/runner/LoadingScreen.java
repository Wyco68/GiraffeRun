package isne12.gp9.runner;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class LoadingScreen implements Screen {
    private final Main game;
    private final ShapeRenderer shapeRenderer;
    private boolean loadStarted;

    public LoadingScreen(final Main game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        game.setState(GameState.LOADING);
        loadStarted = false;
    }

    @Override
    public void render(float delta) {
        if (!loadStarted) {
            game.assets.loadAll();
            loadStarted = true;
        }

        game.assets.update();

        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();

        float progress = game.assets.getProgress();
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float barWidth = worldWidth * 0.6f;
        float barHeight = 0.25f;
        float barX = (worldWidth - barWidth) / 2f;
        float barY = worldHeight / 2f;

        shapeRenderer.setProjectionMatrix(game.viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(0f, 0.8f, 0.9f, 1f);
        shapeRenderer.rect(barX, barY, barWidth * progress, barHeight);
        shapeRenderer.end();

        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        game.font.draw(game.batch, "Loading... " + Math.round(progress * 100) + "%", barX, barY + barHeight + 0.4f);
        game.batch.end();

        if (game.assets.isLoaded()) {
            game.setState(GameState.MENU);
            game.setScreen(new FirstScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.updateFontScale();
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
        shapeRenderer.dispose();
    }
}
