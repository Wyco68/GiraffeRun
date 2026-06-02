package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class EffectManager implements Disposable {
    private static class Particle {
        float x;
        float y;
        float vx;
        float vy;
        float life;
        float maxLife;
        float size;
        final Color color = new Color();
    }

    private final Texture pixel;
    private final Array<Particle> particles = new Array<>();
    private final Array<Particle> pool = new Array<>();

    public EffectManager(Texture pixel) {
        this.pixel = pixel;
    }

    private Particle obtainParticle() {
        return pool.size > 0 ? pool.pop() : new Particle();
    }

    private void freeParticle(Particle p) {
        pool.add(p);
    }

    public void spawnCrystalCollect(float x, float y) {
        for (int i = 0; i < 14; i++) {
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float speed = MathUtils.random(1.5f, 3.5f);
            spawn(x, y, MathUtils.cos(angle) * speed, MathUtils.sin(angle) * speed,
                MathUtils.random(0.06f, 0.14f), MathUtils.random(0.35f, 0.55f),
                0f, 0.85f, 0.95f, 1f);
        }
    }

    public void spawnTeleport(float fromX, float fromY, float toX, float toY) {
        for (int i = 0; i < 10; i++) {
            float t = MathUtils.random();
            float x = MathUtils.lerp(fromX, toX, t);
            float y = MathUtils.lerp(fromY, toY, t);
            spawn(x, y, MathUtils.random(-2f, 2f), MathUtils.random(-2f, 2f),
                MathUtils.random(0.08f, 0.16f), MathUtils.random(0.25f, 0.45f),
                0.7f, 0.5f, 1f, 1f);
        }
        for (int i = 0; i < 8; i++) {
            float angle = MathUtils.random(0f, MathUtils.PI2);
            float speed = MathUtils.random(2f, 4f);
            spawn(toX, toY, MathUtils.cos(angle) * speed, MathUtils.sin(angle) * speed,
                MathUtils.random(0.05f, 0.12f), MathUtils.random(0.3f, 0.5f),
                0.9f, 0.9f, 1f, 1f);
        }
    }

    private void spawn(float x, float y, float vx, float vy, float size, float life,
                       float r, float g, float b, float a) {
        Particle p = obtainParticle();
        p.x = x;
        p.y = y;
        p.vx = vx;
        p.vy = vy;
        p.size = size;
        p.life = life;
        p.maxLife = life;
        p.color.set(r, g, b, a);
        particles.add(p);
    }

    public void update(float delta) {
        for (int i = particles.size - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.life -= delta;
            if (p.life <= 0f) {
                particles.removeIndex(i);
                freeParticle(p);
                continue;
            }
            p.x += p.vx * delta;
            p.y += p.vy * delta;
            p.vy -= 2f * delta;
        }
    }

    public void draw(SpriteBatch batch) {
        for (Particle p : particles) {
            float alpha = p.color.a * (p.life / p.maxLife);
            batch.setColor(p.color.r, p.color.g, p.color.b, alpha);
            batch.draw(pixel, p.x - p.size / 2f, p.y - p.size / 2f, p.size, p.size);
        }
        batch.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        particles.clear();
        pool.clear();
    }
}
