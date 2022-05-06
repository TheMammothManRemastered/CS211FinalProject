package rootPackage.Level.Features.Enemy;

import rootPackage.Battle.BattleSupervisor;
import rootPackage.Battle.Combatants.Player;
import rootPackage.Level.Features.Enemy.Bosses.Minotaur;
import rootPackage.Level.Features.Enemy.Enemies.Bear;
import rootPackage.Level.Features.Enemy.Enemies.Wolf;
import rootPackage.Level.Features.Enemy.Enemies.WolfPack;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.util.ArrayList;

public abstract class Enemy extends Feature {

    protected String jsonFile;

    public Enemy() {
        this.flags.add(FeatureFlag.ENEMY);
    }

    public Enemy(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        this.flags.add(FeatureFlag.ENEMY);
    }

    public Enemy(String primaryName, String[] allNames, Feature parent, ArrayList<Feature> children, ArrayList<FeatureFlag> flags) {
        super(primaryName, allNames, parent, children, flags);
    }

    public Enemy(String primaryName, String[] allNames, String jsonFile) {
        super(primaryName, allNames);
        this.jsonFile = "json"+System.getProperty("file.separator")+"enemyStats"+System.getProperty("file.separator")+jsonFile;
    }

    public void fight() {
        System.out.println("going with file "+this.jsonFile);
        Main.battleManagerMain.go(jsonFile);
    }

    public static Enemy jsonToEnemy(String jsonFile) {
        switch (jsonFile) {
            case "bear.json" -> {
                return new Bear();
            }
            case "wolfPack.json" -> {
                return new WolfPack();
            }
            case "minotaur.json" -> {
                return new Minotaur();
            }
            default -> {
                return new Wolf();
            }
        }
    }
}
