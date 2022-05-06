package rootPackage.Battle.AI;

import rootPackage.Battle.Actions.Action;
import rootPackage.Battle.Combatants.Enemy;
import rootPackage.Battle.Combatants.Player;

import java.util.List;

/**
 * Work in progresss
 * Don't know how to get the status effects from player and enemy
 * Since we never initalized a status effect variable in either of them
 * If I were to initialize it now, Will have to recreate all the Combatants callings,
 * Which is tedious, for a variable that will not be used for any other mobs
 * Please help
 */

/*
public class AILevel4 extends AI{


    @Override
    public Action determineBestMove(List<Action> validActions) {
        Action dealDamage = new Action();
        Enemy currentEnemy = new Enemy();
        Action raiseAttack = new Action();
        Action reduceAttack = new Action();
        Player player = new Player();

        for(Action action: validActions){
            if(!(action.getIntentsToApply() instanceof DealDamageIntent)) {
                currentEnemy = (Enemy) action.getIntentsToApply().getTarget();
                if(action.getIntentsToApply() instanceof RaiseAttackIntent)
                    raiseAttack = action;
            }
            else if(action.getIntentsToApply() instanceof DealDamageIntent) {
                dealDamage = action;
                player = (Player) dealDamage.getIntentsToApply().getTarget();
                break;
            }
            else if(action.getIntentsToApply() instanceof ReduceDamageIntent){
                reduceAttack = action;
            }
        }

        if(player.getAttack() >= currentEnemy.getCurrentHp()/2){
            return reduceAttack;
        }
        else if(player.getAttack() >= currentEnemy.getCurrentHp()/3){
            return dealDamage;
        }
        else
            return raiseAttack;
    }
}

 */
