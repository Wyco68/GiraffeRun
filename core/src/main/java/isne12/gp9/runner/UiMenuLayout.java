package isne12.gp9.runner;

/** Keeps menu / overlay buttons inside the gameplay {@link FitViewport} safe area. */
public final class UiMenuLayout {
    public static final class MenuLayout {
        /** Baseline Y for the top title line. */
        public float titleBaselineY;
        /** Baseline Y for the first hint line (below buttons). */
        public float hintBaselineY;
    }

    private UiMenuLayout() {
    }

    public static float hintBlockHeight(Main game, float worldHeight, int hintLines) {
        if (hintLines <= 0) {
            return 0f;
        }
        float line = MenuText.lineHeight(game, McUi.BODY_MULT);
        float gap = UiSpacing.medium(worldHeight);
        return hintLines * line + gap * (hintLines - 1);
    }

    private static float titleBlockHeight(Main game, float worldHeight, int titleLineCount) {
        if (titleLineCount <= 0) {
            return 0f;
        }
        float gap = UiSpacing.medium(worldHeight);
        float h = MenuText.lineHeight(game, McUi.TITLE_MULT) + gap;
        if (titleLineCount > 1) {
            h += MenuText.lineHeight(game, McUi.SUBTITLE_MULT) + gap;
        }
        return h;
    }

    /**
     * Centers title + buttons + hints vertically. {@code buttonsTopToBottom[0]} is the topmost button.
     */
    public static MenuLayout layoutMenuBlock(Main game, float worldWidth, float worldHeight,
                                             McButton[] buttonsTopToBottom, String[] labels,
                                             int titleLineCount, int hintLineCount) {
        MenuLayout layout = new MenuLayout();
        float cx = worldWidth / 2f;
        float gap = UiSpacing.medium(worldHeight);
        float btnW = Math.min(worldWidth * 0.72f, worldWidth - UiBounds.margin(worldHeight) * 2f);

        float safeBottom = UiBounds.safeBottom(worldHeight);
        float safeTop = UiBounds.safeTop(worldHeight);
        float titleBlock = titleBlockHeight(game, worldHeight, titleLineCount);
        float hintBlock = hintBlockHeight(game, worldHeight, hintLineCount);
        int count = buttonsTopToBottom.length;
        float avail = Math.max(0.35f, safeTop - safeBottom);
        float btnH = Math.min(Math.max(UiSpacing.touchTarget(worldHeight) * 0.42f, 0.28f),
            (avail - titleBlock - hintBlock - gap * Math.max(0, count - 1)) / Math.max(1, count));

        float btnStackH = count * btnH + gap * Math.max(0, count - 1);
        float totalH = titleBlock + btnStackH + hintBlock;
        float blockTop = (safeBottom + safeTop) * 0.5f + totalH * 0.5f;
        blockTop = Math.min(blockTop, safeTop);
        layout.titleBaselineY = blockTop;

        float y = blockTop - titleBlock;
        for (int i = 0; i < count; i++) {
            y -= btnH;
            float x = UiBounds.clampX(cx - btnW / 2f, btnW, worldWidth, worldHeight);
            buttonsTopToBottom[i].set(x, Math.max(safeBottom, y), btnW, btnH, labels[i]);
            y -= gap;
        }
        layout.hintBaselineY = buttonsTopToBottom[count - 1].getBounds().y - gap;
        return layout;
    }

    public static MenuLayout layoutSingleButton(Main game, float worldWidth, float worldHeight,
                                                McButton button, String label,
                                                int titleLineCount, int hintLineCount) {
        return layoutMenuBlock(game, worldWidth, worldHeight,
            new McButton[] { button }, new String[] { label }, titleLineCount, hintLineCount);
    }

    public static MenuLayout layoutButtonsCentered(Main game, float worldWidth, float worldHeight,
                                                   McButton[] buttonsBottomFirst, String[] labels,
                                                   int hintLinesBelow) {
        McButton[] topFirst = new McButton[buttonsBottomFirst.length];
        String[] topLabels = new String[labels.length];
        for (int i = 0; i < buttonsBottomFirst.length; i++) {
            int j = buttonsBottomFirst.length - 1 - i;
            topFirst[i] = buttonsBottomFirst[j];
            topLabels[i] = labels[j];
        }
        return layoutMenuBlock(game, worldWidth, worldHeight, topFirst, topLabels, 0, hintLinesBelow);
    }

    public static void layoutSoundToggle(Main game, McButton sound, float worldWidth, float worldHeight) {
        float btnH = Math.max(UiSpacing.touchTarget(worldHeight) * 0.38f, 0.24f);
        float btnW = Math.min(worldWidth * 0.28f, worldWidth * 0.32f);
        float x = UiBounds.clampX(UiBounds.safeRight(worldWidth, worldHeight) - btnW, btnW,
            worldWidth, worldHeight);
        float y = UiBounds.clampY(UiBounds.safeTop(worldHeight) - btnH, btnH, worldHeight);
        sound.set(x, y, btnW, btnH, game.settings.isAudioEnabled() ? "SOUND" : "MUTE");
    }
}
