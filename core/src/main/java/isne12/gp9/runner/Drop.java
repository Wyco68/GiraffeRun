package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Drop {
    protected Sprite sprite;
    protected Rectangle rectangle;
    protected float speed;

    public Drop(Texture texture, Main game) {
        sprite = new Sprite(texture);
        sprite.setSize(0.5f, 0.5f);
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        sprite.setPosition(MathUtils.random(0, worldWidth - sprite.getWidth()), worldHeight);
        rectangle = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
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

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public abstract void onCatch(Player player);
}
