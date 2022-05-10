package rootPackage.Level.Features.TopLevel;

import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.Feature;
import rootPackage.Level.Features.FeatureFlag;

import java.util.ArrayList;

public class PlayerFeature extends Feature {

    //TODO: refactor this. it will always have the same names
    public PlayerFeature(String primaryName, String[] allNames) {
        super(primaryName, allNames);
        this.parent = null;
        this.children = new ArrayList<>();
        this.flags = new ArrayList<>();
        flags.add(FeatureFlag.PLAYER);
    }

    // these are needed, but never used

    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }
}
