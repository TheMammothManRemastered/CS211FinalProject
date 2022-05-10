package rootPackage.Level.Features;

import rootPackage.Graphics.Viewport.Sprite;
import rootPackage.Input.PlayerAction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parent abstract class of all Features one may find in a room.
 *
 * @author William Owens
 * @version 3.0
 */
public abstract class Feature {

    // features are a bit too complex to be easily implemented as JSON files unfortunately

    protected String primaryName;
    protected String[] allNames;
    protected Feature parent;
    protected ArrayList<Feature> children;
    protected ArrayList<FeatureFlag> flags;
    protected Sprite sprite;
    protected String examineText;

    public Feature() {
        primaryName = null;
        allNames = null;
        parent = null;
        children = new ArrayList<>();
        flags = new ArrayList<>();
        sprite = new Sprite();
        examineText = "";
    }

    public Feature(String primaryName, String[] allNames) {
        this.primaryName = primaryName;
        this.allNames = allNames;
        parent = null;
        children = new ArrayList<>();
        flags = new ArrayList<>();
        sprite = new Sprite();
        examineText = "";
    }

    public Feature(String primaryName, String[] allNames, Feature parent, ArrayList<Feature> children, ArrayList<FeatureFlag> flags) {
        this.primaryName = primaryName;
        this.allNames = allNames;
        this.parent = parent;
        this.children = children;
        this.flags = flags;
        sprite = new Sprite();
        examineText = "";
    }

    public String getExamineText() {
        return examineText;
    }

    public void setExamineText(String examineText) {
        this.examineText = examineText;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
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

    public void setFlags(ArrayList<FeatureFlag> flags) {
        this.flags = flags;
    }

    public void removeChild(Feature child) {
        children.remove(child);
    }

    /**
     * Properly reparents a Feature in the tree, 'moving' it to a new spot.
     */
    public void reparentSelf(Feature newParent) {
        this.getParent().removeChild(this);
        newParent.addChild(this);
    }

    public boolean hasChildren() {
        return (this.children.size() > 0);
    }

    /**
     * Checks if any of this feature's possible names matches the given input.
     */
    public boolean isNamed(String name) {
        System.out.println("names of the %s".formatted(primaryName));
        System.out.println(Arrays.toString(allNames));
        System.out.println("query is named this? %s".formatted(primaryName.toLowerCase().equals(name)));
        System.out.println("query is named this at all? %s".formatted(Arrays.asList(allNames).contains(name)));
        System.out.println();
        if (primaryName.toLowerCase().equals(name)) {
            return true;
        }
        return Arrays.asList(allNames).contains(name);
    }

    /**
     * Returns the first child of this feature (or the feature itself) that has the given input as a possible name.
     * If no such feature exists, returns null.
     */
    public Feature getChildWithName(String name) {
        System.out.println("beginning getChildWithName in %s".formatted(primaryName));
        name = name.strip();
        if (this.isNamed(name)) {
            return this;
        }
        System.out.println("%s isn't the child straight up, moving to recursion".formatted(primaryName));
        return getChildWithNameRecursive(name, this.getChildren());
    }

    private Feature getChildWithNameRecursive(String name, ArrayList<Feature> featuresToSearch) {
        System.out.println("checking children of %s. current queue is as follows".formatted(primaryName));
        System.out.println(Arrays.toString(featuresToSearch.toArray()));
        boolean terminate = true;
        // a combination of all of the features in the next 'level' down from the one currently being checked
        ArrayList<Feature> nextLevel = new ArrayList<>();
        for (Feature feature : featuresToSearch) {
            System.out.println("checking feature %s, name %s".formatted(feature, feature.primaryName));
            if (feature.isNamed(name)) {
                System.out.println("echo found!!");
                System.out.println(feature == null);
                return feature;
            }
            if (feature.hasChildren()) {
                terminate = false; // if there are still children to check, we won't be breaking now
                nextLevel.addAll(feature.getChildren());
            }
        }
        if (terminate) { // base case, no more children exist and the feature hasn't already been found
            return null;
        }
        return getChildWithNameRecursive(name, nextLevel);
    }

    /**
     * Reparents a given feature to this one.
     */
    public void addChild(Feature child) {
        if (child.getParent() != null)
            child.getParent().removeChild(child);
        child.setParent(this);
        children.add(child);
    }

    public void removeFromPlay() {
        this.getParent().removeChild(this);
        this.setParent(null);
    }

    /**
     * The react method encompasses any feature's reaction to an action the player may perform to it.
     * Should an action not be applicable to the feature, {@link rootPackage.Level.Features.Feature#onActionNotApplicable(PlayerAction) onActionNotApplicable} will be called.
     */
    public abstract void react(PlayerAction playerAction);

    /**
     * The onActionNotApplicable method is functionally identical to {@link #react(PlayerAction) react}, but should be
     * used for actions that a features shouldn't respond to very much (ie. trying to pick up a doorframe).
     */
    public abstract void onActionNotApplicable(PlayerAction playerAction);

}
