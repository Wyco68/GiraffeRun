package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Legacy batch HUD — replaced by {@link AdaptiveHudLayout} + Scene2D actors.
 * @deprecated use {@link AdaptiveGameUi}
 */
@Deprecated
public final class HUDManager {
    private static final int HEALTH_SEGMENTS = Player.MAX_HEALTH;
    private static final float CRYSTAL_LERP = 8f;

    private final Texture shieldIcon;
    private final Texture teleportIcon;
    private final Texture crystalIcon;
    private final Texture whitePixel;

    private float crystalDisplayProgress;

    public HUDManager(Texture shieldIcon, Texture teleportIcon, Texture crystalIcon, Texture whitePixel) {
        this.shieldIcon = shieldIcon;
        this.teleportIcon = teleportIcon;
        this.crystalIcon = crystalIcon;
        this.whitePixel = whitePixel;
    }

    public void reset() {
        crystalDisplayProgress = 0f;
    }

    public void update(float delta, Player player) {
        float target = player.getCrystalCollected() / (float) Player.CRYSTALS_TO_WIN;
        crystalDisplayProgress = MathUtils.lerp(crystalDisplayProgress, target,
            Math.min(1f, delta * CRYSTAL_LERP));
    }

    public void draw(SpriteBatch batch, Main game, Player player) {
        float w = game.viewport.getWorldWidth();
        float h = game.viewport.getWorldHeight();
        float pad = UiBounds.margin(h);

        drawHealthBar(batch, w, pad, h - pad, player.getHealth(), h);
        drawSkillIcons(batch, game.fonts, player, w, h, pad);
        drawCrystalBar(batch, w, h);
    }

    private void drawHealthBar(SpriteBatch batch, float worldWidth, float x, float top,
                               int health, float worldHeight) {
        float barH = UiSpacing.pxToWorld(12f, worldHeight);
        float y = top - barH;
        float gap = UiSpacing.pxToWorld(2f, worldHeight);
        float border = UiSpacing.pxToWorld(2f, worldHeight);
        float totalW = UiSpacing.pxToWorld(128f, worldHeight);
        float maxW = UiBounds.safeRight(worldWidth, worldHeight) - UiBounds.safeLeft(worldHeight);
        totalW = Math.min(totalW, maxW);
        float segW = (totalW - gap * (HEALTH_SEGMENTS - 1)) / HEALTH_SEGMENTS;

        x = UiBounds.clampX(x, totalW + border * 2f, worldWidth, worldHeight);
        y = UiBounds.clampY(y, barH + border * 2f, worldHeight);

        Color old = batch.getColor().cpy();

        batch.setColor(UiColors.BAR_BG);
        batch.draw(whitePixel, x - border, y - border, totalW + border * 2f, barH + border * 2f);

        for (int i = 0; i < HEALTH_SEGMENTS; i++) {
            float sx = x + i * (segW + gap);
            if (i < health) {
                batch.setColor(UiColors.HEALTH_LOW);
            } else {
                batch.setColor(UiColors.BAR_EMPTY);
            }
            batch.draw(whitePixel, sx, y, segW, barH);
        }
        batch.setColor(old);
    }

    private void drawSkillIcons(SpriteBatch batch, Fonts fonts, Player player, float w, float h, float pad) {
        float iconSize = skillIconSize(h);
        float gap = UiSpacing.small(h);
        float rowW = iconSize * 2f + gap;
        float right = UiBounds.safeRight(w, h);
        float rightX = right - rowW;
        float top = UiBounds.safeTop(h);
        float y = UiBounds.clampY(top - iconSize, iconSize, h);

        batch.draw(shieldIcon, rightX, y, iconSize, iconSize);
        drawCooldownText(batch, fonts, player.getShieldCooldown(), rightX, y, iconSize);

        float teleportX = rightX + iconSize + gap;
        batch.draw(teleportIcon, teleportX, y, iconSize, iconSize);
        drawCooldownText(batch, fonts, player.getTeleportCooldown(), teleportX, y, iconSize);
    }

    private static void drawCooldownText(SpriteBatch batch, Fonts fonts, float cooldown,
                                         float iconX, float iconY, float iconSize) {
        if (cooldown <= 0f) {
            return;
        }
        String text = formatCooldown(cooldown);
        float cx = iconX + iconSize / 2f;
        float baseline = iconY + iconSize * 0.58f;
        UiText.drawCentered(batch, fonts, Fonts.Tier.HUD, UiColors.HUD, text, cx, baseline);
    }

    private static String formatCooldown(float seconds) {
        return String.valueOf(Math.round(seconds * 10f) / 10f);
    }

    private void drawCrystalBar(SpriteBatch batch, float w, float h) {
        float barH = UiSpacing.pxToWorld(6f, h);
        float barW = UiSpacing.pxToWorld(64f, h);
        float icon = barH * 1.2f;
        float iconGap = UiSpacing.pxToWorld(2f, h);
        float groupW = barW + icon + iconGap;
        float x = (w - groupW) / 2f;
        float top = UiBounds.safeTop(h);
        float y = top - icon - UiSpacing.small(h);
        y = UiBounds.clampY(y, icon, h);

        Color old = batch.getColor().cpy();
        batch.setColor(UiColors.BAR_BG);
        batch.draw(whitePixel, x + icon + iconGap, y, barW, barH);

        float fillW = barW * MathUtils.clamp(crystalDisplayProgress, 0f, 1f);
        batch.setColor(UiColors.CRYSTAL_FILL);
        if (fillW > 0.001f) {
            batch.draw(whitePixel, x + icon + iconGap, y, fillW, barH);
        }
        batch.setColor(old);

        batch.draw(crystalIcon, x, y - UiSpacing.pxToWorld(1f, h), icon, icon);
    }

    private static float skillIconSize(float worldHeight) {
        return UiSpacing.pxToWorld(44f, worldHeight);
    }
}
