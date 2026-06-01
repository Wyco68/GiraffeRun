package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import isne12.gp9.runner.hud.HudCrystalBarActor;
import isne12.gp9.runner.hud.HudHealthBarActor;
import isne12.gp9.runner.hud.HudSkillsActor;
import isne12.gp9.runner.hud.TouchSlotActor;

/**
 * HUD in gameplay world space (8×5). Health + skills are pinned to the top corners;
 * bottom chrome uses a {@link Table} on the stage.
 */
public final class AdaptiveHudLayout {
  private final Main game;
  private final ViewportStage stage;
  private final Table root;
  private final HudHealthBarActor healthBar;
  private final HudSkillsActor skills;
  private final HudCrystalBarActor crystalBar;

  private TouchSlotActor touchLeft;
  private TouchSlotActor touchRight;
  private TouchSlotActor touchShield;
  private TouchSlotActor touchPause;

  private UiScreenProfile profile = UiScreenProfile.fromSize(1280, 720);
  private final HudLayoutMetrics gameplayMetrics = new HudLayoutMetrics();

  public AdaptiveHudLayout(Main game, Texture shieldIcon, Texture teleportIcon, Texture crystalIcon) {
    this.game = game;
    stage = new ViewportStage();
    root = UiFactory.screenRoot();
    stage.addActor(root);

    healthBar = new HudHealthBarActor(game.whitePixel);
    skills = new HudSkillsActor(shieldIcon, teleportIcon, game.fonts);
    crystalBar = new HudCrystalBarActor(crystalIcon, game.whitePixel);

    rebuildLayout();
  }

  public ViewportStage getStage() {
    return stage;
  }

  public UiScreenProfile getProfile() {
    return profile;
  }

  public HudLayoutMetrics getGameplayMetrics() {
    return gameplayMetrics;
  }

  public HudHealthBarActor getHealthBar() {
    return healthBar;
  }

  public HudCrystalBarActor getCrystalBar() {
    return crystalBar;
  }

  public HudSkillsActor getSkills() {
    return skills;
  }

  public void resize(int screenWidth, int screenHeight) {
    stage.resize(screenWidth, screenHeight);
    profile = stage.getProfile();
    rebuildLayout();
  }

  public void rebuildLayout() {
    profile = stage.getProfile();
    root.clear();
    detach(healthBar);
    detach(skills);
    touchLeft = null;
    touchRight = null;
    touchShield = null;
    touchPause = null;

    float worldH = game.viewport.getWorldHeight();
    float worldW = game.viewport.getWorldWidth();
    float pad = UiBounds.margin(worldH);

    root.padLeft(pad);
    root.padRight(pad);
    root.padBottom(pad);
    root.padTop(0f);
    root.bottom().left();

    TopHudSizes top = computeTopHudSizes(worldW, worldH);
    pinTopHud(worldW, worldH, top);

    if (profile.isMobile) {
      buildMobileBottom(root, worldW, worldH, top);
    } else {
      buildDesktopBottom(root, worldW, worldH);
    }
    root.layout();

    stage.addActor(healthBar);
    stage.addActor(skills);
    healthBar.toFront();
    skills.toFront();
  }

  private static void detach(Actor actor) {
    if (actor.getParent() != null) {
      actor.remove();
    }
  }

  private static final class TopHudSizes {
    float barW;
    float barH;
    float skillsW;
    float iconH;
  }

  private TopHudSizes computeTopHudSizes(float worldW, float worldH) {
    TopHudSizes s = new TopHudSizes();
    s.barW = Math.min(UiSpacing.pxToWorld(128f, worldH), worldW * 0.45f);
    s.barH = UiSpacing.pxToWorld(14f, worldH);
    float icon = UiSpacing.pxToWorld(40f, worldH);
    s.skillsW = Math.min(icon * 2f + UiSpacing.small(worldH), worldW * 0.36f);
    s.iconH = icon;
    healthBar.layoutSize(s.barW, s.barH);
    skills.layoutSize(s.skillsW, s.iconH);
    return s;
  }

  /** Scene2D position is bottom-left; pin tops to {@link UiBounds#safeTop}. */
  private void pinTopHud(float worldW, float worldH, TopHudSizes top) {
    float topEdge = UiBounds.safeTop(worldH);
    float left = UiBounds.safeLeft(worldH);
    float right = UiBounds.safeRight(worldW, worldH);

    healthBar.setPosition(left, topEdge - top.barH);
    skills.setPosition(right - top.skillsW, topEdge - top.iconH);
  }

  private void buildMobileBottom(Table root, float worldW, float worldH, TopHudSizes top) {
    float touch = profile.gameplayTouchSize(worldH);
    float crystalH = UiSpacing.pxToWorld(18f, worldH);
    float crystalW = Math.min(UiSpacing.pxToWorld(88f, worldH), worldW * 0.35f);
    crystalBar.layoutSize(crystalW, crystalH);

    touchPause = new TouchSlotActor(touch * 0.85f);
    touchLeft = new TouchSlotActor(touch);
    touchRight = new TouchSlotActor(touch);
    touchShield = new TouchSlotActor(touch);

    Table leftBottom = new Table();
    leftBottom.add(touchPause).size(touchPause.getWidth(), touchPause.getHeight()).left();
    leftBottom.row();
    leftBottom.add(touchLeft).size(touch);
    leftBottom.add(touchRight).size(touch);

    root.add(leftBottom).left().bottom();
    root.add(crystalBar).size(crystalW, crystalH).expandX().center().bottom();
    root.add(touchShield).size(touch).right().bottom();
  }

  private void buildDesktopBottom(Table root, float worldW, float worldH) {
    float crystalH = UiSpacing.pxToWorld(18f, worldH);
    float crystalW = Math.min(UiSpacing.pxToWorld(88f, worldH), worldW * 0.35f);
    crystalBar.layoutSize(crystalW, crystalH);
    root.add(crystalBar).colspan(3).size(crystalW, crystalH).expandX().center().bottom();
  }

  public void syncGameplayMetrics(float gameWorldW, float gameWorldH) {
    if (!profile.touchUiEnabled()) {
      gameplayMetrics.leftMove.set(0, 0, 0, 0);
      gameplayMetrics.rightMove.set(0, 0, 0, 0);
      gameplayMetrics.shieldTouch.set(0, 0, 0, 0);
      gameplayMetrics.pause.set(0, 0, 0, 0);
      gameplayMetrics.teleportMinWorldY = gameWorldH;
      return;
    }

    if (profile.isMobile && touchLeft != null) {
      copyActorBounds(touchLeft, gameplayMetrics.leftMove);
      copyActorBounds(touchRight, gameplayMetrics.rightMove);
      copyActorBounds(touchShield, gameplayMetrics.shieldTouch);
      if (touchPause != null) {
        copyActorBounds(touchPause, gameplayMetrics.pause);
      }
      clampTouchRectsToGameplay(gameWorldW, gameWorldH);
    } else {
      float touch = profile.gameplayTouchSize(gameWorldH);
      layoutGameplayTouchFallback(gameWorldW, gameWorldH, touch);
    }

    float bottomBand = UiBounds.safeBottom(gameWorldH) + profile.gameplayTouchSize(gameWorldH)
      + UiSpacing.small(gameWorldH);
    gameplayMetrics.teleportMinWorldY = bottomBand;
  }

  private static void copyActorBounds(Actor actor, Rectangle out) {
    out.set(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());
  }

  private void clampTouchRectsToGameplay(float gameWorldW, float gameWorldH) {
    clampRect(gameplayMetrics.leftMove, gameWorldW, gameWorldH);
    clampRect(gameplayMetrics.rightMove, gameWorldW, gameWorldH);
    clampRect(gameplayMetrics.shieldTouch, gameWorldW, gameWorldH);
    clampRect(gameplayMetrics.pause, gameWorldW, gameWorldH);
  }

  private static void clampRect(Rectangle rect, float gameWorldW, float gameWorldH) {
    if (rect.width <= 0f) {
      return;
    }
    UiBounds.clampRect(rect, gameWorldW, gameWorldH);
  }

  private void layoutGameplayTouchFallback(float w, float h, float touch) {
    float left = UiBounds.safeLeft(h);
    float bottom = UiBounds.safeBottom(h);
    gameplayMetrics.leftMove.set(left, bottom, touch, touch);
    gameplayMetrics.rightMove.set(left + touch + UiSpacing.small(h), bottom, touch, touch);
    gameplayMetrics.shieldTouch.set(UiBounds.safeRight(w, h) - touch, bottom, touch, touch);

    float topEdge = UiBounds.safeTop(h);
    float barH = healthBar.getHeight();
    float pauseH = touch * 0.85f;
    float pauseY = topEdge - barH - UiSpacing.small(h) - pauseH;
    gameplayMetrics.pause.set(left, Math.max(UiBounds.safeBottom(h), pauseY), touch, pauseH);
    clampTouchRectsToGameplay(w, h);
  }
}
