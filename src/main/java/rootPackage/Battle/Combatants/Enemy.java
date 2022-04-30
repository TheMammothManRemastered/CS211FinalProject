package rootPackage.Battle.Combatants;

import rootPackage.Battle.AI.AI;
import rootPackage.Battle.AI.AILevel1;
import rootPackage.Battle.Actions.Action;

import java.util.List;

/**
 * A class representing the stats of an enemy in a battle.
 *
 * @version 1.0
 * @author Jovin Antony-Maria
 */
public class Enemy extends Combatant{

    private AI enemyAI;

    //Constructors
     public Enemy(){
         this(500, 450, 60, 0.2, 2, new AILevel1());
     }
     public Enemy(int maxHp, int currentHp, int attack, double block, int priority, AI enemyAI){
         super(maxHp,currentHp,attack,block,priority);
         this.enemyAI = enemyAI;
     }

    //Getting the enemy Action
    public Action askingForInput(List<Action> validActions) {
        return enemyAI.determineBestMove(validActions);
    }


}
