package rootPackage.Battle.AI;

import rootPackage.Battle.Actions.Action;

import java.util.List;

/**
 * Parent ADT of all AI levels.
 *
 * @author Jovin Antony-Maria
 * @version 1.0
 */
public abstract class AI {
    public abstract Action determineBestMove(List<Action> validActions);
}
