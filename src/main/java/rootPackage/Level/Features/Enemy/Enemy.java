package rootPackage.Level.Features.Enemy;

import org.json.simple.*;
import rootPackage.Level.Features.Enemy.Bosses.Minotaur;
import rootPackage.Level.Features.Enemy.Enemies.Bear;
import rootPackage.Level.Features.Enemy.Enemies.Wolf;
import rootPackage.Level.Features.Enemy.Enemies.WolfPack;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Main;

import java.util.ArrayList;

/**
 * Parent ADT of all enemy {@link Feature Feature}s.
 *
 * @author William Owens
 * @version 2.2
 */
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
        this.jsonFile = "json" + System.getProperty("file.separator") + "enemyStats" + System.getProperty("file.separator") + jsonFile;
    }

    /**
     * Converts a json file to a given enemy.
     */
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

    /**
     * Starts combat with this enemy.
     */
    public void fight() {
        Main.battleManagerMain.go(jsonFile);
    }
}
