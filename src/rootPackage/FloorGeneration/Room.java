package rootPackage.FloorGeneration;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Door;
import rootPackage.FloorGeneration.Features.Feature;
import rootPackage.FloorGeneration.Layout.MyPoint2D;
import rootPackage.FloorGeneration.Layout.ProtoRoom;

import java.util.ArrayList;

/**
 * A class representing a room of a floor.
 *
 * @author William Owens
 * @version 2.0
 */
public class Room {

    //TODO: refactor this so that features are handled using the tree-like structure
    // instead of having a list of features here, have a reference to a feature representing the room
    // for adding doors, have another list to contain them here; doors are special
    // if features get added to the room here (which should only happen with doors), get the associated feature representing the room and add them to that

    private MyPoint2D coordinates;
    private ArrayList<Feature> features;
    private boolean isDeadEnd;
    private boolean spawnRoom;
    private String roomDescription;

    /**
     * Construct the room from a protoRoom.
     * @param protoRoom
     */
    public Room(ProtoRoom protoRoom) {
        this.coordinates = protoRoom.getCoordinates();
        this.features = new ArrayList<>();
        this.isDeadEnd = false;
        this.spawnRoom = false;
        this.roomDescription = null;
    }

    public boolean isSpawnRoom() {
        return spawnRoom;
    }

    public void setSpawnRoom(boolean spawnRoom) {
        this.spawnRoom = spawnRoom;
    }

    public ArrayList<Feature> getFeatures() {
        return features;
    }

    public MyPoint2D getCoordinates() {
        return coordinates;
    }

    public boolean isDeadEnd() {
        return isDeadEnd;
    }

    public void setDeadEnd(boolean deadEnd) {
        isDeadEnd = deadEnd;
    }

    public boolean hasDoorInDirection(Direction dir) {
        ArrayList<Door> doors = new ArrayList<>();
        for (Feature feature : features) {
            if (feature.namesContain("door")) {
                doors.add((Door)feature);
            }
        }
        for (Door door : doors) {
            if (door.getDirection().equals(dir)) {
                return true;
            }
        }
        return false;
    }

    public Door getDoorInDirection(Direction dir) throws Exception {
        ArrayList<Door> doors = new ArrayList<>();
        for (Feature feature : features) {
            if (feature.namesContain("door")) {
                doors.add((Door)feature);
            }
        }
        for (Door door : doors) {
            if (door.getDirection().equals(dir)) {
                return door;
            }
        }
        throw new Exception("no door in that direction");
    }

    public ArrayList<Door> getDoors() {
        ArrayList<Door> output = new ArrayList<>();
        for (Feature feature : features) {
            if (feature instanceof Door) {
                output.add((Door)feature);
            }
        }
        return output;
    }

    public String getRoomDescription() {
        //TODO: generate the room's proper description for the first time if the field is null
        // otherwise, use the field value

        return "man, this is a room. like, what a total bummer.";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Room) {
            return this.getCoordinates().equals(((Room)obj).getCoordinates());
        }
        return false;
    }

    @Override
    public String toString() {
        ArrayList<Door> doors = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("Room at {");
        sb.append(coordinates.toString());
        sb.append("}:\n");
        for (Feature feature : features) {
            if (feature instanceof Door) {
                doors.add((Door) feature);
            }
        }
        for (Door door : doors) {
            sb.append("\tDoor on ");
            sb.append(door.getLocationInRoom());
            sb.append(" [");
            sb.append(door.getDirection());
            sb.append("] leads to room at {");
            sb.append(door.getOtherSide().getAssociatedRoom().getCoordinates().toString());
            sb.append("} on ");
            sb.append(door.getOtherSide().getLocationInRoom());
            sb.append(" [");
            sb.append(door.getOtherSide().getDirection());
            sb.append("]\n");
        }
        return sb.toString();
    }
}
