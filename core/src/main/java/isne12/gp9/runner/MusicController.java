package isne12.gp9.runner;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

public class MusicController {
    private Music current;
    private float volume;
    private float targetVolume;
    private float fadeSpeed;

    public void play(Music music, float targetVol, boolean loop) {
        if (music == null) {
            return;
        }
        if (current != null && current != music) {
            current.stop();
        }
        current = music;
        current.setLooping(loop);
        targetVolume = targetVol;
        volume = targetVol;
        current.setVolume(volume);
        if (!current.isPlaying()) {
            current.play();
        }
    }

    public void fadeTo(float targetVol, float durationSeconds) {
        targetVolume = MathUtils.clamp(targetVol, 0f, 1f);
        fadeSpeed = durationSeconds <= 0f ? 1f : Math.abs(targetVolume - volume) / durationSeconds;
    }

    public void stop() {
        if (current != null) {
            current.stop();
        }
    }

    public void pause() {
        if (current != null) {
            current.pause();
        }
    }

    public void resume() {
        if (current != null) {
            current.play();
        }
    }

    public void update(float delta) {
        if (current == null || Math.abs(volume - targetVolume) <= 0.01f) {
            return;
        }
        if (volume < targetVolume) {
            volume = Math.min(volume + fadeSpeed * delta, targetVolume);
        } else {
            volume = Math.max(volume - fadeSpeed * delta, targetVolume);
        }
        current.setVolume(volume);
    }
}
