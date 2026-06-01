package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import isne12.gp9.runner.hud.HudCrystalBarActor;

/**
 * Facade for in-game adaptive HUD (Scene2D) + gameplay touch overlay sync.
 * Rebuilds layout on resize only.
 */
public final class AdaptiveGameUi {
  private static final float CRYSTAL_LERP = 8f;

  private final Main game;
  private final AdaptiveHudLayout hudLayout;
  private final TouchControlsOverlay touchControls;
  private boolean hudVisible = true;

  public AdaptiveGameUi(Main game, Player player, Texture shieldIcon, Texture teleportIcon,
                        Texture crystalIcon, Runnable onPause) {
    this.game = game;
    hudLayout = new AdaptiveHudLayout(game, shieldIcon, teleportIcon, crystalIcon);
    touchControls = new TouchControlsOverlay(game, player, onPause, hudLayout);
    applyInputMode();
    syncLayout();
  }

  public AdaptiveHudLayout getHudLayout() {
    return hudLayout;
  }

  public TouchControlsOverlay getTouchControls() {
    return touchControls;
  }

  public ViewportStage getUiStage() {
    return hudLayout.getStage();
  }

  public void resize(int screenWidth, int screenHeight) {
    hudLayout.resize(screenWidth, screenHeight);
    applyInputMode();
    syncLayout();
  }

  public void rebuildLayout() {
    hudLayout.rebuildLayout();
    applyInputMode();
    syncLayout();
  }

  private void applyInputMode() {
    if (hudLayout.getProfile().touchUiEnabled()) {
      touchControls.enableTouchUi();
    } else {
      touchControls.disableTouchUi();
    }
  }

  private void syncLayout() {
    hudLayout.syncGameplayMetrics(game.viewport.getWorldWidth(), game.viewport.getWorldHeight());
    touchControls.applyMetrics(hudLayout.getGameplayMetrics());
  }

  public void update(float delta, Player player) {
    HudCrystalBarActor crystal = hudLayout.getCrystalBar();
    float target = player.getCrystalCollected() / (float) Player.CRYSTALS_TO_WIN;
    crystal.setDisplayProgress(
      MathUtils.lerp(crystal.getDisplayProgress(), target, Math.min(1f, delta * CRYSTAL_LERP)));

    hudLayout.getHealthBar().setHealth(player.getHealth());
    hudLayout.getSkills().setCooldowns(player.getShieldCooldown(), player.getTeleportCooldown());
  }

  public void setHudVisible(boolean visible) {
    hudVisible = visible;
  }

  public void drawHudStage() {
    if (!hudVisible) {
      return;
    }
    ViewportStage uiStage = hudLayout.getStage();
    float worldH = game.viewport.getWorldHeight();
    game.fonts.updateHudScale(worldH * hudLayout.getProfile().uiScale);
    uiStage.getViewport().apply();
    uiStage.act(Gdx.graphics.getDeltaTime());
    uiStage.draw();
  }

  public void drawTouchOverlay(SpriteBatch batch) {
    touchControls.draw(batch);
  }

  public void setTouchVisible(boolean visible) {
    touchControls.setVisible(visible);
  }

  public void dispose() {
    touchControls.dispose();
    hudLayout.getStage().dispose();
  }
}
