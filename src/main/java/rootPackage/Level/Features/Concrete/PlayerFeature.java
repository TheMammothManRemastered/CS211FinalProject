package rootPackage.Level.Features.Concrete;

import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;
import rootPackage.Player;

import java.util.ArrayList;

public class PlayerFeature extends Feature {

    public PlayerFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        this.parent = null;
        this.children = new ArrayList<>();
        this.flags = new ArrayList<>();
        flags.add(FeatureFlag.PLAYER);
    }

    public void updateCurrentRoom(Player player) {
        this.parent = player.getCurrentRoom().getRoomAsFeature();
    }

    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }
}
