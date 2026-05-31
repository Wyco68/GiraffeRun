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

    public GameOverScreen(final Main game) {
        this.game = game;
        backGround = game.assets.getTexture(Assets.GAME_OVER);
    }

    @Override
    public void show() {
        game.setState(GameState.GAME_OVER);
        game.music.play(game.assets.getMusic(Assets.THEME_AUDIO), 0.12f, true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        game.batch.draw(backGround, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        game.font.draw(game.batch, "Game Over!", 0.5f, 1f);
        game.font.draw(game.batch, "Score: " + game.gameData.getHighScore()
            + "  Best Level: " + game.gameData.getBestLevel(), 0.5f, 0.75f);
        game.font.draw(game.batch, "Press SPACE to Restart", 0.5f, 0.5f);

        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.resetRun();
            game.music.fadeTo(0.3f, 0.5f);
            game.setScreen(new GameScreen(game));
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
    }
}
