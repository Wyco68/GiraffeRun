package isne12.gp9.runner;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Scene2D stage with its own {@link FitViewport} — never share the game world viewport
 * (sharing breaks the camera, layout, and batch projection).
 */
public class ViewportStage extends Stage {
    private static final float WORLD_WIDTH = 8f;
    private static final float WORLD_HEIGHT = 5f;

    private final FitViewport fitViewport;

    public ViewportStage() {
        super(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        this.fitViewport = (FitViewport) getViewport();
    }

    public void resize(int screenWidth, int screenHeight) {
        fitViewport.update(screenWidth, screenHeight, true);
        getRoot().setBounds(0f, 0f, fitViewport.getWorldWidth(), fitViewport.getWorldHeight());
    }
}
