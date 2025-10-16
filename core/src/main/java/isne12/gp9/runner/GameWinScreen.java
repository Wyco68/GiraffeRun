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

    public GameWinScreen(final Main game) {
        this.game = game;
        backGround = new Texture(Gdx.files.internal("MenuScreen.png"));
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        game.batch.draw(backGround, 0, 0, game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
        game.font.draw(game.batch, "Congratuation for winning RunGiraffeRun!", 0.5f, 1.5f);
        game.font.draw(game.batch, "Enter Space if you want to Play Again", 0.5f, 1);


        game.batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        backGround.dispose();
    }
}
