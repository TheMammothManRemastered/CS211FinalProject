package rootPackage.Battle.AI;

import org.json.simple.*;
import rootPackage.Battle.Actions.Action;

import java.util.List;
import java.util.Random;

/**
 * Ai level 2, This enemy is a little more competent
 * The best move is determined by all the time attack
 * @author jovin
 */
public class AILevel2 extends AI{

    //Attack hungry
    @Override
    public Action determineBestMove(List<Action> validActions) {
        for(Action action: validActions){
            if(action.getName().equals("attack"))
                return action;
        }
        Random rand = new Random();
        int selectedMove = rand.nextInt(validActions.size());
        return validActions.get(selectedMove);
    }
}
