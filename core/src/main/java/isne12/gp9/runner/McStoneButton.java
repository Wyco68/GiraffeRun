package isne12.gp9.runner;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** Minecraft-style stone button for touch overlays (Scene2D). */
public class McStoneButton extends Actor {
    private final Main game;
    private final Texture whitePixel;
    private final Rectangle bounds = new Rectangle();
    private final Color faceUp;
    private final Color facePressed;

    private String label = "";
    private float labelScaleMult = McUi.BODY_MULT;
    private boolean pressed;

    public McStoneButton(Main game, Texture whitePixel, String label) {
        this(game, whitePixel, label, McStyle.BUTTON_FACE, McStyle.BUTTON_FACE_PRESSED);
    }

    public McStoneButton(Main game, Texture whitePixel, String label, Color faceUp, Color facePressed) {
        this.game = game;
        this.whitePixel = whitePixel;
        this.label = label != null ? label : "";
        this.faceUp = faceUp;
        this.facePressed = facePressed;
        setTouchable(Touchable.enabled);
    }

    public void setLabelScaleMult(float mult) {
        labelScaleMult = mult;
    }

    public void setHoldCallbacks(Runnable onDown, Runnable onUp) {
        addListener(new com.badlogic.gdx.scenes.scene2d.InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressed = true;
                onDown.run();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressed = false;
                onUp.run();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pressed) {
                    pressed = false;
                    onUp.run();
                }
            }
        });
    }

    public void setClickCallback(Runnable onClick) {
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                onClick.run();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pressed = false;
            }
        });
    }

    public boolean isShieldButton() {
        return faceUp.equals(McStyle.SHIELD_FACE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color old = batch.getColor().cpy();
        float alpha = old.a * parentAlpha;
        batch.setColor(old.r, old.g, old.b, alpha);

        bounds.set(0f, 0f, getWidth(), getHeight());
        McUi.drawStoneButton(batch, whitePixel, bounds, pressed, faceUp, facePressed);

        if (!label.isEmpty()) {
            float baseline = getHeight() * 0.38f;
            MenuText.drawCentered(game, batch, label, getWidth() / 2f, baseline,
                labelScaleMult, McStyle.TEXT);
        } else if (isShieldButton()) {
            drawShieldGlyph(batch);
        }
        batch.setColor(old);
    }

    private void drawShieldGlyph(Batch batch) {
        float w = getWidth();
        float h = getHeight();
        float cx = w * 0.5f;
        float cy = h * 0.48f;
        float sw = w * 0.36f;
        float sh = h * 0.42f;
        Color prev = batch.getColor().cpy();
        batch.setColor(0.78f, 0.78f, 0.82f, prev.a);
        batch.draw(whitePixel, cx - sw / 2f, cy - sh * 0.35f, sw, sh * 0.7f);
        batch.draw(whitePixel, cx - sw * 0.35f, cy - sh * 0.55f, sw * 0.7f, sh * 0.35f);
        batch.setColor(prev);
    }
}
