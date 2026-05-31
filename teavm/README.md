# GiraffeRun — TeaVM WebAssembly

Browser build that compiles the **existing Java LibGDX game** (`core` module) to **WebAssembly** using [gdx-teavm](https://github.com/xpenatan/gdx-teavm) and TeaVM 0.14.

This reuses the same `Main`, screens, entities, and assets as the desktop JAR.

## Prerequisites

- **JDK 17+** (Gradle and TeaVM tooling)
- **gdx-teavm 1.5.6** vendored at [`../tools/gdx-teavm/`](../tools/gdx-teavm/) (included via Gradle `includeBuild`)

First-time setup (if `tools/gdx-teavm` is missing):

```bash
git clone --depth 1 --branch 1.5.6 https://github.com/xpenatan/gdx-teavm.git tools/gdx-teavm
```

Or run [`../scripts/setup-teavm.ps1`](../scripts/setup-teavm.ps1) on Windows.

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

The game canvas uses **75%** of the browser viewport (centered on a black background). Change `WebLauncher.VIEWPORT_SCALE` to adjust.

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

## Deploy (static hosting)

Upload the contents of `teavm/build/dist/webapp/` to any static host (Vercel, Netlify, GitHub Pages).

For Vercel, set **Root Directory** to `teavm/build/dist/webapp` after building, or add a CI step that runs `:teavm:buildWasm` first.

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
