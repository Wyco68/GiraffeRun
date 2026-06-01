package isne12.gp9.runner.hud;

import com.badlogic.gdx.scenes.scene2d.Actor;

/** Invisible layout cell marking a touch target region (size only). */
public final class TouchSlotActor extends Actor {
  public TouchSlotActor(float size) {
    setSize(size, size);
  }

}
