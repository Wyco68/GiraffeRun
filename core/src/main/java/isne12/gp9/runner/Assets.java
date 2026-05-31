package isne12.gp9.runner;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable {
    public static final String BG1 = "BG1.png";
    public static final String BG2 = "BG2.png";
    public static final String BG3 = "BG3.png";
    public static final String MENU = "MenuScreen.png";
    public static final String LOAD = "LoadScreen.png";
    public static final String GAME_OVER = "GameOverScreen.png";
    public static final String GAME_WIN = "GameWinScreen.png";
    public static final String BACK_VIEW = "backView.png";
    public static final String RUN_RIGHT = "rightMove.png";
    public static final String RUN_LEFT = "leftMove.png";
    public static final String SHIELD_RIGHT = "rightMoveShield.png";
    public static final String SHIELD_LEFT = "leftMoveShield.png";
    public static final String ROCKET = "rocket.png";
    public static final String HEART = "heart.png";
    public static final String CRYSTAL = "crystal.png";
    public static final String SHIELD_ICON = "shieldIcon.png";
    public static final String TELEPORT_ICON = "teleportIcon.png";
    public static final String HIT_SOUND = "hitSound.mp3";
    public static final String SHIELD_HIT_SOUND = "shieldHit.mp3";
    public static final String HEAL_SOUND = "healSound.mp3";
    public static final String COLLECT_SOUND = "collectSound.mp3";
    public static final String TELEPORT_SOUND = "teleportSound.mp3";
    public static final String GAME_WIN_SOUND = "gameWinSound.mp3";
    public static final String GAME_OVER_SOUND = "gameOverSound.mp3";
    public static final String THEME_AUDIO = "themeAudio.mp3";

    private static final String[] TEXTURES = {
        BG1, BG2, BG3, MENU, LOAD, GAME_OVER, GAME_WIN,
        BACK_VIEW, RUN_RIGHT, RUN_LEFT, SHIELD_RIGHT, SHIELD_LEFT,
        ROCKET, HEART, CRYSTAL, SHIELD_ICON, TELEPORT_ICON
    };

    private static final String[] SOUNDS = {
        HIT_SOUND, SHIELD_HIT_SOUND, HEAL_SOUND, COLLECT_SOUND, TELEPORT_SOUND,
        GAME_WIN_SOUND, GAME_OVER_SOUND
    };

    private final AssetManager assetManager = new AssetManager();
    private boolean loadingQueued;

    public void loadAll() {
        if (loadingQueued) {
            return;
        }
        loadingQueued = true;
        TextureLoader.TextureParameter textureParams = new TextureLoader.TextureParameter();
        textureParams.minFilter = TextureFilter.Nearest;
        textureParams.magFilter = TextureFilter.Nearest;
        for (String path : TEXTURES) {
            assetManager.load(path, Texture.class, textureParams);
        }
        for (String path : SOUNDS) {
            assetManager.load(path, Sound.class, new SoundLoader.SoundParameter());
        }
        assetManager.load(THEME_AUDIO, Music.class, new MusicLoader.MusicParameter());
    }

    public boolean update() {
        return assetManager.update();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public boolean isLoaded() {
        return assetManager.getProgress() >= 1f;
    }

    public Texture getTexture(String path) {
        return assetManager.get(path, Texture.class);
    }

    public Sound getSound(String path) {
        return assetManager.get(path, Sound.class);
    }

    public Music getMusic(String path) {
        return assetManager.get(path, Music.class);
    }

    public String getLevelBackgroundPath(int level) {
        switch (level) {
            case 2:
                return BG2;
            case 3:
                return BG3;
            default:
                return BG1;
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
