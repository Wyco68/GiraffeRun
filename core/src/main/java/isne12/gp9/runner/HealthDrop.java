package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;

public class HealthDrop extends Drop {

    public HealthDrop(Texture texture, Main game) {
        super(texture, game);
        this.speed=2f;
    }

    @Override
    public void onCatch(Player player) {
        player.heal();
    }
}
