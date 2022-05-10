package rootPackage.Input;

import rootPackage.Main;
import rootPackage.Player;

/**
 * Enum representing all of the possible player actions.
 *
 * @author William Owens
 * @version 1.0
 */
public enum PlayerAction {
    PICKUP,
    DROP,
    EXAMINE,
    UNLOCK,
    USE,
    ATTACK,
    EQUIP;

    private final Player player;

    PlayerAction() {
        this.player = Main.player;
    }

    public Player getPlayer() {
        return player;
    }
}
