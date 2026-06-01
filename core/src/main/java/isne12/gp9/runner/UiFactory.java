package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/** Builds Scene2D widgets with spacing and touch targets. */
public final class UiFactory {
    private UiFactory() {
    }

    public static Label label(String text, Fonts fonts, Fonts.Tier tier, Color color) {
        Label.LabelStyle style = new Label.LabelStyle(fonts.getFont(), color);
        Label label = new Label(text, style);
        label.setFontScale(tier.scale);
        label.setWrap(true);
        return label;
    }

    public static ImageButton iconButton(UiIcons icons, UiIcons.Kind kind, Runnable onClick) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = icons.normal(kind);
        style.imageDown = icons.pressed(kind);
        style.imageOver = icons.normal(kind);
        ImageButton button = new ImageButton(style);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }
        });
        return button;
    }

    public static void sizeTouchIcon(ImageButton button, float worldHeight) {
        float size = Math.max(UiSpacing.iconSize(worldHeight), UiSpacing.touchTarget(worldHeight));
        button.setSize(size, size);
    }

    public static float touchButtonSize(float worldHeight) {
        UiScreenProfile profile = UiScreenProfile.fromDisplay();
        return profile.pxToGameplayWorld(profile.touchButtonPx, worldHeight);
    }

    public static ImageButton.ImageButtonStyle textureButtonStyle(Texture up, Texture down) {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(new TextureRegion(up));
        style.imageOver = style.imageUp;
        Texture pressed = down != null ? down : up;
        style.imageDown = new TextureRegionDrawable(new TextureRegion(pressed));
        return style;
    }

    public static ImageButton textureImageButton(Texture up, Texture down) {
        return new ImageButton(textureButtonStyle(up, down));
    }

    public static InputListener holdListener(Runnable onDown, Runnable onUp) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                onDown.run();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                onUp.run();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                onUp.run();
            }
        };
    }

    public static ImageButton holdButton(Texture up, Texture down, Runnable onDown, Runnable onUp) {
        ImageButton button = textureImageButton(up, down);
        button.addListener(holdListener(onDown, onUp));
        return button;
    }

    public static ImageButton clickButton(Texture up, Texture down, Runnable onClick) {
        ImageButton button = textureImageButton(up, down);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }
        });
        return button;
    }

    /** Button with a cooldown fill overlay child (ImageButton or {@link McStoneButton}). */
    public static Stack cooldownButtonStack(com.badlogic.gdx.scenes.scene2d.Actor button, Texture whitePixel) {
        Stack stack = new Stack();
        stack.add(button);
        Image overlay = new Image(new TextureRegionDrawable(new TextureRegion(whitePixel)));
        overlay.setVisible(false);
        overlay.setColor(0f, 0f, 0f, 0.45f);
        stack.add(overlay);
        stack.setUserObject(overlay);
        return stack;
    }

    public static void updateCooldownOverlay(Stack stack, float cooldownRatio) {
        Image overlay = (Image) stack.getUserObject();
        com.badlogic.gdx.scenes.scene2d.Actor button = stack.getChild(0);
        boolean onCooldown = cooldownRatio > 0.001f;
        button.setColor(1f, 1f, 1f, onCooldown ? 0.45f : 1f);
        overlay.setVisible(onCooldown);
        if (onCooldown) {
            float h = button.getHeight();
            overlay.setHeight(h * cooldownRatio);
            overlay.setY(0f);
        }
    }

    /**
     * Semi-transparent backdrop that does not tint child actors (avoids invisible labels).
     */
    public static Table dimPanel(Table content, Texture whitePixel, float worldHeight) {
        float pad = UiSpacing.medium(worldHeight);
        final TextureRegionDrawable tile = new TextureRegionDrawable(new TextureRegion(whitePixel));
        Drawable dimBg = new Drawable() {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                Color prev = batch.getColor().cpy();
                batch.setColor(0f, 0f, 0f, 0.55f);
                tile.draw(batch, x, y, width, height);
                batch.setColor(prev);
            }

            @Override public float getLeftWidth() { return 0; }
            @Override public void setLeftWidth(float leftWidth) { }
            @Override public float getRightWidth() { return 0; }
            @Override public void setRightWidth(float rightWidth) { }
            @Override public float getTopHeight() { return 0; }
            @Override public void setTopHeight(float topHeight) { }
            @Override public float getBottomHeight() { return 0; }
            @Override public void setBottomHeight(float bottomHeight) { }
            @Override public float getMinWidth() { return 0; }
            @Override public void setMinWidth(float minWidth) { }
            @Override public float getMinHeight() { return 0; }
            @Override public void setMinHeight(float minHeight) { }
        };
        Table panel = new Table();
        panel.setBackground(dimBg);
        panel.pad(pad);
        panel.add(content);
        return panel;
    }

    public static Table screenRoot() {
        Table root = new Table();
        root.setFillParent(true);
        return root;
    }
}
