package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class FirstScreen implements Screen {
    final Main game;
    Texture backGround;

    private final McButton playButton = new McButton();
    private final McButton soundButton = new McButton();

    public FirstScreen(final Main game) {
        this.game = game;
        backGround = game.assets.getTexture(Assets.MENU);
        layoutUi();
    }

    private void layoutUi() {
        game.updateMenuFontScale();
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float gap = UiSpacing.medium(h);
        float btnW = w * 0.48f;
        float btnH = Math.max(UiSpacing.touchTarget(h), 0.5f);

        float y = h - UiSpacing.large(h);
        y -= MenuText.lineHeight(game, McUi.TITLE_MULT) + gap;
        y -= MenuText.lineHeight(game, McUi.SUBTITLE_MULT) + gap * 1.5f;

        float playY = UiBounds.clampY(y - btnH, btnH, h);
        playButton.set(UiBounds.clampX(cx - btnW / 2f, btnW, w, h), playY, btnW, btnH, "PLAY");

        float sndW = w * 0.2f;
        float sndH = btnH * 0.62f;
        float sndX = UiBounds.clampX(UiBounds.safeRight(w, h) - sndW, sndW, w, h);
        float sndY = UiBounds.clampY(UiBounds.safeTop(h) - sndH, sndH, h);
        soundButton.set(sndX, sndY, sndW, sndH,
            game.settings.isMusicEnabled() ? "SOUND" : "MUTE");
    }

    @Override
    public void show() {
        game.setState(GameState.MENU);
        game.music.setEnabled(game.settings.isMusicEnabled());
        game.music.play(game.assets.getMusic(Assets.THEME_AUDIO), 0.25f, true);
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.updateMenuFontScale();

        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float cx = w / 2f;
        float gap = UiSpacing.medium(h);

        game.batch.begin();
        game.batch.setColor(Color.WHITE);
        game.batch.draw(backGround, 0, 0, w, h);

        float y = h - UiSpacing.large(h);
        y = McUi.drawTitle(game, game.batch, "GiraffeRun", cx, y);
        y -= gap;
        y = McUi.drawSubtitle(game, game.batch, "Collect 5 crystals per level", cx, y);
        y -= gap * 1.2f;

        playButton.draw(game, game.batch);
        soundButton.draw(game, game.batch);

        float hintsY = playButton.getBounds().y - gap;
        hintsY = McUi.drawHint(game, game.batch,
            "A/D move  S shield  Tap field to teleport  On-screen buttons on mobile", cx, hintsY);
        McUi.drawHint(game, game.batch,
            "High " + game.gameData.getHighScore() + "  Best Lv " + game.gameData.getBestLevel(),
            cx, hintsY - gap * 0.5f);
        game.batch.end();

        playButton.clearPressed();
        soundButton.clearPressed();

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || playButton.handleClick(game.viewport)) {
            beginGame();
        } else if (soundButton.handleClick(game.viewport)) {
            game.settings.toggleMusic();
            game.music.setEnabled(game.settings.isMusicEnabled());
            layoutUi();
        }
    }

    private void beginGame() {
        game.music.fadeTo(0.3f, 0.5f);
        game.setScreen(new GameScreen(game));
        dispose();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
        game.updateMenuFontScale();
        layoutUi();
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
