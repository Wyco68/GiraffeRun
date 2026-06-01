package isne12.gp9.runner;

/** Runtime UI preferences (TeaVM-safe local state). */
public class GameSettings {
    /** When false, mutes theme music and all one-shot SFX. */
    private boolean audioEnabled = true;

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    public boolean isMusicEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean enabled) {
        this.audioEnabled = enabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.audioEnabled = enabled;
    }

    public void toggleAudio() {
        audioEnabled = !audioEnabled;
    }

    public void toggleMusic() {
        toggleAudio();
    }
}
