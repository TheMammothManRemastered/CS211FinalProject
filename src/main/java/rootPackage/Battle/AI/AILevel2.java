package rootPackage.Battle.AI;

import rootPackage.Battle.Actions.Action;

import java.util.List;
import java.util.Random;

/**
 * Ai level 2, This enemy is a little more competent
 * The best move is determined by all the time attack
 *
 * @author jovin
 */
public class AILevel2 extends AI {

    //Attack hungry
    @Override
    public Action determineBestMove(List<Action> validActions) {
        Action bestAttack = null;
        int thresh = 0;
        for (Action action : validActions) {
            int dam = action.doesDamage();
            if (dam > thresh) {
                thresh = dam;
                bestAttack = action;
            }
        }
        if (bestAttack != null) {
            return bestAttack;
        }
        Random rand = new Random();
        int selectedMove = rand.nextInt(validActions.size());
        return validActions.get(selectedMove);
    }
}
