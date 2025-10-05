package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Player {

    private final Main game;

    // Player' properties
    private final Sprite sprite;
    private final Rectangle rectangle;
    private final float speed = 4f;
    private int health;
    private int crystalCollected;

    // Player's skills
    private float shieldTimer;
    private float shieldCooldown;
    private boolean shieldActive;
    private float teleportCooldown;


    // Animations
    private final Texture runRight;
    private final Texture runLeft;
    private boolean running = true;
    private float animationTimer = 0f;
    private final float frameDuration = 0.15f;

    // Constructor
    public Player(Texture texture, Main game) {
        this.game = game;
        this.sprite = new Sprite(texture);
        sprite.setSize(1, 1);
        sprite.setX(game.viewport.getWorldWidth()/2);

        this.rectangle = new Rectangle(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        this.health = 5;

        runRight = new Texture("rightMove.png");
        runLeft = new Texture("leftMove.png");
    }

    // inputHandling from gameScreen
    public void moveLeft(float delta) {
        sprite.translateX(-speed * delta);
    }

    public void moveRight(float delta) {
        sprite.translateX(speed * delta);
    }

    //skills
    public void teleport(float pos) {
        if (teleportCooldown <= 0) {
            //use skill
            sprite.setCenterX(pos);
            //reset the cooldown
            teleportCooldown = 4f;
        }
    }

    public void activateShield() {
        if (shieldCooldown <= 0 && !shieldActive) {
            // activate it for a duration
            shieldActive = true;
            shieldTimer = 3f;
            //reset the cooldown
            shieldCooldown = 8f;
        }
    }


    //for animation and cooldown
    public void update(float delta) {
        animationTimer += delta;
        if (animationTimer >= frameDuration) {
            animationTimer = 0;
            running = !running;
            sprite.setTexture(running ? runLeft : runRight);
        }
        // shield
        if (shieldActive) {
            shieldTimer -= delta;
            if (shieldTimer <= 0) shieldActive = false;
            else if (shieldCooldown > 0) shieldCooldown -= delta;
        }

        // teleport
        if (teleportCooldown > 0) teleportCooldown -= delta;
        float worldWidth = game.viewport.getWorldWidth();
        float playerWidth = sprite.getWidth();
        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth - playerWidth));
        rectangle.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public int getHealth() {
        return this.health;
    }

    public int getCrystalCollected() {
        return this.crystalCollected;
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }

    // get hit or not , depend on the shield
    public void getHit() {
        if (shieldActive) return;
        health--;
    }

    // heal if capture the heart
    public void heal() {
        if (health < 5) health++;
    }

    // capture crystal for level up
    public void collectCrystal() {
        crystalCollected++;
    }


    //reset for next level
    public void reset() {
        health = 5;
        crystalCollected = 0;
        shieldActive = false;
        shieldTimer = 0;
        shieldCooldown = 0;
        teleportCooldown = 0;
    }

    public void dispose() {
        runRight.dispose();
        runLeft.dispose();
    }

}
