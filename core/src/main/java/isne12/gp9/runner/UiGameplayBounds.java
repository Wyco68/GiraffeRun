package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Maps the gameplay viewport screen rect into another viewport's world insets. */
public final class UiGameplayBounds {
  public final float insetLeft;
  public final float insetRight;
  public final float insetBottom;
  public final float insetTop;
  public final float contentWidth;
  public final float contentHeight;

  private UiGameplayBounds(float insetLeft, float insetRight, float insetBottom, float insetTop,
                           float contentWidth, float contentHeight) {
    this.insetLeft = insetLeft;
    this.insetRight = insetRight;
    this.insetBottom = insetBottom;
    this.insetTop = insetTop;
    this.contentWidth = contentWidth;
    this.contentHeight = contentHeight;
  }

  public static UiGameplayBounds compute(Viewport gameplay, Viewport ui) {
    float uiW = ui.getWorldWidth();
    float uiH = ui.getWorldHeight();
    int sw = Gdx.graphics.getWidth();
    int sh = Gdx.graphics.getHeight();
    if (sw <= 0 || sh <= 0 || gameplay == null) {
      return new UiGameplayBounds(0f, 0f, 0f, 0f, uiW, uiH);
    }
    float left = (float) gameplay.getScreenX() / sw * uiW;
    float right = (float) (gameplay.getScreenX() + gameplay.getScreenWidth()) / sw * uiW;
    float bottom = (float) gameplay.getScreenY() / sh * uiH;
    float top = (float) (gameplay.getScreenY() + gameplay.getScreenHeight()) / sh * uiH;

    float insetL = Math.max(0f, left);
    float insetR = Math.max(0f, uiW - right);
    float insetB = Math.max(0f, bottom);
    float insetT = Math.max(0f, uiH - top);
    return new UiGameplayBounds(insetL, insetR, insetB, insetT,
      Math.max(0f, uiW - insetL - insetR), Math.max(0f, uiH - insetB - insetT));
  }
}
