package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    protected final Main game;
    protected final Sprite sprite;
    protected final Rectangle rectangle;
    protected float speed;

    public GameObject(Texture texture, Main game) {
        this.game = game;
        this.sprite = new Sprite(texture);
        this.rectangle = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.speed = game.globalSpeed + game.level;
    }

    public abstract void update(float delta);

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        rectangle.setPosition(x, y);
    }

    public void dispose() {

    }
}
