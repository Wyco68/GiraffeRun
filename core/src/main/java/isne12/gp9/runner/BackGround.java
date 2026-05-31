package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackGround {
    private final Texture texture;
    private float scrollY;
    private float texHWorld;

    public BackGround(Texture texture) {
        this.texture = texture;
        scrollY = 0;
        texHWorld = 0;
    }

    public void update(float delta, float worldHeight, Main game) {
        texHWorld = worldHeight;
        scrollY -= (game.getGlobalSpeed() + game.getLevel()) * delta;

        if (scrollY <= -texHWorld) {
            scrollY += texHWorld;
        }
    }

    public void render(SpriteBatch batch, float worldWidth, float worldHeight) {
        texHWorld = worldHeight;

        float startY = scrollY;
        while (startY > 0) startY -= texHWorld;
        while (startY <= -texHWorld) startY += texHWorld;

        float y = startY;
        while (y < worldHeight) {
            batch.draw(texture, 0, y, worldWidth, texHWorld);
            y += texHWorld;
        }
    }

    public void onResize(float worldHeight) {
        if (texHWorld > 0 && worldHeight > 0 && texHWorld != worldHeight) {
            scrollY *= worldHeight / texHWorld;
        }
        texHWorld = worldHeight;
    }
}
