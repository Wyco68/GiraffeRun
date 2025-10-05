package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class CrystalDrop extends Drop {
    public CrystalDrop(Texture texture, Main game) {
        super(texture, game);
        this.speed = 3f;
    }

    @Override
    public void onCatch(Player player) {
        player.collectCrystal();
    }
}
