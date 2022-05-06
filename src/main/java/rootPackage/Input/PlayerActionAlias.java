package rootPackage.Input;

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
            case "attack", "engage", "kill", "charge", "destroy", "decimate", "terminate", "eliminate", "stab", "shoot" -> {
                action = PlayerAction.ATTACK;
            }
        }
        return action;
    }

}
