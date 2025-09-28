package isne12.gp9.runner;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public interface Drop {
    Sprite getSprite();

    Rectangle getRectangle();

    void update(float delta);

    void onCatch(GameScreen screen);
}
