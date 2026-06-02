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
    private static final String TAG = "Assets";
    public static final String BG1 = "BG1.png";
    public static final String BG2 = "BG2.png";
    public static final String BG3 = "BG3.png";
    public static final String BG4 = "BG4.png";
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

    public static final String UI_ICON_SETTINGS = "ui/iconSettings.png";
    public static final String UI_ICON_SETTINGS_PRESSED = "ui/iconSettingsPressed.png";
    public static final String UI_ICON_SOUND = "ui/iconSound.png";
    public static final String UI_ICON_SOUND_PRESSED = "ui/iconSoundPressed.png";
    public static final String UI_ICON_PAUSE = "ui/iconPause.png";
    public static final String UI_ICON_PAUSE_PRESSED = "ui/iconPausePressed.png";
    public static final String UI_ICON_BACK = "ui/iconBack.png";
    public static final String UI_ICON_BACK_PRESSED = "ui/iconBackPressed.png";
    public static final String UI_ICON_RETRY = "ui/iconRetry.png";
    public static final String UI_ICON_RETRY_PRESSED = "ui/iconRetryPressed.png";
    public static final String HIT_SOUND = "hitSound.mp3";
    public static final String SHIELD_HIT_SOUND = "shieldHit.mp3";
    public static final String HEAL_SOUND = "healSound.mp3";
    public static final String COLLECT_SOUND = "collectSound.mp3";
    public static final String TELEPORT_SOUND = "teleportSound.mp3";
    public static final String GAME_WIN_SOUND = "gameWinSound.mp3";
    public static final String GAME_OVER_SOUND = "gameOverSound.mp3";
    public static final String THEME_AUDIO = "themeAudio.mp3";

    /** Loaded during initial splash (menu + gameplay essentials). */
    private static final String[] BOOT_TEXTURES = {
        MENU, LOAD, BG1, BG2, BG3, BG4,
        BACK_VIEW, RUN_RIGHT, RUN_LEFT, SHIELD_RIGHT, SHIELD_LEFT,
        ROCKET, HEART, CRYSTAL, SHIELD_ICON, TELEPORT_ICON,
        UI_ICON_SETTINGS, UI_ICON_SETTINGS_PRESSED,
        UI_ICON_SOUND, UI_ICON_SOUND_PRESSED,
        UI_ICON_PAUSE, UI_ICON_PAUSE_PRESSED,
        UI_ICON_BACK, UI_ICON_BACK_PRESSED,
        UI_ICON_RETRY, UI_ICON_RETRY_PRESSED
    };

    /** Large end-of-run screens; loaded after boot pack starts. */
    private static final String[] DEFERRED_TEXTURES = {
        GAME_OVER, GAME_WIN
    };

    private static final String[] SOUNDS = {
        HIT_SOUND, SHIELD_HIT_SOUND, HEAL_SOUND, COLLECT_SOUND, TELEPORT_SOUND,
        GAME_WIN_SOUND, GAME_OVER_SOUND
    };

    private final AssetManager assetManager = new AssetManager();
    private boolean menuSplashQueued;
    private boolean loadingQueued;
    private boolean deferredQueued;
    private int loadErrors;

    public Assets() {
        assetManager.setErrorListener((asset, throwable) -> {
            loadErrors++;
            WebLog.error(TAG, "Failed to load: " + asset, throwable);
        });
    }

    private TextureLoader.TextureParameter textureParams() {
        TextureLoader.TextureParameter textureParams = new TextureLoader.TextureParameter();
        textureParams.minFilter = TextureFilter.Nearest;
        textureParams.magFilter = TextureFilter.Nearest;
        return textureParams;
    }

    /** Loads {@link #MENU} first so the initial loading screen can show it. */
    public void loadMenuSplash() {
        if (menuSplashQueued) {
            return;
        }
        menuSplashQueued = true;
        assetManager.load(MENU, Texture.class, textureParams());
    }

    public boolean isMenuSplashLoaded() {
        return assetManager.isLoaded(MENU, Texture.class);
    }

    public void loadAll() {
        if (loadingQueued) {
            return;
        }
        loadingQueued = true;
        TextureLoader.TextureParameter textureParams = textureParams();
        queueTextures(BOOT_TEXTURES, textureParams);
        for (String path : SOUNDS) {
            if (!assetManager.isLoaded(path, Sound.class)) {
                assetManager.load(path, Sound.class, new SoundLoader.SoundParameter());
            }
        }
        if (!assetManager.isLoaded(THEME_AUDIO, Music.class)) {
            assetManager.load(THEME_AUDIO, Music.class, new MusicLoader.MusicParameter());
        }
    }

    private void queueTextures(String[] paths, TextureLoader.TextureParameter textureParams) {
        for (String path : paths) {
            if (!assetManager.isLoaded(path, Texture.class)) {
                assetManager.load(path, Texture.class, textureParams);
            }
        }
    }

    /** Queues large menu overlays (game over / win) after boot textures are underway. */
    public void loadDeferredTextures() {
        if (deferredQueued) {
            return;
        }
        deferredQueued = true;
        queueTextures(DEFERRED_TEXTURES, textureParams());
    }

    public boolean hasLoadErrors() {
        return loadErrors > 0;
    }

    public boolean update() {
        return assetManager.update();
    }

    public float getProgress() {
        if (!loadingQueued) {
            return 0f;
        }
        return assetManager.getProgress();
    }

    public boolean isLoaded() {
        if (!loadingQueued) {
            return false;
        }
        return assetManager.getProgress() >= 1f;
    }

    public Texture getTexture(String path) {
        if (!assetManager.isLoaded(path, Texture.class)) {
            WebLog.error(TAG, "Texture not loaded: " + path);
            return null;
        }
        return assetManager.get(path, Texture.class);
    }

    public Sound getSound(String path) {
        if (!assetManager.isLoaded(path, Sound.class)) {
            WebLog.error(TAG, "Sound not loaded: " + path);
            return null;
        }
        return assetManager.get(path, Sound.class);
    }

    public Music getMusic(String path) {
        if (!assetManager.isLoaded(path, Music.class)) {
            WebLog.error(TAG, "Music not loaded: " + path);
            return null;
        }
        return assetManager.get(path, Music.class);
    }

    /** Ensures a texture is queued and blocks until loaded (for lazy end screens). */
    public Texture getTextureOrLoad(String path) {
        if (!assetManager.isLoaded(path, Texture.class)) {
            assetManager.load(path, Texture.class, textureParams());
            assetManager.finishLoadingAsset(path);
        }
        return getTexture(path);
    }

    public String getLevelBackgroundPath(int level) {
        switch (level) {
            case 2:
                return BG2;
            case 3:
                return BG3;
            case 4:
                return BG4;
            default:
                return BG1;
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
