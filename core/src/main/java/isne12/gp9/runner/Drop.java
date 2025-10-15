package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Drop extends GameObject {

    public Drop(Texture texture, Main game) {
        super(texture, game);
        sprite.setSize(0.8f, 0.8f);
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        sprite.setPosition(MathUtils.random(0, worldWidth - sprite.getWidth()), worldHeight);
    }

    //Methods used in GameScreen
    public void update(float delta) {
        sprite.translateY(-speed * delta);
        rectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public boolean overlaps(Rectangle r) {
        return rectangle.overlaps(r);
    }

    public boolean isOffScreen() {
        return sprite.getY() < -sprite.getHeight();
    }

    public abstract void onCatch(Player player);
}
