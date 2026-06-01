package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
