package rootPackage.Battle.AI;

import rootPackage.Battle.Actions.Action;
import rootPackage.Battle.Combatants.Enemy;

import java.util.List;
import java.util.Random;

/**
 * this ai is a little more competent
 * decides if it has to attack or defend based on what the player damage is
 * @author jovin
 */
/*
public class AILevel3 extends AI{
    @Override
    public Action determineBestMove(List<Action> validActions) {

        Action dealDamage = new Action();
        Enemy currentEnemy = new Enemy();
        Action raiseAttack = new Action();

        //This loop initializes all the actions possible with the specific modifier
        for(Action action: validActions){
            if(!(action.getIntentsToApply() instanceof DealDamageIntent)) {
                currentEnemy = (Enemy) action.getIntentsToApply().getTarget();
                if(action.getIntentsToApply() instanceof RaiseAttackIntent)
                    raiseAttack = action;
            }
            if(action.getIntentsToApply() instanceof DealDamageIntent) {
                dealDamage = action;
                break;
            }
        }

        Random rand = new Random();
        int random = rand.nextInt(2);

        //Choses if it wants to defend or attack
        if(dealDamage.getIntentsToApply().getTarget().getAttack() >= currentEnemy.getCurrentHp()/2){
            if(random == 1)
                return raiseAttack;
            else
                return dealDamage;
        }

    //default value
        return dealDamage;
    }
}
 */
