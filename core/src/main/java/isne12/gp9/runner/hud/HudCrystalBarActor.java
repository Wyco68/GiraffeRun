package isne12.gp9.runner.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import isne12.gp9.runner.UiColors;
import isne12.gp9.runner.UiSpacing;

/** Crystal progress bar with icon — animated fill. */
public final class HudCrystalBarActor extends Actor {
  private static final Color TMP = new Color();
  private final Texture crystalIcon;
  private final Texture whitePixel;

  private float displayProgress;
  private float barWidthPx = 64f;
  private float barHeightPx = 6f;

  public HudCrystalBarActor(Texture crystalIcon, Texture whitePixel) {
    this.crystalIcon = crystalIcon;
    this.whitePixel = whitePixel;
  }

  public float getDisplayProgress() {
    return displayProgress;
  }

  public void setDisplayProgress(float progress) {
    displayProgress = MathUtils.clamp(progress, 0f, 1f);
  }

  public void setBarSizePx(float barWidthPx, float barHeightPx) {
    this.barWidthPx = barWidthPx;
    this.barHeightPx = barHeightPx;
  }

  public void layoutSize(float width, float height) {
    setSize(width, height);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    float uiH = getStage().getViewport().getWorldHeight();
    float barH = toWorld(uiH, barHeightPx);
    float barW = toWorld(uiH, barWidthPx);
    float icon = barH * 1.2f;
    float iconGap = toWorld(uiH, 2f);
    float x = getX();
    float y = getY();

    TMP.set(batch.getColor());
    batch.setColor(UiColors.BAR_BG.r, UiColors.BAR_BG.g, UiColors.BAR_BG.b,
      UiColors.BAR_BG.a * parentAlpha);
    batch.draw(whitePixel, x + icon + iconGap, y, barW, barH);

    float fillW = barW * displayProgress;
    batch.setColor(UiColors.CRYSTAL_FILL.r, UiColors.CRYSTAL_FILL.g, UiColors.CRYSTAL_FILL.b,
      UiColors.CRYSTAL_FILL.a * parentAlpha);
    if (fillW > 0.001f) {
      batch.draw(whitePixel, x + icon + iconGap, y, fillW, barH);
    }
    batch.setColor(TMP);

    batch.draw(crystalIcon, x, y - toWorld(uiH, 1f), icon, icon);
  }

  private static float toWorld(float worldHeight, float designPx) {
    return UiSpacing.pxToWorld(designPx, worldHeight);
  }
}
