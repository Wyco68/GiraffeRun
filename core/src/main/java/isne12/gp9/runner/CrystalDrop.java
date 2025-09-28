package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class CrystalDrop implements Drop {
    private final Sprite sprite;
    private final Rectangle rectangle;

    public CrystalDrop(Texture texture, float startX, float startY) {
        sprite = new Sprite(texture);
        sprite.setSize(1f, 1f);
        sprite.setPosition(startX, startY);

        rectangle = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }


    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void update(float delta) {
        sprite.translateY(-3f * delta);
        rectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    @Override
    public void onCatch(GameScreen screen) {
        screen.crystalCollected++;
    }
}
