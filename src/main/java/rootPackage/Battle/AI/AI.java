package rootPackage.Battle.AI;

import org.json.simple.*;
import rootPackage.Battle.Actions.Action;

import java.util.List;

/**
 * This is the AI class for the enemy
 * THis takes in a list of actions,
 * which then determines the best move for the enemy
 * to make
 * @author jovin
 */
//Bases of all AI, helps with polymorphism
public abstract class AI {
    public abstract Action determineBestMove(List<Action> validActions);
}
