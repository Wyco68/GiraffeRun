package isne12.gp9.runner.hud;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import isne12.gp9.runner.Fonts;
import isne12.gp9.runner.UiColors;
import isne12.gp9.runner.UiSpacing;
import isne12.gp9.runner.UiText;

/** Shield + teleport icons with cooldown overlay text. */
public final class HudSkillsActor extends Actor {
  private final Texture shieldIcon;
  private final Texture teleportIcon;
  private final Fonts fonts;

  private float shieldCooldown;
  private float teleportCooldown;
  private float iconSizePx = 44f;

  public HudSkillsActor(Texture shieldIcon, Texture teleportIcon, Fonts fonts) {
    this.shieldIcon = shieldIcon;
    this.teleportIcon = teleportIcon;
    this.fonts = fonts;
  }

  public void setCooldowns(float shieldCooldown, float teleportCooldown) {
    this.shieldCooldown = shieldCooldown;
    this.teleportCooldown = teleportCooldown;
  }

  public void setIconSizePx(float iconSizePx) {
    this.iconSizePx = iconSizePx;
  }

  public void layoutSize(float width, float height) {
    setSize(width, height);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    float uiH = getStage().getViewport().getWorldHeight();
    float size = toWorld(uiH, iconSizePx);
    float gap = toWorld(uiH, 8f);
    float x = getX();
    float y = getY();

    batch.draw(shieldIcon, x, y, size, size);
    drawCooldown(batch, shieldCooldown, x, y, size);

    float tx = x + size + gap;
    batch.draw(teleportIcon, tx, y, size, size);
    drawCooldown(batch, teleportCooldown, tx, y, size);
  }

  private void drawCooldown(Batch batch, float cooldown, float iconX, float iconY, float iconSize) {
    if (cooldown <= 0f) {
      return;
    }
    String text = String.valueOf(Math.round(cooldown * 10f) / 10f);
    float cx = iconX + iconSize / 2f;
    float baseline = iconY + iconSize * 0.58f;
    if (batch instanceof com.badlogic.gdx.graphics.g2d.SpriteBatch) {
      UiText.drawCentered((com.badlogic.gdx.graphics.g2d.SpriteBatch) batch, fonts,
        Fonts.Tier.HUD, UiColors.HUD, text, cx, baseline);
    }
  }

  private static float toWorld(float worldHeight, float designPx) {
    return UiSpacing.pxToWorld(designPx, worldHeight);
  }
}
