package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    // Variables
    final Main game;

    //Resources
    BackGround backGround;
    Music themeAudio;
    Sound gameWinSound;
    Sound gameOverSound;

    //Player
    Player player;
    Vector2 touchPos;


    // Drops
    Texture bulletTexture;
    Texture healthTexture;
    Texture crystalTexture;
    Array<Drop> drops;
    float dropTimer;

    // UI
    Texture heartIcon;
    Texture crystalIcon;
    Texture shieldIcon;
    Texture teleportIcon;

    // Constructor
    public GameScreen(final Main game) {
        this.game = game;

        //// Load Resources
        backGround = new BackGround("BG" + game.level + ".png");
        gameWinSound = Gdx.audio.newSound(Gdx.files.internal("gameWinSound.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOverSound.mp3"));
        themeAudio = Gdx.audio.newMusic(Gdx.files.internal("themeAudio.mp3"));
        themeAudio.setLooping(true);
        themeAudio.setVolume(0.3f);

        // Player
        player = new Player(new Texture("backView.png"), game);
        touchPos = new Vector2();

        // Drop
        bulletTexture = new Texture("rocket.png");
        healthTexture = new Texture("heart.png");
        crystalTexture = new Texture("crystal.png");
        drops = new Array<>();

        // UI
        heartIcon = new Texture("heart.png");
        crystalIcon = new Texture("crystal.png");
        shieldIcon = new Texture("shieldIcon.png");
        teleportIcon = new Texture("teleportIcon.png");

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
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            player.activateShield();
        }

        // use teleport
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            player.teleport(touchPos);
        }
    }

    private void logic() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float delta = Gdx.graphics.getDeltaTime();

        backGround.update(delta, worldHeight, game);
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
            gameWinSound.play();
            if (game.level >= 3) {
                game.level = 1; // to play again from the beginning again
                game.setScreen(new GameWinScreen(game));
            } else {
                game.level++;
                game.setScreen(new LoadScreen(game));
            }
        }
        if (player.getHealth() <= 0) {
            gameOverSound.play();
            game.setScreen(new GameOverScreen(game));
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
        backGround.render(game.batch, worldWidth, worldHeight);

        float iconSize = 0.4f;
        float padding = 0.1f;

        // Health (top-left) ---
        for (int i = 0; i < player.getHealth(); i++) {
            game.batch.draw(heartIcon, padding + i * (iconSize + 0.05f), worldHeight - iconSize - padding, iconSize, iconSize);
        }

        // CrystalCollected (top-right) ---
        float crystalX = worldWidth - iconSize - 1f;
        float crystalY = worldHeight - iconSize - padding - 0.5f;
        game.batch.draw(crystalIcon, crystalX, crystalY, iconSize, iconSize);
        game.font.draw(game.batch, "x " + player.getCrystalCollected(), crystalX + iconSize + 0.1f, crystalY + iconSize - 0.1f);

        // Level
        game.font.draw(game.batch, "Level: " + game.level, worldWidth - 1f, worldHeight);

        // Cooldown Icons
        float cooldownIconSize = 0.8f;
        float cooldownY = worldHeight - iconSize - padding - iconSize - 0.5f; // just below hearts

        // Shield Icon
        game.batch.draw(shieldIcon, padding, cooldownY, cooldownIconSize, cooldownIconSize);
        if (player.getShieldCooldown() > 0) {
            String shieldText = "" + Math.round(player.getShieldCooldown() * 10) / 10f;
            game.font.draw(game.batch, shieldText,
                padding + cooldownIconSize / 4f,
                cooldownY + cooldownIconSize / 1.5f);
        }

        // Teleport Icon (next to shield)
        float teleportX = padding + cooldownIconSize + 0.1f;
        game.batch.draw(teleportIcon, teleportX, cooldownY, cooldownIconSize, cooldownIconSize);
        if (player.getTeleportCooldown() > 0) {
            String teleportText = "" + Math.round(player.getTeleportCooldown() * 10) / 10f;
            game.font.draw(game.batch, teleportText,
                teleportX + cooldownIconSize / 4f,
                cooldownY + cooldownIconSize / 1.5f);
        }

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
        dispose();
    }

    public void reset() {
        drops.clear();
        player.reset();
        dropTimer = 0;
    }

    @Override
    public void dispose() {
        backGround.dispose();
        bulletTexture.dispose();
        healthTexture.dispose();
        crystalTexture.dispose();
        heartIcon.dispose();
        crystalIcon.dispose();
        player.dispose();
        themeAudio.dispose();
        gameWinSound.dispose();
        gameOverSound.dispose();
    }
}
