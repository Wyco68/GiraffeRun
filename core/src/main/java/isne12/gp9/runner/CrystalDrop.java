package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;

public class CrystalDrop extends Drop {
    public CrystalDrop(Texture texture, Main game) {
        super(texture, game);
    }

    @Override
    public void onCatch(Player player) {
        player.collectCrystal();
    }
}
