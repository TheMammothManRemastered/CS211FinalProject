package rootPackage.Battle.AI;

import rootPackage.Battle.Actions.Action;
import java.util.List;
import java.util.Random;

/**
 * AI level 1, the brains of an unintelligent fool. Will pick an action at random, with no regard for player statistics
 *
 * @author Jovin Antony-Maria
 * @version 1.0
 */
public class AILevel1 extends AI{

    @Override
    public Action determineBestMove(List<Action> validActions) {
            //Gets the list from which the action is picked and
            //returns at random what the action will be
        Random rand = new Random();
        int random = rand.nextInt(validActions.size());
        return validActions.get(random);
    }
}
