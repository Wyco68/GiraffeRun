package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Drop extends GameObject {

    protected Drop(Main game) {
        super(game);
    }

    public void init(Texture texture, Main game) {
        setFrame(texture);
        speed = game.getMoveSpeed();
        setDisplaySize(0.8f, 0.8f);
        float worldWidth = game.viewport.getWorldWidth();
        float worldHeight = game.viewport.getWorldHeight();
        sprite.setPosition(MathUtils.random(0, worldWidth - displayWidth), worldHeight);
        syncRectangle();
        onInit();
    }

    protected void onInit() {
    }

    public void update(float delta) {
        sprite.translateY(-speed * delta);
        syncRectangle();
    }

    public boolean overlaps(Rectangle r) {
        return rectangle.overlaps(r);
    }

    public boolean isOffScreen() {
        return sprite.getY() < -displayHeight;
    }

    public abstract void onCatch(Player player);
}
