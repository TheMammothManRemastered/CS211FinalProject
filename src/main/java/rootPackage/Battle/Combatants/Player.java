package rootPackage.Battle.Combatants;

import rootPackage.Battle.Actions.Action;

import java.util.List;
import java.util.Scanner;

/**
 * A class representing the player's battle statistics.
 *
 * @version 1.0
 * @author Jovin Antony-Maria
 */
public class Player extends Combatant {

    //Constructors
    public Player(){
        this(500,450,60,0.2,2);
    }
    public Player(int maxHp, int currentHp, int attack, double block, int priority){
        super(maxHp,currentHp,attack,block,priority);
    }

    public Action askingForInput(List<Action> validActions) {   /** don't know how the input works*/
        //interface.display(validActions);
        return null;//interface.takeInput();
                    //prob get it from the json file,
                    //not sure
    }
}
