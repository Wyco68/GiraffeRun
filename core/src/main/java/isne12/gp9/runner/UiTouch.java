package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/** Screen-to-world touch helpers for batch-drawn buttons. */
public final class UiTouch {
    private static final Vector2 tmp = new Vector2();

    private UiTouch() {
    }

    public static boolean justTouchedIn(Viewport viewport, Rectangle worldBounds) {
        if (!Gdx.input.justTouched() && !Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            return false;
        }
        tmp.set(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(tmp);
        return worldBounds.contains(tmp.x, tmp.y);
    }

    public static Rectangle rect(float x, float y, float width, float height) {
        return new Rectangle(x, y, width, height);
    }
}
