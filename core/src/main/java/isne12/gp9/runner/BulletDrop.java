package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;

public class BulletDrop extends Drop {

    public BulletDrop(Texture texture, Main game) {
        super(texture, game);
        this.speed = 4f + (game.level*1.5f);
    }

    @Override
    public void onCatch(Player player) {
        player.getHit();
    }
}
