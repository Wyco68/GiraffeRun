package isne12.gp9.runner;

public class HealthDrop extends Drop {

    public HealthDrop(Main game) {
        super(game);
    }

    @Override
    public void onCatch(Player player) {
        player.heal();
    }
}
