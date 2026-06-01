package isne12.gp9.runner;

/** Campaign level parameters (spawn pacing, drop speed, loot weights). */
public final class LevelConfig {
    public static final int MAX_LEVEL = 4;

    public final int level;
    /** Seconds between spawns (lower = more enemies). */
    public final float spawnInterval;
    /** Multiplier applied to drop fall speed after init. */
    public final float dropSpeedMultiplier;
    /** Relative weight for bullet drops (normalized in {@link GameScreen#createDrop}). */
    public final float bulletWeight;
    public final float healthWeight;

    public LevelConfig(int level, float spawnInterval, float dropSpeedMultiplier,
                       float bulletWeight, float healthWeight) {
        this.level = level;
        this.spawnInterval = spawnInterval;
        this.dropSpeedMultiplier = dropSpeedMultiplier;
        this.bulletWeight = bulletWeight;
        this.healthWeight = healthWeight;
    }

    public static final LevelConfig[] BY_TIER = {
        new LevelConfig(1, 1.25f, 0.85f, 0.38f, 0.14f),
        new LevelConfig(2, 1.0f, 1.0f, 0.48f, 0.11f),
        new LevelConfig(3, 0.72f, 1.18f, 0.60f, 0.09f),
        new LevelConfig(4, 0.5f, 1.35f, 0.74f, 0.07f),
    };

    public static LevelConfig forLevel(int level) {
        int idx = Math.max(0, Math.min(MAX_LEVEL - 1, level - 1));
        return BY_TIER[idx];
    }
}
