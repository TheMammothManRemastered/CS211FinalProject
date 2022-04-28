package rootPackage.Level.Features;

import rootPackage.Input.PlayerAction;

import java.awt.*;
import java.util.ArrayList;

/**
 * Parent abstract class of all Features one may find in a room.
 *
 * @author William Owens
 * @version 3.0
 */
public abstract class Feature {

    //TODO: refactor this so it's more generic/flexible
    // for each floor, a "palette" will be made of possible room features, two or three per common spot.
    // the spots (this is what was, at one time, locationInRoom) are stored in the JSON files for the features.
    // in code, the spots are not present, instead being replaced by a higher-level version of associatedRoom.
    // speaking of, Room associatedRoom can be refactored to FeatureContainer container.
    // this will represent whatever feature contains this one (ie. a skeleton holding a key is represented by a key contained by a skeleton contained by a room)
    // also, in Room, convert it to a feature and add it to its feature list.
    // the room as a feature can contain its doors, and every other feature inside it.
    // vastly simplifies things
    // the names[] can be kept, maybe have a String primaryName for convenience
    // be sure individual features implement the correct interfaces
    // standardize verbs/actions, have subclasses for each feature ready to handle any possible action
    // features should implement some sort of Drawable interface if they can be drawn to the screen.

    protected String primaryName;
    protected String[] allNames;
    protected Feature parent;
    protected ArrayList<Feature> children;
    protected ArrayList<FeatureFlag> flags;

    public Feature(String primaryName, String[] allNames) {
        this.primaryName = primaryName;
        this.allNames = allNames;
        parent = null;
        children = new ArrayList<>();
        flags = new ArrayList<>();
    }

    public Feature(String primaryName, String[] allNames, Feature parent, ArrayList<Feature> children, ArrayList<FeatureFlag> flags) {
        this.primaryName = primaryName;
        this.allNames = allNames;
        this.parent = parent;
        this.children = children;
        this.flags = flags;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public String[] getAllNames() {
        return allNames;
    }

    public Feature getParent() {
        return parent;
    }

    public ArrayList<Feature> getChildren() {
        return children;
    }

    public ArrayList<Feature> getChildren(FeatureFlag flag) {
        ArrayList<Feature> output = new ArrayList<>();
        for (Feature child : children) {
            if (child.hasFlag(flag)) {
                output.add(child);
            }
        }
        return output;
    }

    public ArrayList<FeatureFlag> getFlags() {
        return flags;
    }

    public boolean hasFlag(FeatureFlag flag) {
        return flags.contains(flag);
    }

    public void setParent(Feature parent) {
        this.parent = parent;
    }

    public void setChildren(ArrayList<Feature> children) {
        this.children = children;
    }

    public void setFlags(ArrayList<FeatureFlag> flags) {
        this.flags = flags;
    }

    public void removeChild(Feature child) {
        children.remove(child);
    }

    public boolean belongsToPlayer() {
        do {
            Feature parent = this.getParent();
            if (parent == null) {
                return false;
            }
        } while (!(parent.hasFlag(FeatureFlag.PLAYER)));
        return true;
    }

    public abstract void react(PlayerAction playerAction);

    public abstract void onActionNotApplicable(PlayerAction playerAction);

}
