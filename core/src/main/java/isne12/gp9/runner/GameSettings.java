package isne12.gp9.runner;

/** Runtime UI preferences (TeaVM-safe local state). */
public class GameSettings {
    private boolean musicEnabled = true;

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean musicEnabled) {
        this.musicEnabled = musicEnabled;
    }

    public void toggleMusic() {
        musicEnabled = !musicEnabled;
    }
}
