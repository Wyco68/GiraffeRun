package isne12.gp9.runner;

import com.badlogic.gdx.Preferences;

public class GameData {
    private static final String PREFS_NAME = "girafferun";
    private static final String KEY_HIGH_SCORE = "highScore";
    private static final String KEY_BEST_LEVEL = "bestLevel";

    private final Preferences prefs;
    private int highScore;
    private int bestLevel;

    public GameData() {
        prefs = com.badlogic.gdx.Gdx.app.getPreferences(PREFS_NAME);
        highScore = prefs.getInteger(KEY_HIGH_SCORE, 0);
        bestLevel = prefs.getInteger(KEY_BEST_LEVEL, 1);
    }

    public int getHighScore() {
        return highScore;
    }

    public int getBestLevel() {
        return bestLevel;
    }

    public int computeScore(int level, int crystalsCollected) {
        return (level - 1) * 500 + crystalsCollected * 100;
    }

    public void recordRun(int levelReached, int crystalsCollected) {
        int score = computeScore(levelReached, crystalsCollected);
        if (score > highScore) {
            highScore = score;
            prefs.putInteger(KEY_HIGH_SCORE, highScore);
        }
        if (levelReached > bestLevel) {
            bestLevel = levelReached;
            prefs.putInteger(KEY_BEST_LEVEL, bestLevel);
        }
        prefs.flush();
    }
}
