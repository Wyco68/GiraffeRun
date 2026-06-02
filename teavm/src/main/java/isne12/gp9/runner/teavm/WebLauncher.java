package isne12.gp9.runner.teavm;

import com.github.xpenatan.gdx.teavm.backends.web.WebApplication;
import com.github.xpenatan.gdx.teavm.backends.web.WebApplicationConfiguration;
import isne12.gp9.runner.Main;

/**
 * Browser entry point — compiles to WebAssembly via TeaVM and runs the existing {@link Main} game.
 */
public class WebLauncher {

    /** Fraction of the browser viewport used for the game canvas (1 = full window). */
    public static final float VIEWPORT_SCALE = 1f;

    public static void main(String[] args) {
        System.setProperty("girafferun.web.viewportScale", Float.toString(VIEWPORT_SCALE));

        WebApplicationConfiguration config = new WebApplicationConfiguration("canvas");
        config.width = 0;
        config.height = 0;
        config.showDownloadLogs = "true".equals(System.getProperty("girafferun.web.debug", "false"));

        new WebApplication(new Main(), config);
    }
}
