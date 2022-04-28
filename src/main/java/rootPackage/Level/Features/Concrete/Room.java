package rootPackage.Level.Features.Concrete;

import rootPackage.Level.Features.Feature;
import rootPackage.Input.PlayerAction;
import rootPackage.Level.Features.FeatureFlag;

import java.util.ArrayList;

/**
 * A class representing a room as a top-level feature.
 * <p></p>
 * NOTE: This class is NOT to be confused with {@link rootPackage.Level.FloorGeneration.Layout.RoomNode RoomNode}!
 * If {@link rootPackage.Level.FloorGeneration.Layout.RoomNode RoomNode} is to the number of an office room, then {@link Room Room} is to that room's contents.
 *
 * @version 1.1
 * @author William Owens
 */
public class Room extends Feature {

    public Room(String primaryName, String[] allNames, Feature parent, ArrayList<Feature> children, ArrayList<FeatureFlag> flags) {
        super(primaryName, allNames, parent, children, flags);
    }

    @Override
    public void react(PlayerAction playerAction) {

    }

    @Override
    public void onActionNotApplicable(PlayerAction playerAction) {

    }
}
