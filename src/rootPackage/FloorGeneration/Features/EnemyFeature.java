package rootPackage.FloorGeneration.Features;

import rootPackage.Player;

public class EnemyFeature extends Feature implements Fightable{

    private final boolean aggressive;
    private final String statJSON;

    public EnemyFeature(String[] names, String primaryName, String description, FeatureContainer container, boolean aggressive, String statJSON) {
        super(names, primaryName, description, container);
        this.aggressive = aggressive;
        this.statJSON = statJSON;
    }

    @Override
    public String toString() {
        return "enemy name is %s".formatted(statJSON);
    }

    @Override
    public String getStatJSON() {
        return null;
    }

    @Override
    public boolean getAggressive() {
        return false;
    }
}
