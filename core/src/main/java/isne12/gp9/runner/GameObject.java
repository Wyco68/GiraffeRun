package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    protected final Main game;
    protected final Sprite sprite;
    protected final Rectangle rectangle;
    protected TextureRegion region;
    protected float speed;
    protected float displayWidth = 1f;
    protected float displayHeight = 1f;

    public GameObject(Texture texture, Main game) {
        this.game = game;
        region = new TextureRegion(texture);
        sprite = new Sprite(region);
        rectangle = new Rectangle();
        speed = game.getMoveSpeed();
        syncRectangle();
    }

    protected GameObject(Main game) {
        this.game = game;
        sprite = new Sprite();
        rectangle = new Rectangle();
        speed = game.getMoveSpeed();
    }

    protected void setFrame(Texture texture) {
        region = new TextureRegion(texture);
        sprite.setRegion(region);
    }

    protected void setDisplaySize(float width, float height) {
        displayWidth = width;
        displayHeight = height;
    }

    protected void syncRectangle() {
        rectangle.set(sprite.getX(), sprite.getY(), displayWidth, displayHeight);
    }

    public abstract void update(float delta);

    public void draw(SpriteBatch batch) {
        if (region == null) {
            return;
        }
        batch.draw(region, sprite.getX(), sprite.getY(), displayWidth, displayHeight);
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

    public float getCenterX() {
        return sprite.getX() + displayWidth / 2f;
    }

    public float getCenterY() {
        return sprite.getY() + displayHeight / 2f;
    }

    public float getDisplayWidth() {
        return displayWidth;
    }

    public float getDisplayHeight() {
        return displayHeight;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        rectangle.setPosition(x, y);
    }
}
