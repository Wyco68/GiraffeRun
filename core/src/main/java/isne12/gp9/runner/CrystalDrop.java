package isne12.gp9.runner;

public class CrystalDrop extends Drop {

    public CrystalDrop(Main game) {
        super(game);
    }

    @Override
    public void onCatch(Player player) {
        player.collectCrystal();
    }
}
