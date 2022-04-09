package rootPackage.FloorGeneration.Features;

import rootPackage.FloorGeneration.Room;
import rootPackage.Player;

/**
 * Interface of all Features one may find in a room.
 */
public abstract class Feature {

    private String[] names;
    private String locationInRoom;
    private Room associatedRoom;

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
