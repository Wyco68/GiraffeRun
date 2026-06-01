package isne12.gp9.runner;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player extends GameObject {

    public static final int MAX_HEALTH = 5;
    public static final int CRYSTALS_TO_WIN = 5;
    public static final float SHIELD_COOLDOWN_MAX = 8f;
    public static final float TELEPORT_COOLDOWN_MAX = 4f;

    private int health;
    private int crystalCollected;

    private float shieldTimer;
    private float shieldCooldown;
    private boolean shieldActive;
    private float teleportCooldown;

    private final Texture runRight;
    private final Texture runLeft;
    private final Texture moveRight;
    private final Texture moveLeft;
    private boolean running = true;
    private float animationTimer = 0f;

    private final Sound hitSound;
    private final Sound shieldHitSound;
    private final Sound healSound;
    private final Sound collectSound;
    private final Sound teleportSound;

    public Player(Texture texture, Main game, Assets assets) {
        super(texture, game);
        setDisplaySize(1f, 1f);
        sprite.setX(game.viewport.getWorldWidth() / 2f - displayWidth / 2f);
        sprite.setY(0f);
        syncRectangle();

        health = MAX_HEALTH;

        runRight = assets.getTexture(Assets.RUN_RIGHT);
        runLeft = assets.getTexture(Assets.RUN_LEFT);
        moveRight = assets.getTexture(Assets.SHIELD_RIGHT);
        moveLeft = assets.getTexture(Assets.SHIELD_LEFT);

        hitSound = assets.getSound(Assets.HIT_SOUND);
        shieldHitSound = assets.getSound(Assets.SHIELD_HIT_SOUND);
        healSound = assets.getSound(Assets.HEAL_SOUND);
        collectSound = assets.getSound(Assets.COLLECT_SOUND);
        teleportSound = assets.getSound(Assets.TELEPORT_SOUND);
    }

    public void moveLeft(float delta) {
        sprite.translateX(-speed * delta);
    }

    public void moveRight(float delta) {
        sprite.translateX(speed * delta);
    }

    public boolean tryTeleport(Vector2 touchPos) {
        if (teleportCooldown > 0) {
            return false;
        }
        game.playSound(teleportSound);
        sprite.setX(MathUtils.clamp(touchPos.x - displayWidth / 2f, 0,
            game.viewport.getWorldWidth() - displayWidth));
        syncRectangle();
        teleportCooldown = TELEPORT_COOLDOWN_MAX;
        return true;
    }

    public void activateShield() {
        if (shieldCooldown <= 0 && !shieldActive) {
            shieldActive = true;
            shieldTimer = 3f;
            shieldCooldown = SHIELD_COOLDOWN_MAX;
            speed /= 2f;
        }
    }

    public void update(float delta) {
        animationTimer += delta;
        float frameDuration = 0.15f;
        if (animationTimer >= frameDuration) {
            animationTimer = 0;
            running = !running;
            if (shieldActive) {
                setFrame(running ? moveLeft : moveRight);
            } else {
                setFrame(running ? runLeft : runRight);
            }
        }
        if (shieldActive) {
            shieldTimer -= delta;
            if (shieldTimer <= 0) {
                shieldActive = false;
                speed = game.getMoveSpeed();
            }
        }
        if (shieldCooldown > 0) shieldCooldown -= delta;

        if (teleportCooldown > 0) teleportCooldown -= delta;
        float worldWidth = game.viewport.getWorldWidth();
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth - displayWidth));
        syncRectangle();
    }

    public int getHealth() {
        return health;
    }

    public int getCrystalCollected() {
        return crystalCollected;
    }

    public float getShieldCooldown() {
        return shieldCooldown;
    }

    public float getTeleportCooldown() {
        return teleportCooldown;
    }

    public boolean canActivateShield() {
        return shieldCooldown <= 0f && !shieldActive;
    }

    /** Remaining cooldown fraction in [0, 1]; 0 when ready. */
    public float getShieldCooldownRatio() {
        if (shieldCooldown <= 0f) {
            return 0f;
        }
        return MathUtils.clamp(shieldCooldown / SHIELD_COOLDOWN_MAX, 0f, 1f);
    }

    public void getHit() {
        if (shieldActive) {
            game.playSound(shieldHitSound);
            return;
        }
        game.playSound(hitSound);
        health--;
    }

    public void heal() {
        if (health < MAX_HEALTH) {
            game.playSound(healSound);
            health++;
        }
    }

    public void collectCrystal() {
        game.playSound(collectSound);
        crystalCollected++;
    }
}
