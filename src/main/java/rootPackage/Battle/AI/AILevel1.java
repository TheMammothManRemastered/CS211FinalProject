package rootPackage.Battle.AI;

import org.json.simple.*;
import rootPackage.Battle.Actions.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This is the first level of ai
 * which the best move is determined at random
 * the enemy doesn't choose much
 * @author jovin
 */
public class AILevel1 extends AI{

    //moron, random selection
    @Override
    public Action determineBestMove(List<Action> validActions) {
            //Gets the list from which the action is picked and
            //returns at random what the action will be
        Random rand = new Random();
        System.out.println(Arrays.toString(validActions.toArray()));
        int random = rand.nextInt(validActions.size());
        return validActions.get(random);
    }
}
