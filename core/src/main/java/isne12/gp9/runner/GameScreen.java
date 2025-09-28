package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    // Variables
    final Main game;
    int level = 1;

    //BackGround
    Texture backgroundTexture;

    //Player
    Texture runnerTexture;
    Sprite runnerSprite;
    Rectangle runnerRectangle;
    int runnerHealth = 5;
    int crystalCollected = 0;

    // Drops
    Texture bulletTexture;
    Texture healthTexture;
    Texture crystalTexture;
    Array<Drop> drops;
    float dropTimer;


    // Constructor
    public GameScreen(final Main game) {
        this.game = game;

        //// Load Resources
        // BG
        backgroundTexture = new Texture("BG1.png");

        // Player
        runnerTexture = new Texture("backView.png");
        runnerSprite = new Sprite(runnerTexture);
        runnerSprite.setSize(1, 1);
        runnerRectangle = new Rectangle();

        // Drop
        bulletTexture = new Texture("rocket.png");
        healthTexture = new Texture("heart.png");
        crystalTexture = new Texture("crystal.png");
        drops = new Array<>();

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

    private void input() {
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

    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float delta = Gdx.graphics.getDeltaTime();

        //Player's HitBox
        float runnerWidth = runnerSprite.getWidth();
        float runnerHeight = runnerSprite.getHeight();
        runnerSprite.setX(MathUtils.clamp(runnerSprite.getX(), 0, worldWidth - runnerWidth));
        runnerRectangle.set(runnerSprite.getX(), runnerSprite.getY(), runnerWidth, runnerHeight);

        //Drops' Hitbox
        for (int i = drops.size - 1; i >= 0; i--) {
            Drop drop = drops.get(i);
            drop.update(delta);

            if (drop.getSprite().getY() < -drop.getSprite().getHeight()) { // out of screen
                drops.removeIndex(i);
            } else if (runnerRectangle.overlaps(drop.getRectangle())) { // player hit the drop
                drop.onCatch(this); // do the work define for each drop type
                drops.removeIndex(i);
            }
        }

        dropTimer += delta;
        if (dropTimer > 1f) {
            dropTimer = 0;
            createDrop();
        }

    }

    private void createDrop() {
        float dropWidth = 1;
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float xPos = MathUtils.random(0F, worldWidth - dropWidth);

        //occur chances for each drop // don't like it yet, fix later
        float rand = MathUtils.random();
        float bulletChance = 0.5f + 0.1f * (level - 1);
        float crystalChance = 0.3f - 0.1f * (level - 1);

        if (rand < bulletChance) {
            drops.add(new BulletDrop(bulletTexture, xPos, worldHeight));
        } else if (rand < bulletChance + crystalChance) {
            drops.add(new CrystalDrop(crystalTexture, xPos, worldHeight));
        } else {
            drops.add(new HealthDrop(healthTexture, xPos, worldHeight));
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();

        //Background
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        game.batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);

        //Fonts - replace with UI later
        game.font.draw(game.batch, "Health: " + runnerHealth, 0, worldHeight);
        game.font.draw(game.batch, "Crystals: " + crystalCollected, 0, worldHeight - 0.25f);
        game.font.draw(game.batch, "Level: " + level, worldWidth - 0.6f, worldHeight);

        //Player
        runnerSprite.draw(game.batch);

        //Drops
        for (Drop drop : drops) {
            drop.getSprite().draw(game.batch);
        }


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
        bulletTexture.dispose();
        healthTexture.dispose();
        crystalTexture.dispose();
    }
}
