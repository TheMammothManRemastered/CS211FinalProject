package rootPackage.FloorGeneration.Features;

import rootPackage.Player;

public class EnemyFeature extends Feature implements Fightable{

    private final boolean aggressive;
    private final String statJSON;

    public EnemyFeature(String[] names, String locationInRoom, boolean aggressive, String statJSON) {
        super(names, locationInRoom);
        this.aggressive = aggressive;
        this.statJSON = statJSON;
    }

    @Override
    public void onExamine(Player player) {

    }

    @Override
    public void onInteract(Player player) {

    }

    @Override
    public String getStatJSON() {
        return statJSON;
    }

    @Override
    public boolean getAggressive() {
        return aggressive;
    }
}
