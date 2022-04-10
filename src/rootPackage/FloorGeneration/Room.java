package rootPackage.FloorGeneration;

import rootPackage.Direction;
import rootPackage.FloorGeneration.Features.Door;
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

    private MyPoint2D coordinates;
    private ArrayList<Feature> features;
    private boolean isDeadEnd;
    private boolean spawnRoom;

    public Room(ProtoRoom protoRoom) {
        this.coordinates = protoRoom.getCoordinates();
        this.features = new ArrayList<>();
        this.isDeadEnd = false;
        this.spawnRoom = false;
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
            if (feature.nameContains("door")) {
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
            if (feature.nameContains("door")) {
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
