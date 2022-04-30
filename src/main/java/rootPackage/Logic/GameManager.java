package rootPackage.Logic;

import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;

public class GameManager {

    public static void executeAction(PlayerAction action, Feature target) {
        target.react(action);
    }

}
