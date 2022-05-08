package rootPackage.Input;

import org.json.simple.*;

/**
 * Class responsible for mapping a name to a specific player action.
 *
 * @author William Owens
 * @version 1.04
 */
public class PlayerActionAlias {

    public static PlayerAction aliasToAction(String alias) {
        PlayerAction action = null;
        switch (alias) {
            case "take", "grab", "steal", "pick", "obtain", "don", "wear", "equip" -> {
                action = PlayerAction.PICKUP;
            }
            case "leave", "drop", "discard", "remove", "put", "doff" -> {
                action = PlayerAction.DROP;
            }
            case "look", "examine", "view", "check", "inspect" -> {
                action = PlayerAction.EXAMINE;
            }
            case "unlock", "unlatch" -> {
                action = PlayerAction.UNLOCK;
            }
            case "interact", "use", "activate", "press", "open", "travel", "move" -> {
                action = PlayerAction.USE;
            }
            case "attack", "engage", "kill", "charge", "destroy", "decimate", "terminate", "eliminate", "stab", "shoot", "fight", "battle" -> {
                action = PlayerAction.ATTACK;
            }
        }
        return action;
    }

}
