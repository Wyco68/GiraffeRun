# GiraffeRun — TeaVM WebAssembly

Browser build that compiles the **existing Java LibGDX game** (`core` module) to **WebAssembly** using [gdx-teavm](https://github.com/xpenatan/gdx-teavm) and TeaVM 0.14.

This reuses the same `Main`, screens, entities, and assets as the desktop JAR.

## Run on desktop

The LWJGL3 desktop build uses the same `core` code. From the repo root:

```bash
java -jar GiraffeRun.jar              # pre-built JAR

gradlew.bat lwjgl3:run                # Windows — from source
./gradlew lwjgl3:run                  # macOS / Linux — from source
```

See the root [`README.md`](../README.md) for JAR packaging, native executables (Construo), and controls.

## Prerequisites

- **JDK 17+** (Gradle and TeaVM tooling)
- **gdx-teavm 1.5.6** — **not** in this repo; cloned locally to `tools/gdx-teavm/` (Gradle `includeBuild`)

First-time setup:

```bash
./scripts/setup-teavm.ps1          # Windows
# ./scripts/setup-teavm.sh         # macOS / Linux
```

This clones [gdx-teavm](https://github.com/xpenatan/gdx-teavm) tag `1.5.6` and applies patches from `scripts/gdx-teavm-patches/` (75% viewport, Windows build fix).

## Build WebAssembly

From the repo root:

```bash
./gradlew :teavm:buildWasm
```

Output: `teavm/build/dist/webapp/`

- `index.html` — loader page
- `app.wasm` — compiled game (WebAssembly GC, ~3.9 MB)
- `wasm-gc-runtime.min.js` — TeaVM runtime
- `assets/` — game textures and audio

## Run locally (build + Jetty server)

```bash
./gradlew :teavm:runWasm
```

Open **http://localhost:8080**

The game canvas uses the full browser window by default (`WebLauncher.VIEWPORT_SCALE = 1`). Lower the value (e.g. `0.75`) to letterbox inside the page. UI is clamped to safe margins via `UiBounds`.

### Windows: "Unable to delete … backend-web-1.5.6.jar"

A Gradle daemon or a previous TeaVM server may still have the JAR open. Stop daemons, then retry:

```bash
./gradlew --stop
./gradlew :teavm:runWasm
```

## JavaScript fallback

```bash
./gradlew :teavm:buildJs
./gradlew :teavm:runJs
```

## Deploy (Vercel)

From the repo root (requires **JDK 17+** on the build machine):

```bash
./gradlew :teavm:patchWebIndex
```

This runs `buildWasm` and copies the production `index.html` (relative `./` paths, WASM preload, mobile viewport).

- **Vercel**: `vercel.json` at the repo root sets `outputDirectory` to `teavm/build/dist/webapp` and `buildCommand` to `gradlew.bat :teavm:patchWebIndex` (Windows builders) or `gradlew :teavm:patchWebIndex` on Linux/macOS.
- **Pre-built deploy**: run the Gradle task locally, then `vercel deploy --prebuilt` with output `teavm/build/dist/webapp`.

Debug logs in the browser: `-Dgirafferun.web.debug=true` when running `:teavm:runWasm`.

## Architecture

```
WebLauncher.java  →  WebApplication(new Main())
TeaVMBuilder.java →  TeaCompiler + WebBackend.setWebAssembly(true)
core/             →  unchanged game logic (shared with desktop)
```

## Known limitations (gdx-teavm)

- **Audio** may be limited or silent in the browser; gdx-teavm uses Howler.js stubs — test in your target browser.
- **Fonts** use libGDX `BitmapFont` on web (see `FontFactory.java`) for TeaVM compatibility.
- **Right-click** shield may need browser permission for context menu; **S** activates shield on keyboard.

## Gradle tasks

| Task | Description |
|------|-------------|
| `:teavm:buildWasm` | Compile to WASM, output to `build/dist/webapp` |
| `:teavm:runWasm` | Build WASM and serve on port 8080 |
| `:teavm:buildJs` | Compile to JavaScript instead |
| `:teavm:runJs` | Build JS and serve on port 8080 |
