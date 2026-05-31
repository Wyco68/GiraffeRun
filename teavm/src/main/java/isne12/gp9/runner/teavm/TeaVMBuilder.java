package isne12.gp9.runner.teavm;

import com.github.xpenatan.gdx.teavm.backends.shared.config.AssetFileHandle;
import com.github.xpenatan.gdx.teavm.backends.shared.config.compiler.TeaCompiler;
import com.github.xpenatan.gdx.teavm.backends.web.config.backend.WebBackend;
import java.io.File;
import org.teavm.vm.TeaVMOptimizationLevel;

/**
 * Builds the browser bundle (WebAssembly by default) using gdx-teavm.
 *
 * <p>Gradle tasks {@code :teavm:runWasm} and {@code :teavm:buildWasm} invoke this class.</p>
 */
public class TeaVMBuilder {

    public static void main(String[] args) {
        boolean startServer = false;
        boolean webAssembly = true;

        for (String arg : args) {
            if ("run".equals(arg)) {
                startServer = true;
            } else if ("js".equals(arg)) {
                webAssembly = false;
            } else if ("wasm".equals(arg)) {
                webAssembly = true;
            }
        }

        String assetsPath = System.getProperty("assets.path", "../assets");

        WebBackend backend = new WebBackend()
            .setWebAssembly(webAssembly)
            .setHtmlTitle("GiraffeRun")
            .setHtmlWidth(0)
            .setHtmlHeight(0)
            .setStartJettyAfterBuild(startServer);

        new TeaCompiler(backend)
            .addAssets(new AssetFileHandle(assetsPath))
            .setOptimizationLevel(TeaVMOptimizationLevel.SIMPLE)
            .setMainClass(WebLauncher.class.getName())
            .setObfuscated(false)
            .addReflectionClass("isne12.gp9.runner")
            .addReflectionClass("com.badlogic.gdx.utils.reflect")
            .build(new File("build/dist"));
    }
}
