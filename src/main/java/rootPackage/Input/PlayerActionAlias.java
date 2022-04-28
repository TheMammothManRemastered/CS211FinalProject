package rootPackage.Input;

public class PlayerActionAlias {

    public static PlayerAction aliasToAction(String alias) {
        PlayerAction action = null;
        switch (alias) {
            case "take", "grab", "steal", "pick", "obtain" -> {
                action = PlayerAction.PICKUP;
            }
            case "leave", "drop", "discard", "remove", "put" -> {
                action = PlayerAction.DROP;
            }
            case "look", "examine", "view", "check" -> {
                action = PlayerAction.EXAMINE;
            }
            case "unlock", "unlatch" -> {
                action = PlayerAction.UNLOCK;
            }
            case "interact", "use", "activate", "press", "open" -> {
                action = PlayerAction.USE;
            }
            case "attack", "engage", "kill", "charge" -> {
                action = PlayerAction.ATTACK;
            }
        }
        return action;
    }

}
