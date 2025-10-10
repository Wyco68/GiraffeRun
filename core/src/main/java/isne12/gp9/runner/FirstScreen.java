package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {
    final Main game;
    Texture backGround;

    public FirstScreen(final Main game) {
        this.game = game;
        backGround = new Texture("MenuScreen.png");
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
        float y = 4.5f;      // starting Y position
        float spacing = 0.6f; // space between lines
        game.font.draw(game.batch, "Welcome to GiraffeRun!", 1, y);
        y -= spacing;
        game.font.draw(game.batch, "S - Move Left", 1, y);
        y -= spacing;
        game.font.draw(game.batch, "D - Move Right", 1, y);
        y -= spacing;
        game.font.draw(game.batch, "A - Activate Shield", 1, y);
        y -= spacing;
        game.font.draw(game.batch, "Use Cursor and Click - Teleport", 1, y);
        y -= spacing;
        game.font.draw(game.batch, "Tap Anywhere or Space to Begin", 1, y);


        game.batch.end();

        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
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
        // Destroy screen's assets here.
        backGround.dispose();
    }
}
