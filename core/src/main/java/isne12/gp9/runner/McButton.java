package isne12.gp9.runner;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Text-only menu control (no stone bar background). */
public class McButton {
    private final Rectangle bounds = new Rectangle();
    private String label = "";
    private boolean pressed;

    public void set(float x, float y, float width, float height, String label) {
        bounds.set(x, y, width, height);
        this.label = label;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(Main game, SpriteBatch batch) {
        float baseline = bounds.y + bounds.height * 0.38f;
        MenuText.drawCentered(game, batch, label, bounds.x + bounds.width / 2f, baseline,
            McUi.BODY_MULT, pressed ? McStyle.CLICKABLE_PRESSED : McStyle.CLICKABLE);
    }

    public boolean handleClick(Viewport viewport) {
        pressed = false;
        if (McUi.isClicked(viewport, bounds)) {
            pressed = true;
            return true;
        }
        return false;
    }

    public boolean containsWorld(float worldX, float worldY, float worldHeight) {
        float pad = UiSpacing.small(worldHeight) * 0.35f;
        return bounds.x - pad <= worldX && worldX <= bounds.x + bounds.width + pad
            && bounds.y - pad <= worldY && worldY <= bounds.y + bounds.height + pad;
    }

    public void clearPressed() {
        pressed = false;
    }
}
