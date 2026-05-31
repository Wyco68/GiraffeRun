# GiraffeRun — Project Specification

> **Version:** 1.0.0 | **Platform:** Desktop (Windows / macOS / Linux) | **Build System:** Gradle (multi-module) | **Language:** Java 8

---

## 1. Project Overview

**GiraffeRun** is a 2D top-down arcade dodge-and-collect desktop game built with the **libGDX** cross-platform game framework. The player controls a giraffe navigating a vertically scrolling world, dodging falling rockets while collecting crystals to advance through three progressively difficult levels.

The project was developed as an **Object-Oriented Programming coursework submission** (ISNE12, Group 9) and demonstrates core OOP design patterns within a real-time game loop.

### Core Design Pillars
| Pillar | Implementation |
|--------|---------------|
| Extensibility | Abstract base classes (`GameObject`, `Drop`) allow new entity types with one override |
| Separation of Concerns | Logic/Input/Draw split in `GameScreen.render()` |
| Platform Agnosticism | All game logic in `core` module; platform glue in `lwjgl3` module |
| Resource Lifecycle | Each screen owns and disposes its assets in `dispose()` |

---

## 2. System Architecture

### 2.1 High-Level Architecture

```
┌──────────────────────────────────────────────────────────────┐
│                    GiraffeRun Application                     │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                    lwjgl3 module                         │ │
│  │   Lwjgl3Launcher ──► StartupHelper (macOS JVM fork)      │ │
│  │        │                                                  │ │
│  │        ▼                                                  │ │
│  │   Lwjgl3Application (OpenGL window, event loop)           │ │
│  └──────────────────────┬──────────────────────────────────┘ │
│                          │ creates                            │
│  ┌───────────────────────▼──────────────────────────────────┐ │
│  │                    core module                            │ │
│  │                                                           │ │
│  │  Main (Game) ──────────────────────────────────────────  │ │
│  │   │ SpriteBatch │ BitmapFont │ FitViewport │ level/speed  │ │
│  │   │                                                       │ │
│  │   ├──► FirstScreen      (main menu)                       │ │
│  │   ├──► GameScreen       (core gameplay loop)              │ │
│  │   │      ├── BackGround  (scrolling texture)              │ │
│  │   │      ├── Player      (input, skills, stats)           │ │
│  │   │      └── Drop[]      (BulletDrop/HealthDrop/Crystal)  │ │
│  │   ├──► LoadScreen       (between levels)                  │ │
│  │   ├──► GameOverScreen   (death)                           │ │
│  │   └──► GameWinScreen    (all levels beaten)               │ │
│  └───────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

### 2.2 Module Breakdown

| Module | Artifact | Role |
|--------|----------|------|
| `core` | `core-1.0.0.jar` | Game logic, screens, entities — platform-agnostic |
| `lwjgl3` | `GiraffeRun-1.0.0.jar` (fat JAR) | Desktop launcher, LWJGL3 OpenGL backend, executable wrapping |

### 2.3 Rendering Pipeline

```
Gdx.app.postRunnable() (LWJGL3 event loop)
        │
        ▼
Main.render()
        │
        ▼
ActiveScreen.render(delta)
        │
   ┌────┴─────────────────┐
   │ input()               │  Keyboard + mouse polling
   │ logic()               │  Physics, collisions, timers
   │ draw()                │  SpriteBatch + Viewport projection
   └──────────────────────┘
```

---

## 3. Feature Breakdown

### 3.1 Player System

| Feature | Detail |
|---------|--------|
| Movement | Keyboard: `←`/`S` (left), `→`/`D` (right); speed = `globalSpeed + level` |
| Health | 5 HP max; reduced by BulletDrop collision if shield inactive |
| Shield | Right-click; 3 s active, 8 s cooldown; halves speed; absorbs one hit per activation |
| Teleport | Left-click at cursor position; 4 s cooldown; instant horizontal repositioning |
| Animation | 2-frame sprite swap at 0.15 s intervals; shield-active variants used when shield on |
| Boundary Clamping | `MathUtils.clamp()` prevents player from leaving viewport |

### 3.2 Drop System

The `Drop` abstract class defines the falling-object contract. All drops spawn at a random X position at world top and fall at `globalSpeed + level` (BulletDrop: 2.5× multiplier).

| Type | Trigger | Effect | Spawn Probability (Level 1 / 2 / 3) |
|------|---------|--------|--------------------------------------|
| `BulletDrop` | `onCatch()` → `player.getHit()` | −1 HP (unless shielded) | 55% / 60% / 65% |
| `HealthDrop` | `onCatch()` → `player.heal()` | +1 HP (capped at 5) | 12% / 14% / 16% |
| `CrystalDrop` | `onCatch()` → `player.collectCrystal()` | +1 crystal (need 5 to win) | 33% / 26% / 19% |

> **Note:** Probabilities are calculated as: `bulletProb = min(0.5 + level×0.05, 0.9)`, `healthProb = min(0.1 + level×0.02, 0.35)`, `crystalProb = 1 - bulletProb - healthProb`.

### 3.3 Level Progression

```
Level 1 ──[5 crystals]──► LoadScreen ──[SPACE]──► Level 2
Level 2 ──[5 crystals]──► LoadScreen ──[SPACE]──► Level 3
Level 3 ──[5 crystals]──────────────────────────► GameWinScreen
                                                    └──[SPACE]──► Level 1 (replay)
Any Level ──[HP = 0]────────────────────────────► GameOverScreen
                                                    └──[SPACE]──► Level 1 (restart)
```

**Level speed formula:** Drop speed = `globalSpeed(3.0f) + level(1–3)` → range 4.0–6.0 world-units/sec for crystals/health, 10.0–15.0 for bullets.

### 3.4 Audio System

| Sound | Type | Usage |
|-------|------|-------|
| `themeAudio.mp3` | Music (loop) | Active during gameplay; volume 0.3 |
| `hitSound.mp3` | Sound | Player hit without shield |
| `shieldHit.mp3` | Sound | Bullet absorbed by shield |
| `healSound.mp3` | Sound | Heart collected |
| `collectSound.mp3` | Sound | Crystal collected |
| `teleportSound.mp3` | Sound | Teleport activated |
| `gameOverSound.mp3` | Sound | Player death |
| `gameWinSound.mp3` | Sound | Level/game complete |

### 3.5 HUD System

All UI rendered in `GameScreen.draw()` using world-space coordinates:

| Element | Position | Content |
|---------|----------|---------|
| Heart icons | Top-left | 1 icon per remaining HP |
| Crystal count | Top-right | Crystal icon + `x N` text |
| Level indicator | Top-right corner | `Level: N` |
| Shield cooldown | Below hearts | Shield icon + remaining seconds |
| Teleport cooldown | Next to shield | Teleport icon + remaining seconds |

---

## 4. User Flows

### 4.1 New Game Flow
```
Launch App → FirstScreen (controls displayed) → [SPACE] → GameScreen (Level 1)
```

### 4.2 Level Clear Flow
```
GameScreen → crystalCollected >= 5 → gameWinSound.play()
  ├── if level < 3: level++ → LoadScreen → [SPACE] → new GameScreen
  └── if level == 3: level = 1 → GameWinScreen → [SPACE] → new GameScreen
```

### 4.3 Game Over Flow
```
GameScreen → health <= 0 → gameOverSound.play() → GameOverScreen → [SPACE] → new GameScreen (Level 1)
```

### 4.4 Shield Usage Flow
```
[RIGHT-CLICK] → activateShield()
  ├── if shieldCooldown > 0: rejected (no-op)
  └── if shieldCooldown <= 0:
        shieldActive = true | shieldTimer = 3s | shieldCooldown = 8s | speed /= 2
        ... 3s passes → shieldActive = false | speed = globalSpeed
        ... 8s passes → shieldCooldown <= 0 (ready again)
```

---

## 5. API Structure

> GiraffeRun is a standalone desktop application with no HTTP API, database, or external services. The following documents the internal class interface contracts.

### 5.1 `GameObject` (Abstract Base)
```java
abstract class GameObject {
    protected Sprite sprite;
    protected Rectangle rectangle;
    protected float speed;          // world-units per second
    abstract void update(float delta);
    void draw(SpriteBatch batch);
    Rectangle getRectangle();
    void setPosition(float x, float y);
    void dispose();
}
```

### 5.2 `Drop` (Abstract Falling Object)
```java
abstract class Drop extends GameObject {
    void update(float delta);       // translate Y by -speed*delta
    boolean overlaps(Rectangle r);  // AABB collision
    boolean isOffScreen();          // Y < -height
    abstract void onCatch(Player player);  // effect on collision
}
```

### 5.3 `Player`
```java
class Player extends GameObject {
    void moveLeft(float delta);
    void moveRight(float delta);
    void teleport(Vector2 touchPos);    // cooldown: 4s
    void activateShield();              // cooldown: 8s, duration: 3s
    void update(float delta);           // animation, cooldowns, clamping
    void getHit();                      // -1 HP unless shielded
    void heal();                        // +1 HP if < 5
    void collectCrystal();              // crystalCollected++
    void reset();                       // restore to initial state
    // Getters: getHealth, getCrystalCollected, getShieldCooldown, getTeleportCooldown
}
```

### 5.4 `Main` (Shared State)
```java
class Main extends Game {
    SpriteBatch batch;       // shared renderer
    BitmapFont font;         // shared HUD font
    FitViewport viewport;   // 8×5 world units
    int level;               // 1–3 (mutated by GameScreen)
    float globalSpeed;       // 3.0f (base drop/scroll speed)
}
```

---

## 6. Data Models

### 6.1 Player State
```
PlayerState {
  health:           int     [0–5]         initial: 5
  crystalCollected: int     [0–5]         initial: 0
  shieldActive:     boolean              initial: false
  shieldTimer:      float   [0–3s]       active duration remaining
  shieldCooldown:   float   [0–8s]       time until shield ready
  teleportCooldown: float   [0–4s]       time until teleport ready
  speed:            float                 = globalSpeed + level (halved when shielded)
  positionX:        float                 [0, worldWidth - playerWidth]
  positionY:        float                 fixed (bottom of screen area)
  animationTimer:   float   [0–0.15s]    frame toggle timer
  running:          boolean              current animation frame toggle
}
```

### 6.2 Drop State
```
DropState {
  positionX: float    [0, worldWidth - dropWidth]   (randomised on spawn)
  positionY: float    [worldHeight → -dropHeight]   (falls each frame)
  speed:     float    = (globalSpeed + level) × multiplier
  type:      enum     BULLET | HEALTH | CRYSTAL
}
```

### 6.3 Game Global State (Main)
```
GameState {
  level:       int    [1–3]
  globalSpeed: float  3.0f  (constant in current version)
}
```

---

## 7. Security Design

> As a self-contained desktop game binary, GiraffeRun has no network surface, user authentication, external API calls, or database. Security considerations are limited to:

| Area | Consideration |
|------|--------------|
| **JAR Integrity** | The fat JAR excludes `META-INF/*.SF`, `*.DSA`, `*.RSA` signature files to prevent classpath conflicts — not a security hardening measure |
| **Asset Loading** | Assets are loaded from the classpath (`Gdx.files.internal`) — not from user-supplied paths — eliminating path traversal risk |
| **Input Validation** | Player X position is clamped via `MathUtils.clamp()`; no unchecked user text input exists |
| **Native Code** | LWJGL3 loads native OpenGL libraries; these are bundled within the JAR from trusted libGDX distribution |
| **GraalVM Native** | Native image generation is disabled by default (`enableGraalNative=false`); would require reflection config if enabled |

---

## 8. Performance Strategy

### 8.1 Current Optimisations
| Strategy | Implementation |
|----------|---------------|
| VSync + FPS cap | `configuration.useVsync(true)` + `setForegroundFPS(refreshRate + 1)` |
| Incremental compilation | `options.incremental = true` in `compileJava` |
| Texture filter | `Texture.TextureFilter.Nearest` on background — avoids bilinear filtering overhead |
| Reverse-order drop cleanup | `drops` array iterated in reverse; `removeIndex(i)` is O(n) but safe |
| FitViewport | Single-camera projection; no multi-pass rendering |

### 8.2 Known Performance Risks
| Risk | Severity | Mitigation |
|------|----------|-----------|
| Eager texture loading per screen | MEDIUM | Migrate to `AssetManager` with async loading |
| No object pooling for drops | MEDIUM | Implement `Pool<BulletDrop>`, `Pool<CrystalDrop>`, `Pool<HealthDrop>` |
| Drop array uses libGDX `Array<Drop>` (already GC-efficient) | LOW | Already mitigated vs `ArrayList` |
| All audio loaded at GameScreen construction | LOW | Consider lazy load or AssetManager |

---

## 9. Deployment Architecture

```
Build Phase (Gradle)
──────────────────
:lwjgl3:jar         → GiraffeRun-1.0.0.jar   (cross-platform fat JAR, ~26 MB)
:lwjgl3:jarWin      → GiraffeRun-1.0.0-win.jar
:lwjgl3:jarMac      → GiraffeRun-1.0.0-mac.jar
:lwjgl3:jarLinux    → GiraffeRun-1.0.0-linux.jar

Native Executable (Construo plugin — optional)
──────────────────────────────────────────────
Target: winX64      → GiraffeRun.exe + embedded OpenJDK 17.0.15
Target: macM1       → GiraffeRun.app (AARCH64) + embedded JDK
Target: macX64      → GiraffeRun.app (x86_64)  + embedded JDK
Target: linuxX64    → GiraffeRun binary + embedded JDK

Distribution
────────────
Option A: Direct JAR — requires user to have JVM 8+ installed
Option B: Native executable — self-contained, no JVM requirement for end user
Option C: GitHub Releases — upload JAR as release asset (current approach per README)
```

---

## 10. Future Scalability Plan

| Priority | Enhancement | Rationale |
|----------|------------|-----------|
| HIGH | `AssetManager` integration | Eliminates stuttering on screen transition; enables progress bars |
| HIGH | GameState enum / state machine | Decouples screen logic; enables pause, settings, difficulty selection |
| HIGH | Object pooling for drops | Required for > 10 simultaneous drops without GC spikes |
| MEDIUM | Score/leaderboard via libGDX Preferences | Adds player retention; no backend required |
| MEDIUM | More levels (levels 4+) | Requires only new BG texture and spawn probability table |
| MEDIUM | Controller / gamepad support | libGDX `gdx-controllers` extension available |
| MEDIUM | Mobile ports (Android / iOS) | libGDX supports both; touch input already partially handled via mouse |
| LOW | Particle effects | `libgdx-particleeditor` for teleport/crystal VFX |
| LOW | DistanceField / FreeType fonts | Crisp text at all resolutions |
| LOW | Localisation (i18n) | libGDX `I18NBundle` for multi-language support |
