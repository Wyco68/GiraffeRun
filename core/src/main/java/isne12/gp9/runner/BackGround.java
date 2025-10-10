package isne12.gp9.runner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackGround {
    private final Texture texture;
    private float scrollY;
    private static final float SPEED_WORLD = 2f; // match drop speed (world units/sec)

    public BackGround(String filePath) {
        texture = new Texture(Gdx.files.internal(filePath));
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        scrollY = 0;
    }

    public void update(float delta, float worldHeight) {
        float pixelToWorldY = worldHeight / (float) Gdx.graphics.getHeight();
        float texHWorld = texture.getHeight() * pixelToWorldY;

        scrollY -= SPEED_WORLD * delta;

        if (scrollY <= -texHWorld) {
            scrollY += texHWorld;
        }
    }

    public void render(SpriteBatch batch, float worldWidth, float worldHeight) {
        float pixelToWorldY = worldHeight / (float) Gdx.graphics.getHeight();

        float texHWorld = texture.getHeight() * pixelToWorldY;

        // vertical tiling only
        float startY = scrollY;
        while (startY > 0) startY -= texHWorld;
        while (startY <= -texHWorld) startY += texHWorld;

        float y = startY;
        while (y < worldHeight) {
            batch.draw(texture, 0, y, worldWidth, texHWorld);
            y += texHWorld;
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
