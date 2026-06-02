package isne12.gp9.runner;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

/** Debug logging; disabled on web unless {@code -Dgirafferun.web.debug=true}. */
public final class WebLog {
    private static final boolean DEBUG = isDebugEnabled();

    private WebLog() {
    }

    private static boolean isDebugEnabled() {
        if (Gdx.app == null) {
            return !"false".equals(System.getProperty("girafferun.web.debug", "false"));
        }
        if (Gdx.app.getType() != Application.ApplicationType.WebGL) {
            return true;
        }
        return "true".equals(System.getProperty("girafferun.web.debug", "false"));
    }

    public static void debug(String tag, String message) {
        if (DEBUG) {
            Gdx.app.log(tag, message);
        }
    }

    public static void error(String tag, String message) {
        Gdx.app.error(tag, message);
    }

    public static void error(String tag, String message, Throwable cause) {
        Gdx.app.error(tag, message, cause);
    }
}
