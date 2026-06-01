package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    final Main game;

    BackGround backGround;
    Music themeAudio;
    Sound gameWinSound;
    Sound gameOverSound;

    Player player;
    Vector2 touchPos;

    Texture bulletTexture;
    Texture healthTexture;
    Texture crystalTexture;
    Array<Drop> drops;
    DropPools dropPools;
    EffectManager effects;
    float dropTimer;

    Texture heartIcon;
    Texture crystalIcon;
    Texture shieldIcon;
    Texture teleportIcon;

    boolean gameEnded;
    boolean paused;
    ShapeRenderer shapeRenderer;

    private final McButton resumeButton = new McButton();
    private final McButton retryButton = new McButton();
    private final McButton menuButton = new McButton();

    public GameScreen(final Main game) {
        this.game = game;
        shapeRenderer = new ShapeRenderer();

        Assets assets = game.assets;
        backGround = new BackGround(assets.getTexture(assets.getLevelBackgroundPath(game.getLevel())));
        gameWinSound = assets.getSound(Assets.GAME_WIN_SOUND);
        gameOverSound = assets.getSound(Assets.GAME_OVER_SOUND);
        themeAudio = assets.getMusic(Assets.THEME_AUDIO);

        player = new Player(assets.getTexture(Assets.BACK_VIEW), game, assets);
        touchPos = new Vector2();

        bulletTexture = assets.getTexture(Assets.ROCKET);
        healthTexture = assets.getTexture(Assets.HEART);
        crystalTexture = assets.getTexture(Assets.CRYSTAL);
        drops = new Array<>();
        dropPools = new DropPools(game, bulletTexture, healthTexture, crystalTexture);
        effects = new EffectManager(game.whitePixel);

        heartIcon = assets.getTexture(Assets.HEART);
        crystalIcon = assets.getTexture(Assets.CRYSTAL);
        shieldIcon = assets.getTexture(Assets.SHIELD_ICON);
        teleportIcon = assets.getTexture(Assets.TELEPORT_ICON);
    }

    @Override
    public void show() {
        game.setState(GameState.PLAYING);
        game.music.setEnabled(game.settings.isMusicEnabled());
        game.music.play(themeAudio, 0.3f, true);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        if (backGround == null) {
            return;
        }

        if (paused) {
            drawPauseFrame();
            handlePauseInput();
            return;
        }

        input(delta);
        logic(delta);
        if (backGround == null) {
            return;
        }
        draw();
    }

    private void input(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            openPause();
            return;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.moveRight(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.moveLeft(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.S)
            || Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            player.activateShield();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
            game.viewport.unproject(touchPos);
            float fromX = player.getCenterX();
            float fromY = player.getCenterY();
            if (player.tryTeleport(touchPos)) {
                effects.spawnTeleport(fromX, fromY, player.getCenterX(), player.getCenterY());
            }
        }
    }

    private void openPause() {
        paused = true;
        game.setState(GameState.PAUSED);
        game.music.pause();
        layoutPauseUi();
    }

    private void layoutPauseUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float gap = UiSpacing.medium(h);
        float btnW = w * 0.42f;
        float btnH = Math.max(UiSpacing.touchTarget(h) * 0.7f, 0.4f);

        float y = h * 0.56f;
        y -= MenuText.lineHeight(game, McUi.TITLE_MULT) + gap * 1.5f;
        resumeButton.set(cx - btnW / 2f, y, btnW, btnH, "RESUME");
        y -= btnH + gap * 0.6f;
        retryButton.set(cx - btnW / 2f, y, btnW, btnH, "RETRY");
        y -= btnH + gap * 0.6f;
        menuButton.set(cx - btnW / 2f, y, btnW, btnH, "MENU");
    }

    private void handlePauseInput() {
        resumeButton.clearPressed();
        retryButton.clearPressed();
        menuButton.clearPressed();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
            || resumeButton.handleClick(game.viewport)) {
            resumeGame();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.R) || retryButton.handleClick(game.viewport)) {
            game.resetRun();
            game.setScreen(new GameScreen(game));
            dispose();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.Q) || menuButton.handleClick(game.viewport)) {
            game.music.stop();
            game.setScreen(new FirstScreen(game));
            dispose();
        }
    }

    private void resumeGame() {
        paused = false;
        game.setState(GameState.PLAYING);
        game.music.resume();
    }

    private void logic(float delta) {
        if (gameEnded) {
            return;
        }

        float worldHeight = game.viewport.getWorldHeight();

        backGround.update(delta, worldHeight, game);
        player.update(delta);
        effects.update(delta);

        for (int i = drops.size - 1; i >= 0; i--) {
            Drop drop = drops.get(i);
            drop.update(delta);

            if (drop.isOffScreen()) {
                dropPools.free(drop);
                drops.removeIndex(i);
                continue;
            }
            if (drop.overlaps(player.getRectangle())) {
                if (drop instanceof CrystalDrop) {
                    effects.spawnCrystalCollect(player.getCenterX(), player.getCenterY());
                }
                drop.onCatch(player);
                dropPools.free(drop);
                drops.removeIndex(i);
            }
        }

        dropTimer += delta;
        if (dropTimer > 1f) {
            dropTimer = 0;
            createDrop();
        }

        if (player.getCrystalCollected() >= 5) {
            endGame(true);
        } else if (player.getHealth() <= 0) {
            endGame(false);
        }
    }

    private void endGame(boolean won) {
        gameEnded = true;
        game.music.fadeTo(0f, 0.5f);

        if (won) {
            gameWinSound.play();
            if (game.getLevel() >= 3) {
                game.gameData.recordRun(3, player.getCrystalCollected());
                game.resetRun();
                game.setState(GameState.GAME_WIN);
                game.setScreen(new GameWinScreen(game));
            } else {
                game.gameData.recordRun(game.getLevel(), player.getCrystalCollected());
                game.advanceLevel();
                game.setState(GameState.LEVEL_TRANSITION);
                game.setScreen(new LoadScreen(game));
            }
        } else {
            game.gameData.recordRun(game.getLevel(), player.getCrystalCollected());
            gameOverSound.play();
            game.setState(GameState.GAME_OVER);
            game.setScreen(new GameOverScreen(game));
        }
    }

    private void createDrop() {
        float p = MathUtils.random();
        Drop drop;

        float bulletProb = Math.min(0.5f + game.getLevel() * 0.05f, 0.9f);
        float healthProb = Math.min(0.1f + game.getLevel() * 0.02f, 0.35f);
        float crystalProb = Math.max(0.15f, 1f - bulletProb - healthProb);
        float scale = 1f - crystalProb;
        bulletProb *= scale;
        healthProb *= scale;

        if (p < bulletProb) drop = dropPools.obtainBullet();
        else if (p < bulletProb + healthProb) drop = dropPools.obtainHealth();
        else drop = dropPools.obtainCrystal();

        drops.add(drop);
    }

    private void drawHud() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        float iconSize = 0.4f;
        float padding = 0.1f;
        float topY = worldHeight - padding;

        for (int i = 0; i < player.getHealth(); i++) {
            game.batch.draw(heartIcon, padding + i * (iconSize + 0.05f), topY - iconSize, iconSize, iconSize);
        }

        float crystalRowY = topY - iconSize;
        float crystalIconX = worldWidth - padding - iconSize;
        String crystalCount = player.getCrystalCollected() + "/5";
        game.font.draw(game.batch, crystalCount, crystalIconX - 0.6f, crystalRowY + iconSize * 0.72f);
        game.batch.draw(crystalIcon, crystalIconX, crystalRowY, iconSize, iconSize);

        game.font.draw(game.batch, "Level: " + game.getLevel(), worldWidth - padding - 1.1f, crystalRowY - 0.15f);

        float cooldownIconSize = 0.8f;
        float cooldownY = crystalRowY - iconSize - 0.25f;

        game.batch.draw(shieldIcon, padding, cooldownY, cooldownIconSize, cooldownIconSize);
        if (player.getShieldCooldown() > 0) {
            String shieldText = "" + Math.round(player.getShieldCooldown() * 10) / 10f;
            game.font.draw(game.batch, shieldText,
                padding + cooldownIconSize / 4f,
                cooldownY + cooldownIconSize / 1.5f);
        }

        float teleportX = padding + cooldownIconSize + 0.1f;
        game.batch.draw(teleportIcon, teleportX, cooldownY, cooldownIconSize, cooldownIconSize);
        if (player.getTeleportCooldown() > 0) {
            String teleportText = "" + Math.round(player.getTeleportCooldown() * 10) / 10f;
            game.font.draw(game.batch, teleportText,
                teleportX + cooldownIconSize / 4f,
                cooldownY + cooldownIconSize / 1.5f);
        }
    }

    private void drawWorld() {
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        backGround.render(game.batch, worldWidth, worldHeight);
        drawHud();
        player.draw(game.batch);

        for (Drop drop : drops) {
            drop.draw(game.batch);
        }

        effects.draw(game.batch);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.updateHudFontScale();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.font.setColor(0f, 0.8f, 0.9f, 1f);

        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        drawWorld();
        game.batch.end();
    }

    private void drawPauseFrame() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.updateHudFontScale();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);

        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        drawWorld();
        game.batch.end();

        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float gap = UiSpacing.medium(h);

        game.updateMenuFontScale();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        UiBatch.drawDimFullscreen(game.batch, game.whitePixel, w, h);

        float y = h * 0.62f;
        y = McUi.drawTitle(game, game.batch, "Paused", cx, y);
        y -= gap;
        McUi.drawSubtitle(game, game.batch, "Game paused", cx, y);
        y -= gap * 1.2f;

        resumeButton.draw(game, game.batch);
        retryButton.draw(game, game.batch);
        menuButton.draw(game, game.batch);

        McUi.drawHint(game, game.batch, "Esc / Enter also resumes", cx, UiSpacing.large(h));
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.updateHudFontScale();
        if (paused) {
            layoutPauseUi();
        }
        if (backGround != null) {
            backGround.onResize(game.viewport.getWorldHeight());
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
        game.music.stop();
    }

    @Override
    public void dispose() {
        hide();
        if (drops != null && dropPools != null) {
            for (Drop drop : drops) {
                dropPools.free(drop);
            }
            drops.clear();
        }
        if (effects != null) {
            effects.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
