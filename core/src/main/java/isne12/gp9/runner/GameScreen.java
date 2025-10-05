package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    // Variables
    final Main game;

    //Resources
    Texture backgroundTexture;
    Music themeAudio;
    Sound gameWinSound;
    Sound gameOverSound;

    //Player
    Player player;


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
        backgroundTexture = new Texture("BG" + game.level + ".png");
        themeAudio = Gdx.audio.newMusic(Gdx.files.internal("themeAudio.mp3"));
        themeAudio.setLooping(true);
        themeAudio.setVolume(0.3f);

        // Player
        player = new Player(new Texture("backView.png"), game);

        // Drop
        bulletTexture = new Texture("rocket.png");
        healthTexture = new Texture("heart.png");
        crystalTexture = new Texture("crystal.png");
        drops = new Array<>();

    }

    @Override
    public void show() {
        themeAudio.play();
    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    private void input() {
        float delta = Gdx.graphics.getDeltaTime();

        //Player's movement
        // move right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight(delta);
        }
        // move left
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.moveLeft(delta);
        }
        // use shield
        else if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            player.activateShield();
        }
    }

    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float delta = Gdx.graphics.getDeltaTime();

        player.update(delta);

        //Drops' Hitbox
        for (int i = drops.size - 1; i >= 0; i--) {
            Drop drop = drops.get(i);
            drop.update(delta);

            if (drop.isOffScreen()) {
                drops.removeIndex(i);
                continue;
            }
            if (drop.overlaps(player.getRectangle())) {
                drop.onCatch(player);
                drops.removeIndex(i);
            }
        }

        dropTimer += delta;
        if (dropTimer > 1f) {
            dropTimer = 0;
            createDrop();
        }

        // check conditions
        if (player.getCrystalCollected() >= 5) {
            if (game.level < 4) {
                game.level++;
                game.setScreen(new LoadScreen(game));
            }
            reset();
        }
        if (player.getHealth() <= 0) {
            game.setScreen(new GameOverScreen(game));
            reset();
        }
    }


    // Custom Methods for game
    // random drops
    private void createDrop() {
        float p = MathUtils.random();
        Drop drop;

        // difficulty increase by level
        float bulletProb = Math.min(0.5f + game.level * 0.05f, 0.9f); // will not be more than 0.9f
        float healthProb = Math.min(0.1f + game.level * 0.02f, 0.35f);

        if (p < bulletProb) drop = new BulletDrop(bulletTexture, game);
        else if (p < bulletProb + healthProb) drop = new HealthDrop(healthTexture, game);
        else drop = new CrystalDrop(crystalTexture, game);

        drops.add(drop);
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
        game.font.draw(game.batch, "Health: " + player.getHealth(), 0, worldHeight);
        game.font.draw(game.batch, "Crystals: " + player.getCrystalCollected(), 0, worldHeight - 0.25f);
        game.font.draw(game.batch, "Level: " + game.level, worldWidth - 0.6f, worldHeight);

        //Player
        player.draw(game.batch);

        //Drops
        for (Drop drop : drops) {
            drop.draw(game.batch);
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

    public void reset() {
        drops.clear();
        player.reset();
        dropTimer = 0;
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        bulletTexture.dispose();
        healthTexture.dispose();
        crystalTexture.dispose();
        player.dispose();
        themeAudio.dispose();
        gameWinSound.dispose();
        gameOverSound.dispose();
    }
}
