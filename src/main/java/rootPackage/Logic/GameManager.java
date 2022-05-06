package rootPackage.Logic;

import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Floor;
import rootPackage.Main;

import java.util.Arrays;

public class GameManager {

    public static void executeAction(PlayerAction action, Feature target) {
        target.react(action);
        Main.mainWindow.getViewportPanel().drawRoom(Main.player.getCurrentRoom().getRoomAsFeature());
    }

}
