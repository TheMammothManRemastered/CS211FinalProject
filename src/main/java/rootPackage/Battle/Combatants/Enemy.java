package rootPackage.Battle.Combatants;

import rootPackage.Battle.AI.AI;
import rootPackage.Battle.AI.AILevel1;
import rootPackage.Battle.Actions.Action;
import rootPackage.Level.Features.Feature;

import java.util.ArrayList;
import java.util.List;

/**
 * The enemy class, represents the boasses that the
 * player will fight
 * has an additional data type AI, which determines the best move for the enemy
 * @author jovin
 */
public class Enemy extends Combatant{

    private AI enemyAI;
    private ArrayList<Action> actions;
    private int gold;
    private String[] dropFeatures;

    //Constructors
     public Enemy(){
         this(500, 450, 60, 0.2, 2, new AILevel1());
     }

    public Enemy(int maxHp, int currentHp, int attack, double block, int priority, AI enemyAI){
         super(maxHp,currentHp,attack,block,priority);
         this.enemyAI = enemyAI;
         this.dropFeatures = new String[0];
     }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String[] getDropFeatures() {
        return dropFeatures;
    }

    public void setDropFeatures(String[] dropFeatures) {
        this.dropFeatures = dropFeatures;
    }

    public AI getEnemyAI(){
         return this.enemyAI;
     }

     public void setEnemyAI(AI enemyAI){
         this.enemyAI = enemyAI;    //Althought this is not necessary
     }

    @Override
    public Action askForInput() {
        return enemyAI.determineBestMove(this.getValidActions());
    }
}
