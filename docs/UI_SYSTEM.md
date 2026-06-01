# GiraffeRun UI System

TeaVM-safe LibGDX Scene2D + bitmap fonts. Gameplay sprites use the **8×5** `FitViewport`; HUD chrome uses a separate **1280×720** `ExtendViewport`.

## Adaptive layout (in-game)

| Component | Role |
|-----------|------|
| `UiScreenProfile` | Screen class (mobile &lt;768, tablet, desktop ≥1200), portrait flag, `uiScale = min(w/1280, h/720)`, safe pad, touch size |
| `ViewportStage` | `ExtendViewport(1280, 720)` + dedicated `Stage` |
| `AdaptiveHudLayout` | Root `Table`, `buildMobileLayout()` / `buildDesktopLayout()`, rebuild on resize only |
| `AdaptiveGameUi` | Facade: HUD stage draw + touch overlay sync + input mode |
| `HudLayoutMetrics` | Gameplay-world rects for touch controls (mapped from UI slots) |

### Layout modes

**Desktop / landscape tablet** (`buildDesktopLayout`):

- Top-left: health bar  
- Top-center: (open playfield — no level/crystal text)  
- Top-right: shield / teleport icons  
- Bottom-center: crystal progress bar  

**Mobile portrait** (`buildMobileLayout`):

- Top: health bar; pause slot under health  
- Bottom-left: movement touch slots  
- Bottom-center: crystal bar  
- Bottom-right: skills + shield touch  

### Touch / input

- `touchUiEnabled()` → mobile + tablet (on-screen controls)  
- Desktop (≥1200px): keyboard/mouse only; touch overlay hidden  
- Touch targets: mobile **192–272px**, tablet **128–224px** (design px, clamped, 2× base)  
- `root.pad(10 × scale)` safe margins on the UI table  

### Draw order (`GameScreen`)

1. Gameplay `SpriteBatch` — world, effects, player  
2. UI `Stage` — HUD actors  
3. Gameplay `SpriteBatch` — touch button chrome (gameplay coordinates)  

### Resize

```java
@Override
public void resize(int width, int height) {
  game.viewport.update(width, height, true);
  adaptiveUi.resize(width, height); // rebuilds table + touch metrics
}
```

## Font hierarchy

| Tier | Scale (× base) | Color | Usage |
|------|----------------|-------|--------|
| **Title** | 1.35 | `#F2FAFF` | Menu title, Game Over / Win headings, pause title |
| **Subtitle** | 1.10 | `#BFE0F2` | Taglines, scores, level transition |
| **Body** | 1.00 | `#D9EBF5` | Controls, hints |
| **HUD** | 0.85 | `#00CCE6` | In-game counters, cooldown numbers |

- Base scale: `worldHeight / 480` × `UiScreenProfile.uiScale` for gameplay HUD text.  
- UI stage HUD text uses UI viewport world height × `uiScale`.  
- Line height multiplier: **1.28** (`FontFactory.LINE_HEIGHT_FACTOR`).

## Spacing (8px grid)

| Token | px | World units |
|-------|-----|-------------|
| Unit | 8 | `UiSpacing.pxToWorld(8, h)` |
| Small | 8 | `small(h)` |
| Medium | 16 | `medium(h)` |
| Large | 24 | `large(h)` |
| XL | 32 | `xl(h)` |
| Touch min | 48+ | `UiScreenProfile.touchButtonPx` → gameplay world |
| UI icon | 56 | `iconSize(h)` |

Reference height: **480px** (gameplay), **720px** (UI viewport).

## TeaVM / performance

- No HTML/CSS UI; no FreeType on WebGL path.  
- HUD rebuild **only on resize**, not each frame.  
- Reuse HUD actors (`HudHealthBarActor`, etc.).  
- Gameplay + UI viewports kept separate to avoid camera/projection bugs.
