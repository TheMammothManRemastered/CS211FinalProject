package rootPackage.Battle.AI;

import rootPackage.Battle.Actions.Action;
import rootPackage.Battle.Combatants.Enemy;
import rootPackage.Battle.Combatants.Player;
import rootPackage.Main;

import java.util.List;
import java.util.Random;

/**
 * this ai is a little more competent
 * decides if it has to attack or defend based on what the player damage is
 *
 * @author jovin
 */
public class AILevel3 extends AI{

    @Override
    public Action determineBestMove(List<Action> validActions) {

        Enemy enemy = Main.currentBattle.enemy;
        Player player = Main.currentBattle.player;

        int damageThreshold = (int) (enemy.getMaxHp()/2.0);
        boolean isVeryDamaged = enemy.getCurrentHp() <= damageThreshold;

        // determine which action of the player's does the most damage
        int playerDamageOutput = 0;
        for (Action action : player.getValidActions()) {
            playerDamageOutput += action.doesDamage();
        }

        Random rand = new Random();

        // almost dead anyway, no point in defending any further. prioritize attacking
        if (isVeryDamaged) {
            return new AILevel2().determineBestMove(validActions);
        }
        if (playerDamageOutput > damageThreshold) {
            // player does too much damage in one hit, try to defend
            Action defAction = null;
            for (Action action : validActions) {
                //TODO: implement a json file to hold all defense-raising actions, since there may eventually be more than one
                if (action.getName().equals("defend")) {
                    defAction = action;
                    break;
                }
            }
            // if defense is an option, much more likely to go for it
            if (defAction != null) {
                if (rand.nextInt(101) > 25) {
                    return defAction;
                }
                // might choose the wrong one, so just attack
                return new AILevel2().determineBestMove(validActions);
            } else {
                return new AILevel2().determineBestMove(validActions);
            }
        } else {
            // unconcerned with player damage output, likely to choose an attack
            if (rand.nextInt(101) > 20) {
                return new AILevel2().determineBestMove(validActions);
            }
            // or pick one at random
            return validActions.get(rand.nextInt(validActions.size()));
        }


    }
}
