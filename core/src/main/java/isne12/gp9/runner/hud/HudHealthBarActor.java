package isne12.gp9.runner.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import isne12.gp9.runner.Player;
import isne12.gp9.runner.UiColors;
import isne12.gp9.runner.UiSpacing;

/** Five-segment health bar for adaptive HUD tables. */
public final class HudHealthBarActor extends Actor {
  private static final int SEGMENTS = Player.MAX_HEALTH;

  private final Texture whitePixel;
  private int health = Player.MAX_HEALTH;
  private float barHeightPx = 12f;

  public HudHealthBarActor(Texture whitePixel) {
    this.whitePixel = whitePixel;
  }

  public void setHealth(int health) {
    this.health = Math.max(0, Math.min(SEGMENTS, health));
  }

  public void setBarHeightPx(float barHeightPx) {
    this.barHeightPx = barHeightPx;
  }

  public void layoutSize(float width, float height) {
    setSize(width, height);
  }

  private static float toWorld(float worldHeight, float designPx) {
    return UiSpacing.pxToWorld(designPx, worldHeight);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    float uiH = getStage().getViewport().getWorldHeight();
    float barH = toWorld(uiH, barHeightPx);
    float gap = barH * 0.15f;
    float border = barH * 0.15f;
    float totalW = getWidth();
    float segW = (totalW - gap * (SEGMENTS - 1)) / SEGMENTS;
    float x = getX();
    float y = getY();

    Color old = batch.getColor().cpy();
    batch.setColor(UiColors.BAR_BG.r, UiColors.BAR_BG.g, UiColors.BAR_BG.b,
      UiColors.BAR_BG.a * parentAlpha);
    batch.draw(whitePixel, x - border, y - border, totalW + border * 2f, barH + border * 2f);

    for (int i = 0; i < SEGMENTS; i++) {
      float sx = x + i * (segW + gap);
      Color seg = i < health ? UiColors.HEALTH_LOW : UiColors.BAR_EMPTY;
      batch.setColor(seg.r, seg.g, seg.b, seg.a * parentAlpha);
      batch.draw(whitePixel, sx, y, segW, barH);
    }
    batch.setColor(old);
  }
}
