package rootPackage.Logic;

import org.json.simple.*;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Main;

/**
 * Holder class for managing certain inter-class interactions.
 *
 * @author William Owens
 * @version 1.0
 */
public class GameManager {

    /**
     * Executes a {@link PlayerAction PlayerAction} on a given {@link Feature target}.
     */
    public static void executeAction(PlayerAction action, Feature target) {
        target.react(action);
        Main.mainWindow.getViewportPanel().drawRoom(Main.player.getCurrentRoom().getRoomAsFeature());
    }

}
