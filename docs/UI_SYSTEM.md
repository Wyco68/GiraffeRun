# GiraffeRun UI System

TeaVM-safe LibGDX Scene2D + bitmap fonts. Gameplay sprites are unchanged.

## Font hierarchy

| Tier | Scale (× base) | Color | Usage |
|------|----------------|-------|--------|
| **Title** | 1.35 | `#F2FAFF` | Menu title, Game Over / Win headings, pause title |
| **Subtitle** | 1.10 | `#BFE0F2` | Taglines, scores, level transition |
| **Body** | 1.00 | `#D9EBF5` | Controls, hints |
| **HUD** | 0.85 | `#00CCE6` | In-game counters, cooldown numbers |

- Base scale: `worldHeight / 480` (see `FontFactory`, `Main.updateFontScale()`).
- Line height multiplier: **1.28** (`FontFactory.LINE_HEIGHT_FACTOR`).
- Implementation: `Fonts.java` (four `BitmapFont` instances, default LibGDX glyph atlas — no FreeType on WebGL).

## Spacing (8px grid)

| Token | px | World units |
|-------|-----|-------------|
| Unit | 8 | `UiSpacing.pxToWorld(8, h)` |
| Small | 8 | `small(h)` |
| Medium | 16 | `medium(h)` |
| Large | 24 | `large(h)` |
| XL | 32 | `xl(h)` |
| Touch min | 48 | `touchTarget(h)` |
| UI icon | 56 | `iconSize(h)` |

Reference height: **480px** (`UiSpacing.REF_PIXEL_HEIGHT`).

## Touch icon assets (new — do not replace gameplay icons)

| File | Purpose |
|------|---------|
| `assets/ui/iconSettings.png` | Settings (reserved) |
| `assets/ui/iconSettingsPressed.png` | Pressed |
| `assets/ui/iconSound.png` | Music toggle |
| `assets/ui/iconSoundPressed.png` | Pressed |
| `assets/ui/iconPause.png` | Pause / resume |
| `assets/ui/iconPausePressed.png` | Pressed |
| `assets/ui/iconBack.png` | Quit to menu |
| `assets/ui/iconBackPressed.png` | Pressed |
| `assets/ui/iconRetry.png` | Restart / play again |
| `assets/ui/iconRetryPressed.png` | Pressed |

Size: **56×56** px, transparent PNG. Loaded via `Assets` + `UiIcons`.

**Unchanged assets:** `shieldIcon.png`, `teleportIcon.png`, `heart.png`, `crystal.png` (textures only; HUD uses bars + scaled draw rects).

## Layout structure

### Main menu (`FirstScreen`)
- Background: `MenuScreen.png` (batch).
- Scene2D: top-right sound toggle; centered title → subtitle → **PLAY** → controls; bottom stats.

### In-game HUD (`GameScreen` + `HUDManager`)
- **Top-left:** 5-segment red health bar.
- **Top-right:** shield / teleport icons; cooldown seconds in HUD font (`UiColors.HUD`).
- **Bottom-right:** compact crystal progress bar (no numeric label).
- Campaign uses `BG1`–`BG4` per level. Center clear for player/drops.

### Pause overlay
- Dim fill + centered panel (`UiFactory.dimPanel`).
- Icon row: resume (pause icon), retry, back.

### Level transition / Game Over / Win
- Full-screen art + centered dim panel + title / stats / retry icon.

## LibGDX implementation

- **Layout:** `Table` on `ViewportStage` (shared `FitViewport` 8×5).
- **Icons:** `ImageButton` + `UiFactory.iconButton`.
- **Text:** `Label` (menus) or `UiText` + `fonts.hud` (HUD batch).
- **Overlay panels:** `whitePixel` tinted via `Table.setColor`.

## TeaVM / performance

- No HTML/CSS UI; no FreeType on WebGL path.
- Four bitmap fonts share the same atlas texture (four wrappers) — acceptable draw cost.
- UI icons: 10 small textures, nearest filter, batched by Scene2D.
- Rebuild UI on resize only (not every frame).
- Prefer batch HUD for gameplay; Scene2D for interactive chrome.
