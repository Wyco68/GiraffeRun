package isne12.gp9.runner;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Play-screen touch controls drawn on the gameplay batch (world units).
 * Positions come from {@link AdaptiveHudLayout#syncGameplayMetrics} when touch UI is enabled.
 */
public final class TouchControlsOverlay extends InputAdapter {
  private static final float BUTTON_ALPHA = 0.55f;
  private static final float LABEL_ALPHA = 0.65f;
  private static final Color LABEL_COLOR = new Color(
    McStyle.TEXT.r, McStyle.TEXT.g, McStyle.TEXT.b, LABEL_ALPHA);

  private final Main game;
  private final Texture whitePixel;
  private final Runnable onPause;
  private final Player player;
  private final AdaptiveHudLayout hudLayout;

  private final Rectangle leftBounds = new Rectangle();
  private final Rectangle rightBounds = new Rectangle();
  private final Rectangle shieldBounds = new Rectangle();
  private final Rectangle pauseBounds = new Rectangle();
  private final Vector2 touch = new Vector2();

  private float teleportMinWorldY;
  private boolean touchUiEnabled = true;
  private boolean visible = true;
  private boolean leftHeld;
  private boolean rightHeld;
  private boolean shieldPressed;
  private boolean pausePressed;

  public TouchControlsOverlay(Main game, Player player, Runnable onPause, AdaptiveHudLayout hudLayout) {
    this.game = game;
    this.player = player;
    this.onPause = onPause;
    this.hudLayout = hudLayout;
    whitePixel = game.whitePixel;
    applyMetrics(hudLayout.getGameplayMetrics());
  }

  public Main getGame() {
    return game;
  }

  public void enableTouchUi() {
    touchUiEnabled = true;
  }

  public void disableTouchUi() {
    touchUiEnabled = false;
    releaseAllTouches();
  }

  public void applyMetrics(HudLayoutMetrics metrics) {
    if (!touchUiEnabled) {
      leftBounds.set(0, 0, 0, 0);
      rightBounds.set(0, 0, 0, 0);
      shieldBounds.set(0, 0, 0, 0);
      pauseBounds.set(0, 0, 0, 0);
      teleportMinWorldY = game.viewport.getWorldHeight();
      return;
    }
    leftBounds.set(metrics.leftMove);
    rightBounds.set(metrics.rightMove);
    shieldBounds.set(metrics.shieldTouch);
    pauseBounds.set(metrics.pause);
    teleportMinWorldY = metrics.teleportMinWorldY;
  }

  /** True when a world touch should not trigger teleport (bottom UI band or pause button). */
  public boolean blocksTeleport(float worldX, float worldY) {
    if (!touchUiEnabled) {
      return false;
    }
    if (worldY <= teleportMinWorldY) {
      return true;
    }
    return pauseBounds.contains(worldX, worldY);
  }

  public boolean isMovingLeft() {
    return touchUiEnabled && leftHeld;
  }

  public boolean isMovingRight() {
    return touchUiEnabled && rightHeld;
  }

  public void draw(Batch batch) {
    if (!visible || !touchUiEnabled) {
      return;
    }

    McUi.drawTransparentStoneButton(batch, whitePixel, leftBounds, leftHeld,
      McStyle.BUTTON_FACE, McStyle.BUTTON_FACE_PRESSED, BUTTON_ALPHA);
    drawLabel(batch, leftBounds, "<", 1.35f);

    McUi.drawTransparentStoneButton(batch, whitePixel, rightBounds, rightHeld,
      McStyle.BUTTON_FACE, McStyle.BUTTON_FACE_PRESSED, BUTTON_ALPHA);
    drawLabel(batch, rightBounds, ">", 1.35f);

    drawShieldButton(batch);

    if (pauseBounds.width > 0f) {
      McUi.drawTransparentStoneButton(batch, whitePixel, pauseBounds, pausePressed,
        McStyle.BUTTON_FACE, McStyle.BUTTON_FACE_PRESSED, BUTTON_ALPHA);
      drawLabel(batch, pauseBounds, "||", 0.95f);
    }
  }

  private void drawShieldButton(Batch batch) {
    if (shieldBounds.width <= 0f) {
      return;
    }
    boolean onCooldown = player.getShieldCooldownRatio() > 0.001f;
    McUi.drawTransparentStoneButton(batch, whitePixel, shieldBounds, shieldPressed,
      McStyle.SHIELD_FACE, McStyle.SHIELD_FACE_PRESSED, BUTTON_ALPHA);
    drawShieldGlyph(batch, shieldBounds);
    if (onCooldown) {
      float ratio = player.getShieldCooldownRatio();
      Color prev = batch.getColor().cpy();
      batch.setColor(0f, 0f, 0f, 0.35f);
      batch.draw(whitePixel, shieldBounds.x, shieldBounds.y,
        shieldBounds.width, shieldBounds.height * ratio);
      batch.setColor(prev);
    }
  }

  private void drawShieldGlyph(Batch batch, Rectangle bounds) {
    float cx = bounds.x + bounds.width * 0.5f;
    float cy = bounds.y + bounds.height * 0.48f;
    float sw = bounds.width * 0.36f;
    float sh = bounds.height * 0.42f;
    Color prev = batch.getColor().cpy();
    batch.setColor(0.78f, 0.78f, 0.82f, LABEL_ALPHA);
    batch.draw(whitePixel, cx - sw / 2f, cy - sh * 0.35f, sw, sh * 0.7f);
    batch.draw(whitePixel, cx - sw * 0.35f, cy - sh * 0.55f, sw * 0.7f, sh * 0.35f);
    batch.setColor(prev);
  }

  private void drawLabel(Batch batch, Rectangle bounds, String text, float scaleMult) {
    float baseline = bounds.y + bounds.height * 0.38f;
    MenuText.drawCentered(game, batch, text, bounds.x + bounds.width / 2f, baseline,
      scaleMult, LABEL_COLOR);
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (!visible || !touchUiEnabled || pointer > 0) {
      return false;
    }
    unproject(screenX, screenY);

    if (leftBounds.contains(touch)) {
      leftHeld = true;
      return true;
    }
    if (rightBounds.contains(touch)) {
      rightHeld = true;
      return true;
    }
    if (shieldBounds.contains(touch)) {
      shieldPressed = true;
      if (player.canActivateShield()) {
        player.activateShield();
      }
      return true;
    }
    if (pauseBounds.contains(touch)) {
      pausePressed = true;
      onPause.run();
      return true;
    }
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    leftHeld = false;
    rightHeld = false;
    shieldPressed = false;
    pausePressed = false;
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (!visible || !touchUiEnabled || pointer > 0) {
      return false;
    }
    unproject(screenX, screenY);
    if (leftHeld && !leftBounds.contains(touch)) {
      leftHeld = false;
    }
    if (rightHeld && !rightBounds.contains(touch)) {
      rightHeld = false;
    }
    return leftHeld || rightHeld;
  }

  private void unproject(int screenX, int screenY) {
    touch.set(screenX, screenY);
    game.viewport.unproject(touch);
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
    if (!visible) {
      releaseAllTouches();
    }
  }

  private void releaseAllTouches() {
    leftHeld = false;
    rightHeld = false;
    shieldPressed = false;
    pausePressed = false;
  }

  public void dispose() {
  }
}
