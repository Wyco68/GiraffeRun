package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.*;

public class GameScreen implements Screen {
    // Variables
    final Main game;

    //BackGround
    Texture backgroundTexture;

    //Player
    Texture runnerTexture;
    Sprite runnerSprite;
    Rectangle runnerRectangle;


    //Constructor
    public GameScreen(final Main game) {
        this.game = game;

        //Load Resources
        backgroundTexture = new Texture("BG1.png");

        runnerTexture = new Texture(Gdx.files.internal("backView.png"));
        runnerSprite = new Sprite(runnerTexture);
        runnerSprite.setSize(1, 1);
        runnerRectangle = new Rectangle();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    public void input() {
        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime();

        //Player's movement
        //move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            runnerSprite.translateX(speed * delta);
        }
        //move left
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            runnerSprite.translateX(-speed * delta);
        }
    }

    public void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float delta = Gdx.graphics.getDeltaTime();

        //Player's HitBox
        float runnerWidth = runnerSprite.getWidth();
        float runnerHeight = runnerSprite.getHeight();
        runnerSprite.setX(MathUtils.clamp(runnerSprite.getX(), 0, worldWidth - runnerWidth));
        runnerRectangle.set(runnerSprite.getX(), runnerSprite.getY(), runnerWidth, runnerHeight);

    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        //Background
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        //Player
        runnerSprite.draw(game.batch);


        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
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
        backgroundTexture.dispose();
        runnerTexture.dispose();
    }
}
