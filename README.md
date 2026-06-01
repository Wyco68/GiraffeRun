# 🦒 GiraffeRun

> A 2D top-down arcade game where a giraffe dodges falling rockets and collects crystals to advance through three progressively challenging levels — available in the **browser (TeaVM WebAssembly)** and as a **desktop app (libGDX / Java)**.

<br/>

[![Java](https://img.shields.io/badge/Java-8%2B-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![libGDX](https://img.shields.io/badge/libGDX-1.13.1-red?style=flat-square)](https://libgdx.com/)
[![TeaVM](https://img.shields.io/badge/TeaVM-WASM-blue?style=flat-square)](https://teavm.org/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue?style=flat-square&logo=gradle)](https://gradle.org/)
[![Platform](https://img.shields.io/badge/Platform-Web%20%7C%20Desktop-lightgrey?style=flat-square)]()
[![License](https://img.shields.io/badge/License-Academic-green?style=flat-square)]()

---

## Play in Browser

Compiles the LibGDX `core` module to **WebAssembly** via [gdx-teavm](https://github.com/xpenatan/gdx-teavm). See [`teavm/README.md`](teavm/README.md).

```bash
# One-time: clone gdx-teavm + apply GiraffeRun patches (not stored in this repo)
./scripts/setup-teavm.ps1          # Windows
# ./scripts/setup-teavm.sh         # macOS / Linux

# Build WASM bundle → teavm/build/dist/webapp/
./gradlew :teavm:buildWasm

# Build and serve at http://localhost:8080
./gradlew :teavm:runWasm
```

Requires **JDK 17+**. Deploy by uploading `teavm/build/dist/webapp/` to any static host.

---

## ✨ Key Features

- 🦒 **Animated Giraffe Player** — Smooth 2-frame sprite animation with directional and shield variants
- 🚀 **Falling Drop System** — Three drop types: rockets (hazard), hearts (heal), crystals (objective)
- 🛡️ **Shield Ability** — Press `S` (or right-click) to activate a 3-second damage shield with an 8-second cooldown
- ⚡ **Teleport Ability** — Left-click anywhere on screen to instantly reposition; 4-second cooldown
- 🎯 **3 Progressive Levels** — Each level increases drop speed and bullet spawn probability
- 🎨 **Unique Level Backgrounds** — Distinct scrolling backgrounds for each of the 3 levels
- 🎵 **Full Audio System** — Looping background music + 7 contextual sound effects
- 📊 **Live HUD** — Health bar, crystal progress bar, and ability cooldown timers (no level text overlay)
- 📱 **Adaptive UI** — LibGDX Scene2D (`Stage` + `Table`); separate HUD viewport; mobile portrait vs desktop layouts; touch targets scale with screen (see [`docs/UI_SYSTEM.md`](docs/UI_SYSTEM.md))
- 🖥️ **Resolution Independent** — Gameplay `FitViewport(8×5)`; HUD `ExtendViewport(1280×720)`; layout rebuilds on resize only
- 📦 **Single-JAR Distribution** — Cross-platform fat JAR requires only a JVM to run
- 🌐 **Browser build** — TeaVM WebAssembly (same Java game code as desktop)

---

## 🏗️ Tech Stack

### Web — TeaVM WASM (`teavm/`)

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java (same `core` module) | 11+ (teavm module) |
| Compiler | TeaVM | 0.14 |
| Backend | gdx-teavm | 1.5.6 |
| Output | WebAssembly GC | `app.wasm` |

### Desktop (Java)

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 8 (source/target) |
| Game Framework | libGDX | 1.13.1 |
| Desktop Backend | LWJGL3 (OpenGL) | bundled with libGDX |
| Build System | Gradle (multi-module) | 8.x |
| Native Packaging | Construo | 1.7.1 |
| IDE Support | Eclipse / IntelliJ IDEA | via Gradle plugins |

---

## 🗂️ Architecture Overview

```
GiraffeRun/
├── teavm/                         # Browser WASM (TeaVM + gdx-teavm)
│   └── src/main/java/.../teavm/   # WebLauncher, TeaVMBuilder
├── tools/gdx-teavm/               # Local clone (gitignored; run scripts/setup-teavm.*)
│
├── core/                          # Platform-agnostic game logic (Java)
│   └── src/main/java/
│       └── isne12/gp9/runner/
│           ├── Main.java           # Application entry + shared state
│           ├── GameObject.java     # Abstract base (sprite, rectangle, speed)
│           ├── Drop.java           # Abstract falling-object base
│           ├── Player.java         # Player entity (movement, skills, stats)
│           ├── BackGround.java     # Vertically scrolling background
│           ├── BulletDrop.java     # Hazard drop — deals damage
│           ├── HealthDrop.java     # Healing drop — restores HP
│           ├── CrystalDrop.java    # Objective drop — advance level
│           ├── GameScreen.java     # Core gameplay loop (input/logic/draw)
│           ├── AdaptiveGameUi.java # Adaptive HUD + touch overlay
│           ├── AdaptiveHudLayout.java # Table layouts (mobile / desktop)
│           ├── UiScreenProfile.java  # Screen class + uiScale
│           ├── FirstScreen.java    # Main menu
│           ├── LoadingScreen.java    # Asset splash
│           ├── LoadScreen.java     # Between-level transition
│           ├── GameOverScreen.java # Death screen
│           └── GameWinScreen.java  # Victory screen
│
├── lwjgl3/                        # Desktop launcher (LWJGL3 / OpenGL)
│   └── src/main/java/
│       └── isne12/gp9/runner/lwjgl3/
│           ├── Lwjgl3Launcher.java # Main entry point for desktop
│           └── StartupHelper.java  # macOS JVM fork + Windows helper
│
├── assets/                        # All game textures and audio
├── build.gradle                   # Root Gradle configuration
├── settings.gradle                # Module inclusion (core, lwjgl3)
├── gradle.properties              # Version pins (gdxVersion=1.13.1)
└── GiraffeRun.jar                 # Pre-built distributable
```

**Screen flow:**

```
LoadingScreen ──► FirstScreen ──[SPACE]──► GameScreen ──[5 crystals, level < 4]──► LoadScreen ──► GameScreen
                                                      ──[5 crystals, level = 4]──► GameWinScreen ──[SPACE]──► GameScreen
                                                      ──[HP = 0]──────────────────► GameOverScreen ──[SPACE]──► GameScreen
```

---

## ⚙️ Setup & Prerequisites

### Requirements

| Dependency | Minimum Version | Notes |
|-----------|----------------|-------|
| JDK | 8 | Any distribution (Temurin, Corretto, etc.) |
| Gradle | 8.x | Wrapper (`gradlew`) included — **no install needed** |
| Git | Any | For cloning |
| OS | Windows / macOS / Linux | Native libs bundled in JAR |

> **macOS users:** The `StartupHelper` class automatically forks a new JVM process with the required `-XstartOnFirstThread` flag. No manual action needed.

---

## 🚀 How to Run Locally

### Option A — Run from Pre-built JAR (Simplest)

```bash
# Download GiraffeRun.jar from the repository root, then:
java -jar GiraffeRun.jar
```

### Option B — Build and Run from Source

```bash
# 1. Clone the repository
git clone https://github.com/Wyco68/GiraffeRun.git
cd GiraffeRun

# 2. Run the game using the Gradle wrapper (no Gradle install required)
./gradlew lwjgl3:run
```

### Option C — Build a distributable JAR

```bash
# Cross-platform fat JAR (Windows + macOS + Linux, ~26 MB)
./gradlew lwjgl3:jar

# Platform-specific JARs (smaller file sizes):
./gradlew lwjgl3:jarWin     # Windows only
./gradlew lwjgl3:jarMac     # macOS only
./gradlew lwjgl3:jarLinux   # Linux only

# Output location:
# lwjgl3/build/libs/GiraffeRun-1.0.0.jar
```

---

## 🎮 Controls

| Action | Input |
|--------|-------|
| Move Right | `→` Arrow or `D` |
| Move Left | `←` Arrow or `A` |
| Activate Shield | `S` or Right Mouse Button |
| Teleport | Left Mouse Button / tap playfield (not on HUD buttons) |
| Pause | `Esc` (in-game) |
| Start Game / Next Level / Restart | `SPACE` |

**Mobile / tablet (touch UI on):** bottom-left hold buttons to move; bottom-right shield; pause under health bar. Desktop (width ≥1200px): keyboard/mouse only.

---

## 🌍 Environment Variables

> GiraffeRun is a fully self-contained desktop game. **No environment variables are required.**

Gradle build properties are configured in `gradle.properties`:

```properties
gdxVersion=1.13.1          # libGDX version
projectVersion=1.0.0       # Game version (used in JAR filename)
graalHelperVersion=2.0.1   # GraalVM helper (unused unless enableGraalNative=true)
enableGraalNative=false    # Set to true to build a GraalVM native image
```

---

## 📦 Deployment

### Release via GitHub

1. Build the cross-platform JAR:
   ```bash
   ./gradlew lwjgl3:jar
   ```
2. Create a new GitHub Release tagged with the version (e.g., `v1.0.0`)
3. Attach `lwjgl3/build/libs/GiraffeRun-1.0.0.jar` as a release asset
4. End users download and run: `java -jar GiraffeRun.jar`

### Native Executable (Self-Contained, No JVM Required)

Uses the **Construo** Gradle plugin to bundle OpenJDK 17 with the game:

```bash
# Build Windows native executable
./gradlew lwjgl3:packageWinX64

# Build macOS (Apple Silicon) bundle
./gradlew lwjgl3:packageMacM1

# Build macOS (Intel) bundle
./gradlew lwjgl3:packageMacX64

# Build Linux x64 binary
./gradlew lwjgl3:packageLinuxX64
```

Output bundles are placed in `lwjgl3/build/construo/`.

---

## 🧪 Development Notes

### Adaptive UI

- In-game HUD: `AdaptiveGameUi` → `AdaptiveHudLayout` on `ViewportStage` (`ExtendViewport` 1280×720)
- Screen buckets: mobile (`width < 768`), tablet, desktop (`width ≥ 1200`); portrait uses `buildMobileLayout()`, else `buildDesktopLayout()`
- Full layout, spacing, and TeaVM notes: [`docs/UI_SYSTEM.md`](docs/UI_SYSTEM.md)

### Project Structure Notes

- All textures and audio must be placed in the **`assets/`** directory at the project root — it is included in the LWJGL3 module's resources source set automatically
- An `assets.txt` manifest is auto-generated by the `generateAssetList` Gradle task before each resource processing step
- The working directory at runtime is set to `assets/` so `Gdx.files.internal("filename")` resolves relative to that folder

### Adding a New Drop Type

1. Create a new class extending `Drop` in `core/src/main/java/isne12/gp9/runner/`
2. Implement `onCatch(Player player)` with the desired effect
3. Add a new texture asset to `assets/`
4. Load the texture in `GameScreen` constructor
5. Add spawn logic in `GameScreen.createDrop()` with appropriate probability weight
6. Dispose the texture in `GameScreen.dispose()`

### Adding a New Level

1. Add a tier entry to `LevelConfig.BY_TIER` and raise `LevelConfig.MAX_LEVEL`
2. Add a background texture and wire it in `Assets.getLevelBackgroundPath()`
3. Tune spawn weights in `GameScreen.createDrop()` for the new tier

---

## 👥 Credits

- **Developer:** Wyco (CourseWork: ISNE12 Group 9 — Object-Oriented Programming)
- **Framework:** [libGDX](https://libgdx.com/) by BadLogic Games
- **Build Tooling:** [Gradle](https://gradle.org/), [Construo](https://github.com/fourlastor/construo)
- **Runtime:** [LWJGL3](https://www.lwjgl.org/)

---

## 📄 License

This project was developed as an academic coursework submission. All game assets are for educational use only.
