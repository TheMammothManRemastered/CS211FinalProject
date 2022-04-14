package rootPackage.FloorGeneration.Features;

import rootPackage.FloorGeneration.Room;
import rootPackage.Player;

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

    private String[] names;
    private String locationInRoom;
    private Room associatedRoom;
    private String description;

    public Feature(String[] names, String locationInRoom) {
        this.names = names;
        this.locationInRoom = locationInRoom;
    }

    public Feature(String[] names, String locationInRoom, Room associatedRoom) {
        this.names = names;
        this.locationInRoom = locationInRoom;
        this.associatedRoom = associatedRoom;
    }

    public Room getAssociatedRoom() {
        return associatedRoom;
    }

    public void setAssociatedRoom(Room associatedRoom) {
        this.associatedRoom = associatedRoom;
    }

    public String[] getNames() {
        return names;
    }

    public String getLocationInRoom() {
        return locationInRoom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean nameContains(String input) {
        for (String st : names) {
            if (input.equals(st)) return true;
        }
        return false;
    }

    public abstract void onExamine(Player player);

    public abstract void onInteract(Player player);

    //TODO: once graphics is implemented, add drawing relevant stuff to this
}
