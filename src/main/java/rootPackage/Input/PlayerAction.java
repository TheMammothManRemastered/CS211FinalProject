package rootPackage.Input;

import rootPackage.Main;
import rootPackage.Player;

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
