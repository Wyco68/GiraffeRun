package isne12.gp9.runner;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Scene2D HUD stage — same {@link FitViewport} world (8×5) as gameplay so HUD stays in the play area.
 */
public class ViewportStage extends Stage {
  public static final float WORLD_WIDTH = 8f;
  public static final float WORLD_HEIGHT = 5f;

  private final FitViewport fitViewport;
  private UiScreenProfile profile = UiScreenProfile.fromSize(1280, 720);

  public ViewportStage() {
    super(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
    this.fitViewport = (FitViewport) getViewport();
  }

  public FitViewport getFitViewport() {
    return fitViewport;
  }

  public UiScreenProfile getProfile() {
    return profile;
  }

  public void resize(int screenWidth, int screenHeight) {
    profile = UiScreenProfile.fromSize(screenWidth, screenHeight);
    fitViewport.update(screenWidth, screenHeight, true);
    getRoot().setBounds(0f, 0f, fitViewport.getWorldWidth(), fitViewport.getWorldHeight());
  }
}
