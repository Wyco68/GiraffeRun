package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;

public class DropPools {
    private final Main game;
    private final Texture bulletTexture;
    private final Texture healthTexture;
    private final Texture crystalTexture;

    private final Pool<BulletDrop> bulletPool = new Pool<BulletDrop>(8, 64) {
        @Override
        protected BulletDrop newObject() {
            return new BulletDrop(game);
        }
    };

    private final Pool<HealthDrop> healthPool = new Pool<HealthDrop>(4, 32) {
        @Override
        protected HealthDrop newObject() {
            return new HealthDrop(game);
        }
    };

    private final Pool<CrystalDrop> crystalPool = new Pool<CrystalDrop>(4, 32) {
        @Override
        protected CrystalDrop newObject() {
            return new CrystalDrop(game);
        }
    };

    public DropPools(Main game, Texture bulletTexture, Texture healthTexture, Texture crystalTexture) {
        this.game = game;
        this.bulletTexture = bulletTexture;
        this.healthTexture = healthTexture;
        this.crystalTexture = crystalTexture;
    }

    public Drop obtainBullet() {
        BulletDrop drop = bulletPool.obtain();
        drop.init(bulletTexture, game);
        return drop;
    }

    public Drop obtainHealth() {
        HealthDrop drop = healthPool.obtain();
        drop.init(healthTexture, game);
        return drop;
    }

    public Drop obtainCrystal() {
        CrystalDrop drop = crystalPool.obtain();
        drop.init(crystalTexture, game);
        return drop;
    }

    public void free(Drop drop) {
        if (drop instanceof BulletDrop) {
            bulletPool.free((BulletDrop) drop);
        } else if (drop instanceof HealthDrop) {
            healthPool.free((HealthDrop) drop);
        } else if (drop instanceof CrystalDrop) {
            crystalPool.free((CrystalDrop) drop);
        }
    }
}
